package battleship.utils.io

import java.lang.Exception

import battleship.core.models.Ship

import scala.annotation.tailrec
import scala.io.StdIn

object PlayerInputs {

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
