package battleship.utils.ships

import battleship.core.{GameConfig, models}
import battleship.core.models.{Ship, ShipInformation}
import battleship.utils.io.{GridDisplay, PlayerDisplay, PlayerInputs}

import scala.annotation.tailrec
import scala.collection.immutable.Seq
import scala.util.Random

object Generator {


  @tailrec
  def createShips(shipsConfig: Map[String, Int], existingShips: Seq[Ship]): Seq[Ship] = {
    val shipConfigOption = shipsConfig.headOption
    shipConfigOption match {
      case Some(shipConfig) => {
        GridDisplay.showPlayerGrid(existingShips, Seq[(Int, Int)]())

        val shipInfo: ShipInformation = PlayerInputs.getShipInformation(shipConfig)
        val ship = Ship.convertInputsToShip(shipConfig._1, shipInfo.direction, shipInfo.point)

        if (!Validator.isOverlapping(ship, existingShips, GameConfig.gridSize)) {
          createShips(shipsConfig.tail, existingShips :+ ship)
        } else {
          PlayerDisplay.problemPlacingShip(ship)
          createShips(shipsConfig, existingShips)
        }
      }
      case None => {
        GridDisplay.showPlayerGrid(existingShips, Seq[(Int, Int)]())
        existingShips
      }
    }
  }

  @tailrec
  def randomShips(shipsConfig: Map[String, Int], existingShips: Seq[Ship], random: Random): Seq[Ship] = {
    val shipConfigOption = shipsConfig.headOption
    shipConfigOption match {
      case Some(shipConfig) => {
        val direction = random.nextInt(2) match {
          case 0 => Ship.HORIZONTAL
          case 1 => Ship.VERTICAL
        }
        val shipInfo: ShipInformation = ShipInformation(direction, (random.nextInt(GameConfig.gridSize), random.nextInt(GameConfig.gridSize)))
        val ship = Ship.convertInputsToShip(shipConfig._1, shipInfo.direction, shipInfo.point)

        if (!Validator.isOverlapping(ship, existingShips, GameConfig.gridSize)) {
          randomShips(shipsConfig.tail, existingShips :+ ship, random)
        } else {
          randomShips(shipsConfig, existingShips, random)
        }
      }
      case None => {
        existingShips
      }
    }
  }

}
