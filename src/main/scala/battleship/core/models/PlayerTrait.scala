package battleship.core.models

import scala.annotation.tailrec
import scala.collection.immutable.Seq
import scala.util.Random

trait PlayerTrait {

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
  def receiveShoot(shot: (Int, Int)): (PlayerTrait, Boolean, Boolean)

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
        case Some(ship) => if(ship.isSank()) numberOfShipsLeftTR(ships.tail, number) else numberOfShipsLeftTR(ships.tail, number + 1)
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
  def didShoot(target: (Int, Int), didTouch: Boolean): PlayerTrait

  def addVictory(): PlayerTrait

  def reset(): PlayerTrait

}
