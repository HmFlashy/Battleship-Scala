package battleship.core.models

import battleship.core.GameConfig
import battleship.utils.ships.Generator

import scala.collection.immutable.Seq
import scala.util.Random

case class StrongAIPlayer(ships: Seq[Ship], name: String, shots: Map[(Int, Int),Boolean], receivedShots: Seq[(Int, Int)], numberOfWins: Int, random: Random) extends PlayerTrait {

  /**
    *
    * @return
    */
  override def shoot(): (Int, Int) = {
    (0, 0)
  }

  /**
    *
    * @param shot
    * @return
    */
  override def receiveShoot(shot: (Int, Int)): (StrongAIPlayer, Boolean, Boolean) = {
    val shipShot: Option[Ship] = ships.find(ship => ship.squares.contains(shot))
    shipShot match {
      case Some(ship) => {
        val newShip: Ship = ship.hit(shot)
        val sank: Boolean = newShip.isSank()
        (this.copy( ships = ships.map { case oldShip if oldShip == ship => newShip; case x => x }, receivedShots = receivedShots :+ shot), true, sank)
      }
      case None => (this.copy(receivedShots = receivedShots :+ shot), false, false)
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
