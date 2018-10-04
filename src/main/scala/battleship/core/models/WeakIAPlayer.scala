package battleship.core.models

import battleship.core.GameConfig
import battleship.utils.ships.Generator

import scala.collection.immutable.Seq
import scala.util.Random

case class WeakIAPlayer(ships: Seq[Ship], name: String, shots: Map[(Int, Int), Boolean], receivedShots: Seq[(Int, Int)], random: Random, numberOfWins: Int) extends PlayerTrait {

  /**
    *
    * @return
    */
  override def shoot(): (Int, Int) = {
    val point = (random.nextInt(GameConfig.gridSize), random.nextInt(GameConfig.gridSize))
    point
  }

  /**
    *
    * @param shot
    * @return
    */
  override def receiveShoot(shot: (Int, Int)): (WeakIAPlayer, Boolean, Boolean) = {
    val shipShot: Option[Ship] = ships.find(ship => ship.squares.contains(shot))
    shipShot match {
      case Some(ship) => {
        val newShip: Ship = ship.hit(shot)
        val sank: Boolean = newShip.isSank()
        (WeakIAPlayer(ships.map { case oldShip if oldShip == ship => newShip; case x => x }, name, shots, receivedShots :+ shot, random, numberOfWins), true, sank)
      }
      case None => (WeakIAPlayer(ships, name, shots, receivedShots :+ shot, random, numberOfWins), false, false)
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

  override def addVictory(): PlayerTrait = {
    this.copy(numberOfWins = numberOfWins + 1)
  }

  override def reset(): PlayerTrait = {
    val newShips: Seq[Ship] = Generator.randomShips(GameConfig.shipsConfig, Seq[Ship](), this.random)
    this.copy(ships = newShips, shots = Map[(Int, Int), Boolean](), receivedShots = Seq[(Int, Int)]())
  }
}

object WeakIAPlayer {
  def generateIA(index: Int, random: Random): WeakIAPlayer = {
    val ships: Seq[Ship] = Generator.randomShips(GameConfig.shipsConfig, Seq[Ship](), random)
    WeakIAPlayer(ships, "IA"+index, Map[(Int, Int), Boolean](), Seq[(Int, Int)](), random, 0)
  }
}
