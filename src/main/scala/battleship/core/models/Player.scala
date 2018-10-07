package battleship.core.models

import scala.annotation.tailrec
import scala.collection.immutable.Seq
import scala.util.Random

trait Player {

  val name: String
  val ships: Seq[Ship]
  val shots: Map[(Int, Int), Boolean]
  val receivedShots: Seq[(Int, Int)]
  val numberOfWins: Int
  val random: Random

  /**
    * Get the coordinates of the target
    *
    * @param gridSize Size of the grid to shoot
    * @return (Int, Int) A tuple that indicates where the player is shooting
    */
  def shoot(gridSize: Int): (Int, Int)

  /**
    * The player receive a shot
    *
    * @param shot Coordinate of the shot
    * @return (Player, Bookean) A tuple that contains the player that has been modified with the shot and a Boolean indicating if the shot hit one of the shi of the player or not
    */
  def receiveShoot(shot: (Int, Int)): (Player, Boolean, Option[Ship])

  /**
    * Method the count the number of ships that are still standing in the player's fleet
    *
    * @return Int The number of ships still standing
    */
  def numberOfShipsLeft(): Int = {

    @tailrec
    def numberOfShipsLeftTR(ships: Seq[Ship], number: Int): Int = {
      val currentShip = ships.headOption
      currentShip match {
        case None => number
        case Some(ship) => if (ship.isSunk()) numberOfShipsLeftTR(ships.tail, number) else numberOfShipsLeftTR(ships.tail, number + 1)
      }
    }

    numberOfShipsLeftTR(ships, 0)
  }

  /**
    * Method that has to be implemented to indicate what to do with the results of the shot
    *
    * @param target Coordinated of the shot
    * @param didTouch Boolean that indicates if the shot did touch an opponent ship or not
    * @return The player modified
    */
  def didShoot(target: (Int, Int), didTouch: Boolean): Player

  /**
    * Method that has to be override to indicate what to do when a player win a game
    *
    * @return The player modified with a victory added
    */
  def addVictory(): Player

  /**
    * Reset the player ships
    *
    * @param shipsConfig The configuration of the ships
    * @param gridSize The size of the grid
    * @return The player reseted
    */
  def reset(shipsConfig: Map[String, Int], gridSize: Int): Player

}

object Player {

  val LEFT = 1
  val UP = 2
  val RIGHT = 3
  val DOWN = 4

  /**
    *
    * Find the next slot to process
    *
    * @param origin The origin of the shot
    * @param slot The actual slot to process
    * @param radius The actual radius between the origin and the slot
    * @param direction The current direction of the process
    * @return A tuple containing three objects: 1st one is the next slot to process, 2nd one is the next direction to process and the last one is if the radius is growing
    */
  def nextSlot(origin: (Int, Int), slot: (Int, Int), radius: Int, direction: Int): ((Int, Int), Int, Boolean) = {
    slot match {
      case _ if origin._1 - radius == slot._1 && origin._2 == slot._2 => {
        ((slot._1 - 1, slot._2 - 1), UP, true)
      }
      case _ => {
        direction match {
          case LEFT => {
            if (origin._1 - radius == slot._1 && origin._2 + radius == slot._2)
              ((slot._1, slot._2 - 1), UP, false)
            else
              ((slot._1 - 1, slot._2), LEFT, false)
          }
          case UP => {
            if (origin._1 - radius == slot._1 && origin._2 - radius == slot._2)
              ((slot._1 + 1, slot._2), RIGHT, false)
            else
              ((slot._1, slot._2 - 1), UP, false)
          }
          case RIGHT => {
            if (origin._1 + radius == slot._1 && origin._2 - radius == slot._2)
              ((slot._1, slot._2 + 1), DOWN, false)
            else
              ((slot._1 + 1, slot._2), RIGHT, false)
          }
          case _ => {
            if (origin._1 + radius == slot._1 && origin._2 + radius == slot._2)
              ((slot._1 - 1, slot._2), LEFT, false)
            else
              ((slot._1, slot._2 + 1), DOWN, false)
          }
        }
      }
    }
  }

  /**
    * FindClosestFreeSlot is finding the closest slot that the player did not shot from an origin point.
    * It is using a snail algorithm that allows to check the close slots first
    *
    * @param origin The origin point
    * @param shots The shots of the player
    * @param radius The current radius
    * @param slot The current slot that is processed
    * @param direction The current direction of the algorithm
    * @param gridSize The size of the grid
    * @return The closest slot not shot
    */
  @tailrec
  def findClosestFreeSlot(origin: (Int, Int), shots: Map[(Int, Int), Boolean], radius: Int, slot: (Int, Int), direction: Int, gridSize: Int): (Int, Int) = {
    if (
      slot._1 >= gridSize ||
        slot._1 < 0 ||
        slot._2 >= gridSize ||
        slot._2 < 0 ||
        shots.contains(slot)
    ) {
      val newSlotAndDirection = nextSlot(origin, slot, radius, direction)
      findClosestFreeSlot(origin, shots, if (newSlotAndDirection._3) radius + 1 else radius, newSlotAndDirection._1, newSlotAndDirection._2, gridSize)
    } else {
      slot
    }
  }
}