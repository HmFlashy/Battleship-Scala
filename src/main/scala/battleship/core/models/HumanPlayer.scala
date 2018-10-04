package battleship.core.models

import battleship.core.GameConfig

import scala.collection.immutable.Seq
import battleship.utils.io.{PlayerDisplay, PlayerInputs}
import battleship.utils.ships.{Generator, Validator}

case class HumanPlayer(ships: Seq[Ship], name: String, shots: Map[(Int, Int),Boolean], receivedShots: Seq[(Int, Int)]) extends PlayerTrait {

  override def shoot(): (PlayerTrait, (Int, Int)) = {
    (this, PlayerInputs.getPoint())
  }

  override def receiveShoot(shot: (Int, Int)): (HumanPlayer, Boolean, Boolean) = {
    val shipShot: Option[Ship] = ships.find(ship => ship.squares.contains(shot))
    shipShot match {
      case Some(ship) => {
        val newShip: Ship = ship.hit(shot)
        val sank: Boolean = newShip.isSank()
        (HumanPlayer(ships.map { case oldShip if oldShip == ship => newShip; case x => x }, name, shots, receivedShots :+ shot), true, sank)
      }
      case None => (HumanPlayer(ships, name, shots, receivedShots :+ shot), false, false)
    }
  }

  override def didShoot(target: (Int, Int), didTouch: Boolean): HumanPlayer = {
    HumanPlayer(ships, name, shots + (target -> didTouch), receivedShots)
  }

}

object HumanPlayer {

  def createPlayer(name: String): HumanPlayer = {
    PlayerDisplay.placeYourShips(name)
    val ships = Generator.createShips(GameConfig.shipsConfig, Seq[Ship]())
    new HumanPlayer(ships, name, shots = Map[(Int, Int), Boolean](), Seq[(Int, Int)]())
  }
}