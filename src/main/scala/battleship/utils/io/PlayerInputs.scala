package battleship.utils.io

import java.lang.Exception

import battleship.core.GameConfig
import battleship.core.models.{Ship, ShipInformation}
import scala.annotation.tailrec
import scala.io.StdIn

object PlayerInputs {

  def continue() = {
    StdIn.readLine()
  }


  def pressAKey() = {
    StdIn.readLine()
  }

  @tailrec
  def choseName(): String = {
    val name = StdIn.readLine()
    val Pattern = """(^\w+$)""".r
    name match {
      case Pattern(c) => c
      case _ => choseName()
    }
  }

  def getShipInformation(shipConfig: (String, Int), gridSize: Int): ShipInformation = {
    PlayerDisplay.setNewShip(shipConfig._1, shipConfig._2)
    val direction: String = PlayerInputs.getDirection()
    PlayerDisplay.getOriginShip(shipConfig._1, shipConfig._2)
    val point: (Int, Int) = PlayerInputs.getPoint(gridSize)
    ShipInformation(direction, point)
  }

  @tailrec
  def getDirection(): String = {
    val direction = StdIn.readLine().toUpperCase
    direction match {
      case Ship.HORIZONTAL | Ship.VERTICAL => direction
      case _ => getDirection()
    }
  }

  def choiceOfPlayers(): Int = {
    val choice = StdIn.readLine()
    val Pattern = "(^[1-5]$)".r
    choice match {
      case Pattern(p) => {
        p.toInt
      }
      case _ => choiceOfPlayers()
    }
  }

  @tailrec
  def getPoint(gridSize: Int): (Int, Int) = {
    val coordinates: String = StdIn.readLine()
    val Pattern = ("(^[a-zA-Z][0-"+(gridSize-1)+"]$)").r
    coordinates match {
      case Pattern(p) => {
        val line = p(0).toUpper.toInt - 'A'
        if(line - gridSize < 0)
          (line, p(1) - '0'.toInt)
        else
          getPoint(gridSize)
      }
      case _ => getPoint(gridSize)
    }
  }
}
