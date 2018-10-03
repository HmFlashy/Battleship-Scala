package battleship.utils.io

import java.lang.Exception

import battleship.core.models.{Ship, ShipInformation}
import battleship.utils.display.PlayerDisplay

import scala.annotation.tailrec
import scala.io.StdIn

object PlayerInputs {

  @tailrec
  def choseName(): String = {
    val name = StdIn.readLine()
    val Pattern = """(^\w+$)""".r
    name match {
      case Pattern(c) => c
      case _ => choseName()
    }
  }

  def getShipInformation(shipConfig: (String, Int)): ShipInformation = {
    PlayerDisplay.setNewShip(shipConfig._1, shipConfig._2)
    val direction: String = PlayerInputs.getDirection()
    PlayerDisplay.getOriginShip(shipConfig._1, shipConfig._2)
    val point: (Int, Int) = PlayerInputs.getPoint()
    ShipInformation(direction, point)
  }

  @tailrec
  def getDirection(): String = {
    val direction = StdIn.readLine()
    direction match {
      case Ship.HORIZONTAL | Ship.VERTICAL => direction
      case _ => getDirection()
    }
  }

  def choiceOfPlayers() = {
    StdIn.readInt()
  }

  @tailrec
  def getPoint(): (Int, Int) = {
    val coordinates: String = StdIn.readLine()
    val Pattern = "([0-9] [0-9])".r
    coordinates match {
      case Pattern(p) => {
        val coordinatesList = coordinates.split(" ")
        (coordinatesList(0).toInt, coordinatesList(1).toInt)
      }
      case _ => getPoint()
    }
  }
}
