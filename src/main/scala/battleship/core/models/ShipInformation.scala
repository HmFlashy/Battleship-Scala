package battleship.core.models

/**
  * Class that describes the direction and origin of a ship
  *
  * @param direction Direction Ship.HORIZONTAL or SHIP.VERTICAL
  * @param point     Origin of the ship
  */
case class ShipInformation(direction: String, point: (Int, Int))
