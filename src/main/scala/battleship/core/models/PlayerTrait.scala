package battleship.core.models

import scala.annotation.tailrec

trait PlayerTrait {

  val name: String
  val ships: Seq[Ship]
  val shots: Map[(Int, Int), Boolean]
  val receivedShots: Seq[(Int, Int)]

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
  def receiveShoot(shot: (Int, Int)): (PlayerTrait, Boolean)

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
        case Some(ship) => if(ship.isSink) numberOfShipsLeftTR(ships.tail, number) else numberOfShipsLeftTR(ships.tail, number + 1)
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

}
