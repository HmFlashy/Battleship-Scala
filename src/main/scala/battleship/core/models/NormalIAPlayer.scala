package battleship.core.models
import battleship.core.GameConfig
import battleship.utils.ships.Generator

import scala.annotation.tailrec
import scala.collection.immutable.Seq
import scala.io.StdIn
import scala.util.Random

case class NormalIAPlayer(ships: Seq[Ship], name: String, shots: Map[(Int, Int), Boolean], receivedShots: Seq[(Int, Int)], random: Random, numberOfWins: Int) extends PlayerTrait {

  /**
    *
    * @return
    */
  override def shoot(): (Int, Int) = {
    val point = (random.nextInt(GameConfig.gridSize), random.nextInt(GameConfig.gridSize))

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
    findClosestFreeSlot(point, shots, 0, point, UP)
  }

  /**
    *
    * @param shot
    * @return
    */
  override def receiveShoot(shot: (Int, Int)): (NormalIAPlayer, Boolean, Boolean) = {
    val shipShot: Option[Ship] = ships.find(ship => ship.squares.contains(shot))
    shipShot match {
      case Some(ship) => {
        val newShip: Ship = ship.hit(shot)
        val sank: Boolean = newShip.isSank()
        (this.copy( ships = ships.map { case oldShip if oldShip == ship => newShip; case x => x }, receivedShots = receivedShots :+ shot), true, sank)
      }
      case None => (this.copy(receivedShots = receivedShots :+ shot), false, false)
    }
  }

  /**
    *
    * @param target
    * @param didTouch
    * @return
    */
  override def didShoot(target: (Int, Int), didTouch: Boolean): NormalIAPlayer = {
      this.copy(shots = shots + (target -> didTouch))
    }

  override def addVictory(): NormalIAPlayer = {
    this.copy(numberOfWins = numberOfWins + 1)
  }

  override def reset(): NormalIAPlayer = {
    val newShips: Seq[Ship] = Generator.randomShips(GameConfig.shipsConfig, Seq[Ship](), this.random)
    this.copy(ships = newShips, shots = Map[(Int, Int), Boolean](), receivedShots = Seq[(Int, Int)]())
  }
}

object NormalIAPlayer {
  def generateIA(index: Int, random: Random): NormalIAPlayer = {
    val ships: Seq[Ship] = Generator.randomShips(GameConfig.shipsConfig, Seq[Ship](), random)
    NormalIAPlayer(ships, "Normal IA "+index, Map[(Int, Int), Boolean](), Seq[(Int, Int)](), random, 0)
  }
}
