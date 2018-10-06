package battleship.core.models

import battleship.utils.io.PlayerInputs
import battleship.utils.ships.Generator

import scala.collection.immutable.Seq
import scala.util.Random

case class HumanPlayer(ships: Seq[Ship], name: String, shots: Map[(Int, Int), Boolean], receivedShots: Seq[(Int, Int)], numberOfWins: Int, random: Random) extends Player {

  /**
    * Get the coordinates of the target
    *
    * @param gridSize Size of the grid to shoot
    * @return (Int, Int) A tuple that indicates where the player is shooting
    */
  override def shoot(gridSize: Int): (Int, Int) = {
    PlayerInputs.getPoint(gridSize)
  }

  /**
    *
    * @param shot Coordinate of the shot
    * @return (Player, Bookean) A tuple that contains the player that has been modified with the shot and a Boolean indicating if the shot hit one of the shi of the player or not
    */
  override def receiveShoot(shot: (Int, Int)): (HumanPlayer, Boolean, Option[Ship]) = {
    val shipShot: Option[Ship] = ships.find(ship => ship.squares.contains(shot))
    shipShot match {
      case Some(ship) => {
        val newShip: Ship = ship.hit(shot)
        val sunk: Boolean = newShip.isSunk()
        (HumanPlayer(ships.map { case oldShip if oldShip == ship => newShip; case x => x }, name, shots, receivedShots :+ shot, numberOfWins, random), true, if (sunk) Some(newShip) else None)
      }
      case None => (HumanPlayer(ships, name, shots, receivedShots :+ shot, numberOfWins, random), false, None)
    }
  }

  /**
    *
    * @param target Coordinates of the shot
    * @param didTouch Boolean that indicates if the shot did touch an opponent ship or not
    * @return The player modified
    */
  override def didShoot(target: (Int, Int), didTouch: Boolean): HumanPlayer = {
    HumanPlayer(ships, name, shots + (target -> didTouch), receivedShots, numberOfWins, random)
  }

  /**
    *
    * @return The player modified with a victory added
    */
  override def addVictory(): Player = {
    this.copy(numberOfWins = numberOfWins + 1)
  }

  /**
    *
    * @param shipsConfig The configuration of the ships
    * @param gridSize The size of the grid
    * @return The player reseted
    */
  override def reset(shipsConfig: Map[String, Int], gridSize: Int): HumanPlayer = {
    val newShips: Seq[Ship] = Generator.createShips(shipsConfig, Seq[Ship](), gridSize)
    this.copy(ships = newShips, shots = Map[(Int, Int), Boolean](), receivedShots = Seq[(Int, Int)]())
  }
}

object HumanPlayer {

  /**
    *
    * @param name The name of the player
    * @param random An instance of the random class
    * @param shipsConfig The config of the ships
    * @param gridSize The size of the grid
    * @return A new player configured manually
    */
  def createPlayer(name: String, random: Random, shipsConfig: Map[String, Int], gridSize: Int): HumanPlayer = {
    val ships = Generator.createShips(shipsConfig, Seq[Ship](), gridSize)
    new HumanPlayer(ships, name, shots = Map[(Int, Int), Boolean](), Seq[(Int, Int)](), 0, random)
  }

}