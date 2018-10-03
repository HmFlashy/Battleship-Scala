package battleship.core.models

import battleship.utils.display.{GameDisplay, PlayerDisplay}

import scala.collection.immutable.Seq
import battleship.utils.io.PlayerInputs

import scala.annotation.tailrec

case class HumanPlayer(ships: Seq[Ship], name: String, shots: Map[(Int, Int),Boolean], receivedShots: Seq[(Int, Int)]) extends PlayerTrait {

  override def shoot(): (Int, Int) = {
    PlayerInputs.getPoint()
  }

  override def receiveShoot(shot: (Int, Int)): Option[HumanPlayer] = {
    val shipShot: Option[Ship] = ships.find(ship => ship.squares.contains(shot))
    shipShot match {
      case Some(ship) => {
        val newShip: Ship = ship.hit(shot)
        Some(HumanPlayer(ships.map { case oldShip if oldShip == ship => newShip; case x => x }, name, shots, receivedShots :+ shot))
      }
      case None => None
    }
  }

  override def didShoot(target: (Int, Int), didTouch: Boolean): HumanPlayer = {
    HumanPlayer(ships, name, shots + (target -> didTouch), receivedShots)
  }

}

object HumanPlayer {

  def createPlayer(name: String): HumanPlayer = {

    def isShipValid(ship: Ship): Boolean = {
      true
    }

    @tailrec
    def createShip(shipConfig: (String, Int)): Ship = {
      PlayerDisplay.setNewShip(shipConfig._1, shipConfig._2)
      val direction: String = PlayerInputs.getDirection()
      PlayerDisplay.getOriginShip(shipConfig._1, shipConfig._2)
      val point: (Int, Int) = PlayerInputs.getPoint()
      val ship = Ship.convertInputsToShip(shipConfig._1, direction, point)
      if(isShipValid(ship)) ship else createShip(shipConfig)
    }

    val ships = Ship.shipsConfig.map(shipConfig => {
      createShip(shipConfig)
    })
    new HumanPlayer(ships.to[collection.immutable.Seq], name, shots = Map[(Int, Int), Boolean](), Seq[(Int, Int)]())
  }
}
