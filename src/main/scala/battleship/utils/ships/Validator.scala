package battleship.utils.ships

import battleship.core.models.Ship

import scala.collection.immutable.Seq

object Validator {


  def isOverlapping(ship: Ship, fleet: Seq[Ship], gridSize: Int): Boolean = {
    ship.
      squares.
      dropWhile(point => {
        val coordinates = point._1
        coordinates._1 < gridSize && coordinates._2 < gridSize && fleet.dropWhile(ship => !ship.squares.contains(coordinates)).isEmpty
      })
      .nonEmpty
  }

}
