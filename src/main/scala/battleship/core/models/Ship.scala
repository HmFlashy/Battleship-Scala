package battleship.core.models

import battleship.core.GameConfig

import scala.annotation.tailrec
case class Ship(name: String, squares: Map[(Int, Int), Boolean]){

  /**
    *
    * @return
    */
  def isSunk(): Boolean = {
    squares.forall(point => point._2)
  }

  /**
    *
    * @param x
    * @param y
    * @return
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
    *
    * @param name
    * @param isHorizontal
    * @param point
    * @return
    */
  def convertInputsToShip(name: String, direction: String, point: (Int, Int), shipsConfig: Map[String, Int]): Ship = {
    /**
      *
      * @param squareLeft
      * @param isHorizontal
      * @param point
      * @param squares
      * @return
      */
    @tailrec
    def convertInputsToShipTR(name: String, squareLeft: Int, direction: String, point: (Int, Int), squares: Map[(Int, Int), Boolean], shipsConfig: Map[String, Int]): Ship = {
      squareLeft match {
        case 0 => Ship(name, squares)
        case _ => {
          val newSquares = squares.+(point -> false)
          val newPoint = if(direction == Ship.HORIZONTAL) (point._1, point._2 + 1) else (point._1 + 1, point._2)
          convertInputsToShipTR(name, squareLeft - 1, direction, newPoint, newSquares, shipsConfig)
        }
      }
    }
    convertInputsToShipTR(name, shipsConfig(name), direction, point, Map[(Int, Int), Boolean](), shipsConfig)
  }

}
