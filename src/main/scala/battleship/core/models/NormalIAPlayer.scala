package battleship.core.models
import battleship.core.GameConfig
import battleship.utils.ships.Generator

import scala.collection.immutable.Seq
import scala.util.Random

case class NormalIAPlayer(ships: Seq[Ship], name: String, shots: Map[(Int, Int), Boolean], receivedShots: Seq[(Int, Int)], random: Random, numberOfWins: Int) extends Player {

  /**
    *
    * @return
    */
  override def shoot(): (Int, Int) = {
    val point = (random.nextInt(GameConfig.gridSize), random.nextInt(GameConfig.gridSize))
    Player.findClosestFreeSlot(point, shots, 0, point, Player.UP)
  }

  /**
    *
    * @param shot
    * @return
    */
  override def receiveShoot(shot: (Int, Int)): (NormalIAPlayer, Boolean, Option[Ship]) = {
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
  override def didShoot(target: (Int, Int), didTouch: Boolean): NormalIAPlayer = {
      this.copy(shots = shots + (target -> didTouch))
    }

  override def addVictory(): NormalIAPlayer = {
    this.copy(numberOfWins = numberOfWins + 1)
  }

  override def reset(): NormalIAPlayer = {
    val newShips: Seq[Ship] = Generator.randomShips(GameConfig.shipsConfig, Seq[Ship](), this.random)
    this.copy(ships = newShips, shots = Map[(Int, Int), Boolean](), receivedShots = Seq[(Int, Int)]())
  }
}

object NormalIAPlayer {
  def generateIA(index: Int, random: Random): NormalIAPlayer = {
    val ships: Seq[Ship] = Generator.randomShips(GameConfig.shipsConfig, Seq[Ship](), random)
    NormalIAPlayer(ships, "Normal IA "+index, Map[(Int, Int), Boolean](), Seq[(Int, Int)](), random, 0)
  }
}
