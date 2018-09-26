package battleship.models

case class Coordinates(x: Int, y: Int)

object Coordinates {
    def apply(x: Int, y: Int) = new Coordinates(x, y)
}