package battleship

import org.scalatest._
import battleship.core.models.Ship

class ShipSpec extends FlatSpec with Matchers {
  "A new ship" should "be alive" in {
    val ship: Ship = Ship.convertInputsToShip(Ship.DESTROYER, Ship.HORIZONTAL, (0, 0))
    ship.isSank() shouldBe false
  }
  "A " + Ship.CARRIER should "have 5 units" in {
    val ship: Ship = Ship.convertInputsToShip(Ship.CARRIER, Ship.HORIZONTAL, (0, 0))
    ship.squares should have size 5
  }
  "A " + Ship.BATTLESHIP  should "have 4 units" in {
    val ship: Ship = Ship.convertInputsToShip(Ship.BATTLESHIP, Ship.HORIZONTAL, (0, 0))
    ship.squares should have size 4
  }
  "A " + Ship.CRUISER should "have 3 units" in {
    val ship: Ship = Ship.convertInputsToShip(Ship.CRUISER, Ship.HORIZONTAL, (0, 0))
    ship.squares should have size 3
  }
  "A " + Ship.SUBMARINE should "have 3 units" in {
    val ship: Ship = Ship.convertInputsToShip(Ship.SUBMARINE, Ship.HORIZONTAL, (0, 0))
    ship.squares should have size 3
  }
  "A " + Ship.DESTROYER should "have 2 units" in {
    val ship: Ship = Ship.convertInputsToShip(Ship.DESTROYER, Ship.HORIZONTAL, (0, 0))
    ship.squares should have size 2
  }

  s"A ${Ship.DESTROYER} that has been "
}
