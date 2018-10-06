package battleship.utils.ships

import battleship.core.{GameConfig, models}
import battleship.core.models.{Ship, ShipInformation}
import battleship.utils.io.{GameDisplay, GridDisplay, PlayerDisplay, PlayerInputs}

import scala.annotation.tailrec
import scala.collection.immutable.Seq
import scala.util.Random

object Generator {


  @tailrec
  def createShips(shipsConfig: Map[String, Int], existingShips: Seq[Ship], gridSize: Int): Seq[Ship] = {

    val shipConfigOption = shipsConfig.headOption
    shipConfigOption match {
      case Some(shipConfig) => {
        GameDisplay.clear()
        GridDisplay.showPlayerGrid(existingShips, Seq[(Int, Int)](), gridSize)

        val shipInfo: ShipInformation = PlayerInputs.getShipInformation(shipConfig, gridSize)
        val ship = Ship.convertInputsToShip(shipConfig._1, shipInfo.direction, shipInfo.point, shipsConfig)

        if (!Validator.isOverlapping(ship, existingShips, gridSize)) {
          createShips(shipsConfig.tail, existingShips :+ ship, gridSize)
        } else {
          PlayerDisplay.problemPlacingShip(ship)
          createShips(shipsConfig, existingShips, gridSize)
        }
      }
      case None => {
        GridDisplay.showPlayerGrid(existingShips, Seq[(Int, Int)](), gridSize)
        existingShips
      }
    }
  }

  @tailrec
  def randomShips(shipsConfig: Map[String, Int], existingShips: Seq[Ship], random: Random, gridSize: Int): Seq[Ship] = {
    val shipConfigOption = shipsConfig.headOption
    shipConfigOption match {
      case Some(shipConfig) => {
        val direction = random.nextInt(2) match {
          case 0 => Ship.HORIZONTAL
          case 1 => Ship.VERTICAL
        }
        val shipInfo: ShipInformation = ShipInformation(direction, (random.nextInt(gridSize), random.nextInt(gridSize)))
        val ship = Ship.convertInputsToShip(shipConfig._1, shipInfo.direction, shipInfo.point, shipsConfig)

        if (!Validator.isOverlapping(ship, existingShips, gridSize)) {
          randomShips(shipsConfig.tail, existingShips :+ ship, random, gridSize)
        } else {
          randomShips(shipsConfig, existingShips, random, gridSize)
        }
      }
      case None => {
        existingShips
      }
    }
  }

}
