package battleship.core.models

import battleship.core.GameConfig
import battleship.utils.ships.Generator

import scala.annotation.tailrec
import scala.collection.immutable.Seq
import scala.io.StdIn
import scala.util.Random

case class StrongAIPlayer(ships: Seq[Ship], name: String, shots: Map[(Int, Int),Boolean], receivedShots: Seq[(Int, Int)], numberOfWins: Int, random: Random) extends Player {

  /**
    *
    * @return
    */
  override def shoot(): (Int, Int) = {
    val hits = shots.filter(shot => shot._2).keys.toSet

    @tailrec
    def hitHasNotShotNeighbours(hits: Set[(Int, Int)]): Option[(Int, Int)] = {
      val hitOption = hits.headOption
      hitOption match {
        case Some(hit) => {
          hit match {
            case up if shots.get(hit._1, hit._2 - 1).isEmpty => Some(up._1, up._2 - 1)
            case right if shots.get((hit._1 + 1, hit._2)).isEmpty => Some(right._1 + 1, right._2)
            case down if shots.get((hit._1, hit._2 + 1)).isEmpty => Some(down._1, down._2 + 1)
            case left if shots.get((hit._1 - 1, hit._2)).isEmpty => Some(left._1 - 1, left._2)
            case _ => hitHasNotShotNeighbours(hits.tail)
          }
        }
        case None => None
      }
    }

    val shotOption = hitHasNotShotNeighbours(hits)
    shotOption.getOrElse({
      val point = (random.nextInt(GameConfig.gridSize), random.nextInt(GameConfig.gridSize))
      Player.findClosestFreeSlot(point, shots, 0, point, Player.UP)
    })
  }

  /**
    *
    * @param shot
    * @return
    */
  override def receiveShoot(shot: (Int, Int)): (StrongAIPlayer, Boolean, Option[Ship]) = {
    val shipShot: Option[Ship] = ships.find(ship => ship.squares.contains(shot))
    shipShot match {
      case Some(ship) => {
        val newShip: Ship = ship.hit(shot)
        val sunk: Boolean = newShip.isSunk()
        (this.copy( ships = ships.map { case oldShip if oldShip == ship => newShip; case x => x }, receivedShots = receivedShots :+ shot), true, if(sunk) Some(newShip) else None)
      }
      case None => (this.copy(receivedShots = receivedShots :+ shot), false, None)
    }
  }

  /**
    *
    * @param target
    * @param didTouch
    * @return
    */
  override def didShoot(target: (Int, Int), didTouch: Boolean): StrongAIPlayer = {
    this.copy(shots = shots + (target -> didTouch))
  }


  override def addVictory(): StrongAIPlayer = {
    this.copy(numberOfWins = numberOfWins + 1)
  }

  override def reset(): StrongAIPlayer = {
    val newShips: Seq[Ship] = Generator.randomShips(GameConfig.shipsConfig, Seq[Ship](), this.random)
    this.copy(ships = newShips, shots = Map[(Int, Int), Boolean](), receivedShots = Seq[(Int, Int)]())
  }
}


object StrongAIPlayer {
  def generateIA(index: Int, random: Random): StrongAIPlayer = {
    val ships: Seq[Ship] = Generator.randomShips(GameConfig.shipsConfig, Seq[Ship](), random)
    StrongAIPlayer(ships, "Strong IA "+index, Map[(Int, Int), Boolean](), Seq[(Int, Int)](), 0, random)
  }
}
