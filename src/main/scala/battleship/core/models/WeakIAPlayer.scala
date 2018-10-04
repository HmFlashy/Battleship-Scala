package battleship.core.models

import scala.collection.immutable.Seq

case class WeakIAPlayer(ships: Seq[Ship], name: String, shots: Map[(Int, Int), Boolean], receivedShots: Seq[(Int, Int)]) extends PlayerTrait {

  /**
    *
    * @return
    */
  override def shoot(): (Int, Int) = ???

  /**
    *
    * @param shot
    * @return
    */
  override def receiveShoot(shot: (Int, Int)): (PlayerTrait, Boolean, Boolean) = ???

  /**
    *
    * @param target
    * @param didTouch
    * @return
    */
  override def didShoot(target: (Int, Int), didTouch: Boolean): PlayerTrait = ???
}
