package battleship.core.models

import battleship.core.GameConfig

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
    *
    * @return
    */
  def shoot(): (Int, Int)

  /**
    *
    * @param shot
    * @return
    */
  def receiveShoot(shot: (Int, Int)): (Player, Boolean, Option[Ship])

  /**
    *
    * @return
    */
  def numberOfShipsLeft(): Int = {

    @tailrec
    def numberOfShipsLeftTR(ships: Seq[Ship], number: Int): Int = {
      val currentShip = ships.headOption
      currentShip match {
        case None => number
        case Some(ship) => if(ship.isSunk()) numberOfShipsLeftTR(ships.tail, number) else numberOfShipsLeftTR(ships.tail, number + 1)
      }
    }
    numberOfShipsLeftTR(ships, 0)
  }

  /**
    *
    * @param target
    * @param didTouch
    * @return
    */
  def didShoot(target: (Int, Int), didTouch: Boolean): Player

  def addVictory(): Player

  def reset(): Player

}

object Player {

  val LEFT = 1
  val UP = 2
  val RIGHT = 3
  val DOWN = 4

  def nextSlot(origin: (Int, Int), slot: (Int, Int), rayon: Int, direction: Int): ((Int, Int), Int, Boolean) = {
    slot match {
      case _ if origin._1 - rayon == slot._1 && origin._2 == slot._2 => {
        ((slot._1 - 1, slot._2 - 1), UP, true)
      }
      case _ => {
        direction match {
          case LEFT => {
            if (origin._1 - rayon == slot._1 && origin._2 + rayon == slot._2)
              ((slot._1, slot._2 - 1), UP, false)
            else
              ((slot._1 - 1, slot._2), LEFT, false)
          }
          case UP => {
            if (origin._1 - rayon == slot._1 && origin._2 - rayon == slot._2)
              ((slot._1 + 1, slot._2), RIGHT, false)
            else
              ((slot._1, slot._2 - 1), UP, false)
          }
          case RIGHT => {
            if (origin._1 + rayon == slot._1 && origin._2 - rayon == slot._2)
              ((slot._1, slot._2 + 1), DOWN, false)
            else
              ((slot._1 + 1, slot._2), RIGHT, false)
          }
          case _ => {
            if (origin._1 + rayon == slot._1 && origin._2 + rayon == slot._2)
              ((slot._1 - 1, slot._2), LEFT, false)
            else
              ((slot._1, slot._2 + 1), DOWN, false)
          }
        }
      }
    }
  }

  @tailrec
  def findClosestFreeSlot(origin: (Int, Int), shots: Map[(Int, Int), Boolean], rayon: Int, slot: (Int, Int), direction: Int): (Int, Int) = {
    if(
      slot._1 >= GameConfig.gridSize ||
        slot._1 < 0 ||
        slot._2 >= GameConfig.gridSize ||
        slot._2 < 0 ||
        shots.contains(slot)
    ){
      val newSlotAndDirection = nextSlot(origin, slot, rayon, direction)
      findClosestFreeSlot(origin, shots, if(newSlotAndDirection._3) rayon + 1 else rayon, newSlotAndDirection._1, newSlotAndDirection._2)
    } else {
      slot
    }
  }
}