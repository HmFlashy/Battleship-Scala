package battleship.core.models

import scala.annotation.tailrec
import scala.collection.immutable.ListMap

case class Ship(name: String, squares: Map[(Int, Int), Boolean]){

  /**
    *
    * @return
    */
  def isSink(): Boolean = {
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
  val HORIZONTAL = "1"
  val VERTICAL = "2"

  val CARRIER = "Carrier"
  val BATTLESHIP = "Battleship"
  val CRUISER = "Cruiser"
  val SUBMARINE = "Submarine"
  val DESTROYER = "Destroyer"

  val shipsConfig: Map[String, Int] = ListMap(Map(
    CARRIER -> 5,
    BATTLESHIP -> 4,
    CRUISER -> 3,
    SUBMARINE -> 3,
    DESTROYER -> 2
  ).toSeq.sortBy(_._2):_*)

  /**
    *
    * @param name
    * @param isHorizontal
    * @param point
    * @return
    */
  def convertInputsToShip(name: String, direction: String, point: (Int, Int)): Ship = {

    /**
      *
      * @param squareLeft
      * @param isHorizontal
      * @param point
      * @param squares
      * @return
      */
    @tailrec
    def convertInputsToShipTR(name: String, squareLeft: Int, direction: String, point: (Int, Int), squares: Map[(Int, Int), Boolean]): Ship = {
      squareLeft match {
        case 0 => Ship(name, squares)
        case _ => {
          val newSquares = squares.+(point -> false)
          val newPoint = if(direction == Ship.HORIZONTAL) (point._1 + 1, point._2) else (point._1, point._2 + 1)
          convertInputsToShipTR(name, squareLeft - 1, direction, newPoint, newSquares)
        }
      }
    }
    convertInputsToShipTR(name, shipsConfig(name), direction, point, Map[(Int, Int), Boolean]())
  }

}
