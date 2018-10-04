package battleship.core.models

import battleship.core.GameConfig
import battleship.utils.display.{GameDisplay, GridDisplay, PlayerDisplay}

import scala.collection.immutable.Seq
import battleship.utils.io.PlayerInputs

import scala.annotation.tailrec

case class HumanPlayer(ships: Seq[Ship], name: String, shots: Map[(Int, Int),Boolean], receivedShots: Seq[(Int, Int)]) extends PlayerTrait {

  override def shoot(): (Int, Int) = {
    PlayerInputs.getPoint()
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
    def isOverlapping(ship: Ship, existingShips: Seq[Ship]): Boolean = {
      ship.
        squares.
        dropWhile(point => {
          val coordinates = point._1
          coordinates._1 < GameConfig.gridSize && coordinates._2 < GameConfig.gridSize && existingShips.dropWhile(ship => !ship.squares.contains(coordinates)).isEmpty
        })
        .nonEmpty
    }

    def isShipOut(): Boolean = {
      true
    }

    @tailrec
    def createShip(shipsConfig: Map[String, Int], existingShips: Seq[Ship]): Seq[Ship] = {
      val shipConfigOption = shipsConfig.headOption
      shipConfigOption match {
        case Some(shipConfig) => {
          GridDisplay.showPlayerGrid(existingShips, Seq[(Int, Int)]())

          val shipInfo: ShipInformation = PlayerInputs.getShipInformation(shipConfig)
          val ship = Ship.convertInputsToShip(shipConfig._1, shipInfo.direction, shipInfo.point)

          if (!isOverlapping(ship, existingShips)) {
            createShip(shipsConfig.tail, existingShips :+ ship)
          } else {
            PlayerDisplay.problemPlacingShip(ship)
            createShip(shipsConfig, existingShips)
          }
        }
        case None => {
          GridDisplay.showPlayerGrid(existingShips, Seq[(Int, Int)]())
          existingShips
        }
      }
    }

    val ships = createShip(GameConfig.shipsConfig, Seq[Ship]())
    new HumanPlayer(ships, name, shots = Map[(Int, Int), Boolean](), Seq[(Int, Int)]())
  }
}