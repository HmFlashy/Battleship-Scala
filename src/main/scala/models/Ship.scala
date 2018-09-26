package battleship.models

case class Ship(length: Int, direction: Boolean, coordinates: Coordinates)

object Ship {
    def apply(length: Int, direction: Boolean, coordinates: Coordinates): Ship = new Ship(length, direction, coordinates)

}

