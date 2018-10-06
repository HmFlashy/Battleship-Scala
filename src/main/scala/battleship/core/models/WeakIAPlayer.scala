package battleship.core.models

import battleship.utils.ships.Generator

import scala.collection.immutable.Seq
import scala.util.Random

case class WeakIAPlayer(ships: Seq[Ship], name: String, shots: Map[(Int, Int), Boolean], receivedShots: Seq[(Int, Int)], random: Random, numberOfWins: Int) extends Player {

  /**
    *
    * @param gridSize Size of the grid to shoot
    * @return (Int, Int) A tuple that indicates where the player is shooting
    */
  override def shoot(gridSize: Int): (Int, Int) = {
    (random.nextInt(gridSize), random.nextInt(gridSize))
  }

  /**
    *
    * @param shot Coordinate of the shot
    * @return (Player, Bookean) A tuple that contains the player that has been modified with the shot and a Boolean indicating if the shot hit one of the shi of the player or not
    */
  override def receiveShoot(shot: (Int, Int)): (WeakIAPlayer, Boolean, Option[Ship]) = {
    val shipShot: Option[Ship] = ships.find(ship => ship.squares.contains(shot))
    shipShot match {
      case Some(ship) => {
        val newShip: Ship = ship.hit(shot)
        val sunk: Boolean = newShip.isSunk()
        (WeakIAPlayer(ships.map { case oldShip if oldShip == ship => newShip; case x => x }, name, shots, receivedShots :+ shot, random, numberOfWins), true, if (sunk) Some(newShip) else None)
      }
      case None => (WeakIAPlayer(ships, name, shots, receivedShots :+ shot, random, numberOfWins), false, None)
    }
  }

  /**
    *
    * @param target Coordinated of the shot
    * @param didTouch Boolean that indicates if the shot did touch an opponent ship or not
    * @return The player modified
    */
  override def didShoot(target: (Int, Int), didTouch: Boolean): WeakIAPlayer = {
    WeakIAPlayer(ships, name, shots + (target -> didTouch), receivedShots, random, numberOfWins)
  }

  /**
    *
    * @return The player modified with a victory added
    */
  override def addVictory(): WeakIAPlayer = {
    this.copy(numberOfWins = numberOfWins + 1)
  }

  /**
    *
    * @param shipsConfig The configuration of the ships
    * @param gridSize The size of the grid
    * @return A reseted Strong AI player
    */
  override def reset(shipsConfig: Map[String, Int], gridSize: Int): Player = {
    val newShips: Seq[Ship] = Generator.randomShips(shipsConfig, Seq[Ship](), this.random, gridSize)
    this.copy(ships = newShips, shots = Map[(Int, Int), Boolean](), receivedShots = Seq[(Int, Int)]())
  }
}

object WeakIAPlayer {

  /**
    *
    * @param index The index of the IA
    * @param random Instance of the random class
    * @param shipsConfig The configuration of the ships
    * @param gridSize The size of the grid
    * @return A player configured randomly
    */
  def generateIA(index: Int, random: Random, shipsConfig: Map[String, Int], gridSize: Int): WeakIAPlayer = {
    val ships: Seq[Ship] = Generator.randomShips(shipsConfig, Seq[Ship](), random, gridSize)
    WeakIAPlayer(ships, "Weak IA " + index, Map[(Int, Int), Boolean](), Seq[(Int, Int)](), random, 0)
  }
}
