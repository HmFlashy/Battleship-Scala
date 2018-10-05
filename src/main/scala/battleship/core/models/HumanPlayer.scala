package battleship.core.models

import battleship.core.GameConfig

import scala.collection.immutable.Seq
import battleship.utils.io.{PlayerDisplay, PlayerInputs}
import battleship.utils.ships.{Generator, Validator}

import scala.util.Random

case class HumanPlayer(ships: Seq[Ship], name: String, shots: Map[(Int, Int),Boolean], receivedShots: Seq[(Int, Int)], numberOfWins: Int, random: Random) extends Player {

  override def shoot(): (Int, Int) = {
    PlayerInputs.getPoint()
  }

  override def receiveShoot(shot: (Int, Int)): (HumanPlayer, Boolean, Option[Ship]) = {
    val shipShot: Option[Ship] = ships.find(ship => ship.squares.contains(shot))
    shipShot match {
      case Some(ship) => {
        val newShip: Ship = ship.hit(shot)
        val sunk: Boolean = newShip.isSunk()
        (HumanPlayer(ships.map { case oldShip if oldShip == ship => newShip; case x => x }, name, shots, receivedShots :+ shot, numberOfWins, random), true, if(sunk) Some(newShip) else None)
      }
      case None => (HumanPlayer(ships, name, shots, receivedShots :+ shot, numberOfWins, random), false, None)
    }
  }

  override def didShoot(target: (Int, Int), didTouch: Boolean): HumanPlayer = {
    HumanPlayer(ships, name, shots + (target -> didTouch), receivedShots, numberOfWins, random)
  }

  override def addVictory(): Player = {
    this.copy(numberOfWins = numberOfWins + 1)
  }

  override def reset(): HumanPlayer = {
    val newShips: Seq[Ship] = Generator.createShips(GameConfig.shipsConfig, Seq[Ship]())
    this.copy(ships = newShips, shots = Map[(Int, Int), Boolean](), receivedShots = Seq[(Int, Int)]())
  }
}

object HumanPlayer {

  def createPlayer(name: String, random: Random): HumanPlayer = {
    PlayerDisplay.placeYourShips(name)
    val ships = Generator.createShips(GameConfig.shipsConfig, Seq[Ship]())
    new HumanPlayer(ships, name, shots = Map[(Int, Int), Boolean](), Seq[(Int, Int)](), 0, random)
  }

}