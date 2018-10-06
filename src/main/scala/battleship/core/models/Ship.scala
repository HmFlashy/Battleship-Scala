package battleship.core.models

import scala.annotation.tailrec

case class Ship(name: String, squares: Map[(Int, Int), Boolean]) {

  /**
    *
    * @return True if the ship is sunk false otherwise
    */
  def isSunk(): Boolean = {
    squares.forall(point => point._2)
  }

  /**
    *
    * @param shot The coordinates where the ship is hit
    * @return The ship modified with his slot hit updated to true
    */
  def hit(shot: (Int, Int)): Ship = {
    Ship(name, squares.updated(shot, true))
  }
}

object Ship {

  val HORIZONTAL = "H"
  val VERTICAL = "V"

  val CARRIER = "Carrier"
  val BATTLESHIP = "Battleship"
  val CRUISER = "Cruiser"
  val SUBMARINE = "Submarine"
  val DESTROYER = "Destroyer"


  /**
    * Use a tail recursion within the method
    *
    * @param name Name of the ship
    * @param direction Direction of the ship (Ship.HORIZONTAL or Ship.VERTICAl)
    * @param point The origin of the ship
    * @param shipsConfig The configuration of the ships
    * @return A ship configured
    */
  def convertInputsToShip(name: String, direction: String, point: (Int, Int), shipsConfig: Map[String, Int]): Ship = {

    @tailrec
    def convertInputsToShipTR(name: String, squareLeft: Int, direction: String, point: (Int, Int), squares: Map[(Int, Int), Boolean], shipsConfig: Map[String, Int]): Ship = {
      squareLeft match {
        case 0 => Ship(name, squares)
        case _ => {
          val newSquares = squares.+(point -> false)
          val newPoint = if (direction == Ship.HORIZONTAL) (point._1, point._2 + 1) else (point._1 + 1, point._2)
          convertInputsToShipTR(name, squareLeft - 1, direction, newPoint, newSquares, shipsConfig)
        }
      }
    }

    convertInputsToShipTR(name, shipsConfig(name), direction, point, Map[(Int, Int), Boolean](), shipsConfig)
  }

}
