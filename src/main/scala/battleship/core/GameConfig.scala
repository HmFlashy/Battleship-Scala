package battleship.core

import battleship.core.models.Ship
import battleship.core.models.Ship._

import scala.collection.immutable.ListMap

case class GameConfig(shipsConfig: Map[String, Int], gridSize: Int) {

  private val configIsCorrect = shipsConfig.map(shipConfig => {
    val nameShip = shipConfig._1
    shipConfig._2 < gridSize && (nameShip == Ship.DESTROYER || nameShip == Ship.BATTLESHIP || nameShip == Ship.CARRIER || nameShip == Ship.SUBMARINE || nameShip == Ship.CRUISER)
  }).size == shipsConfig.size

  if(!configIsCorrect){
    throw new IllegalArgumentException("The ships configuration is not correct")
  }
}

object GameConfig {

  val DEFAULT: Int = 1
  val CUSTOM: Int = 2


  /**
    * The configuration of the ships of the game
    */
  private val DEFAULT_SHIP_CONFIG: Map[String, Int] = ListMap(Map(
    CARRIER -> 5,
    BATTLESHIP -> 4,
    CRUISER -> 3,
    SUBMARINE -> 3,
    DESTROYER -> 2
  ).toSeq.sortBy(-_._2): _*)

  /**
    * The size of the grid
    */
  private val DEFAULT_GRID_SIZE: Int = 10

  def apply(typeConfig: Int): GameConfig = {
    typeConfig match {
      case DEFAULT => new GameConfig(DEFAULT_SHIP_CONFIG, DEFAULT_GRID_SIZE)
      case _ => new GameConfig(DEFAULT_SHIP_CONFIG, DEFAULT_GRID_SIZE)
    }
  }
}
