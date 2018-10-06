package battleship.core.models

import battleship.utils.ships.Generator

import scala.annotation.tailrec
import scala.collection.immutable.Seq
import scala.util.Random

case class StrongAIPlayer(ships: Seq[Ship], name: String, shots: Map[(Int, Int), Boolean], receivedShots: Seq[(Int, Int)], numberOfWins: Int, random: Random) extends Player {

  /**
    *
    * @param gridSize Size of the grid to shoot
    * @return (Int, Int) A tuple that indicates where the player is shooting
    */
  override def shoot(gridSize: Int): (Int, Int) = {
    val hits = shots.filter(shot => shot._2).keys.toSet

    @tailrec
    def hitHasNotShotNeighbours(hits: Set[(Int, Int)]): Option[(Int, Int)] = {
      val hitOption = hits.headOption
      hitOption match {
        case Some(hit) => {
          hit match {
            case up if hit._2 > 0 && shots.get(hit._1, hit._2 - 1).isEmpty => Some(up._1, up._2 - 1)
            case right if hit._1 < gridSize && shots.get((hit._1 + 1, hit._2)).isEmpty => Some(right._1 + 1, right._2)
            case down if hit._2 < gridSize && shots.get((hit._1, hit._2 + 1)).isEmpty => Some(down._1, down._2 + 1)
            case left if hit._1 > 0 && shots.get((hit._1 - 1, hit._2)).isEmpty => Some(left._1 - 1, left._2)
            case _ => hitHasNotShotNeighbours(hits.tail)
          }
        }
        case None => None
      }
    }

    val shotOption = hitHasNotShotNeighbours(hits)
    shotOption.getOrElse({
      val point = (random.nextInt(gridSize), random.nextInt(gridSize))
      Player.findClosestFreeSlot(point, shots, 0, point, Player.UP, gridSize)
    })
  }

  /**
    *
    * @param shot Coordinate of the shot
    * @return (Player, Bookean) A tuple that contains the player that has been modified with the shot and a Boolean indicating if the shot hit one of the shi of the player or not
    */
  override def receiveShoot(shot: (Int, Int)): (StrongAIPlayer, Boolean, Option[Ship]) = {
    val shipShot: Option[Ship] = ships.find(ship => ship.squares.contains(shot))
    shipShot match {
      case Some(ship) => {
        val newShip: Ship = ship.hit(shot)
        val sunk: Boolean = newShip.isSunk()
        (this.copy(ships = ships.map { case oldShip if oldShip == ship => newShip; case x => x }, receivedShots = receivedShots :+ shot), true, if (sunk) Some(newShip) else None)
      }
      case None => (this.copy(receivedShots = receivedShots :+ shot), false, None)
    }
  }

  /**
    *
    * @param target Coordinated of the shot
    * @param didTouch Boolean that indicates if the shot did touch an opponent ship or not
    * @return The player modified with the shot information
    */
  override def didShoot(target: (Int, Int), didTouch: Boolean): StrongAIPlayer = {
    this.copy(shots = shots + (target -> didTouch))
  }

  /**
    *
    * @return The player modified with a victory added
    */
  override def addVictory(): StrongAIPlayer = {
    this.copy(numberOfWins = numberOfWins + 1)
  }

  /**
    *
    * @param shipsConfig The configuration of the ships
    * @param gridSize The size of the grid
    * @return The player reseted
    */
  override def reset(shipsConfig: Map[String, Int], gridSize: Int): StrongAIPlayer = {
    val newShips: Seq[Ship] = Generator.randomShips(shipsConfig, Seq[Ship](), this.random, gridSize)
    this.copy(ships = newShips, shots = Map[(Int, Int), Boolean](), receivedShots = Seq[(Int, Int)]())
  }
}


object StrongAIPlayer {

  /**
    *
    * @param index The index of the IA
    * @param random Instance of the random class
    * @param shipsConfig The configuration of the ships
    * @param gridSize The size of the grid
    * @return A player configured randomly
    */
  def generateIA(index: Int, random: Random, shipsConfig: Map[String, Int], gridSize: Int): StrongAIPlayer = {
    val ships: Seq[Ship] = Generator.randomShips(shipsConfig, Seq[Ship](), random, gridSize)
    StrongAIPlayer(ships, "Strong IA " + index, Map[(Int, Int), Boolean](), Seq[(Int, Int)](), 0, random)
  }
}
