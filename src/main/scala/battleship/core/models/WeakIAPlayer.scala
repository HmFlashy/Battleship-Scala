package battleship.core.models

import battleship.core.GameConfig
import battleship.utils.ships.Generator

import scala.collection.immutable.Seq
import scala.util.Random

case class WeakIAPlayer(ships: Seq[Ship], name: String, shots: Map[(Int, Int), Boolean], receivedShots: Seq[(Int, Int)], random: Random, numberOfWins: Int) extends Player {

  /**
    *
    * @return
    */
  override def shoot(): (Int, Int) = {
    (random.nextInt(GameConfig.gridSize), random.nextInt(GameConfig.gridSize))
  }

  /**
    *
    * @param shot
    * @return
    */
  override def receiveShoot(shot: (Int, Int)): (WeakIAPlayer, Boolean, Option[Ship]) = {
    val shipShot: Option[Ship] = ships.find(ship => ship.squares.contains(shot))
    shipShot match {
      case Some(ship) => {
        val newShip: Ship = ship.hit(shot)
        val sunk: Boolean = newShip.isSunk()
        (WeakIAPlayer(ships.map { case oldShip if oldShip == ship => newShip; case x => x }, name, shots, receivedShots :+ shot, random, numberOfWins), true, if(sunk) Some(newShip) else None)
      }
      case None => (WeakIAPlayer(ships, name, shots, receivedShots :+ shot, random, numberOfWins), false, None)
    }
  }

  /**
    *
    * @param target
    * @param didTouch
    * @return
    */
  override def didShoot(target: (Int, Int), didTouch: Boolean): WeakIAPlayer = {
    WeakIAPlayer(ships, name, shots + (target -> didTouch), receivedShots, random, numberOfWins)
  }

  override def addVictory(): Player = {
    this.copy(numberOfWins = numberOfWins + 1)
  }

  override def reset(): Player = {
    val newShips: Seq[Ship] = Generator.randomShips(GameConfig.shipsConfig, Seq[Ship](), this.random)
    this.copy(ships = newShips, shots = Map[(Int, Int), Boolean](), receivedShots = Seq[(Int, Int)]())
  }
}

object WeakIAPlayer {
  def generateIA(index: Int, random: Random): WeakIAPlayer = {
    val ships: Seq[Ship] = Generator.randomShips(GameConfig.shipsConfig, Seq[Ship](), random)
    WeakIAPlayer(ships, "Weak IA "+index, Map[(Int, Int), Boolean](), Seq[(Int, Int)](), random, 0)
  }
}
