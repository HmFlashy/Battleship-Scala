package battleship.utils.ships

import battleship.core.models.Ship

import scala.collection.immutable.Seq

object Validator {


  /**
    *
    * @param ship The ship to check
    * @param fleet The ships to compare to
    * @param gridSize The size of the grid
    * @return True if the ship to check is overlapping one of the ship of the fleet
    */
  def isOverlappingOrOut(ship: Ship, fleet: Seq[Ship], gridSize: Int): Boolean = {
    ship.
      squares.
      dropWhile(point => {
        val coordinates = point._1
        coordinates._1 < gridSize && coordinates._2 < gridSize && fleet.dropWhile(ship => !ship.squares.contains(coordinates)).isEmpty
      })
      .nonEmpty
  }

}
