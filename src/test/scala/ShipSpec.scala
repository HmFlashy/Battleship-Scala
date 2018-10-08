package battleship

import battleship.core.GameConfig
import org.scalatest._
import battleship.core.models.Ship

class ShipSpec extends FlatSpec with Matchers {

  val gameConfig: GameConfig = GameConfig(GameConfig.DEFAULT)
  info("Taking the default battleship configuration")
  "Depending on this configuration a new ship" should "be alive" in {
    val ship: Ship = Ship.convertInputsToShip(Ship.DESTROYER, Ship.HORIZONTAL, (0, 0), gameConfig.shipsConfig)
    ship.isSunk() shouldBe false
  }

  gameConfig.shipsConfig.foreach(ship => {
    "A " + ship._1 should s"have ${ship._2} slots" in {
      val newShip: Ship = Ship.convertInputsToShip(ship._1, Ship.HORIZONTAL, (0, 0), gameConfig.shipsConfig)
      newShip.squares should have size ship._2
    }
  })

  "A new ship that has been hit" should "have one slot hit" in {
    val newShip: Ship = Ship.convertInputsToShip(Ship.DESTROYER, Ship.HORIZONTAL, (0, 0), gameConfig.shipsConfig)
    val hitShip = newShip.hit((0, 0))
    hitShip.squares.count(square => square._2) == 1
  }

  "A new ship that has been sunk" should "have all his slots hit" in {
    val newShip: Ship = Ship.convertInputsToShip(Ship.DESTROYER, Ship.HORIZONTAL, (0, 0), gameConfig.shipsConfig)
    val hitShip = newShip.hit((0, 0)).hit((1, 0))
    hitShip.squares.count(square => square._2) == gameConfig.shipsConfig(Ship.DESTROYER)
  }

  "A ship that is receiving wrong coordinates" should "not be changed" in {
    val newShip: Ship = Ship.convertInputsToShip(Ship.DESTROYER, Ship.HORIZONTAL, (0, 0), gameConfig.shipsConfig)
    val hitShip = newShip.hit((1, 0))
    hitShip.squares.count(square => square._2) == 0
  }
}
