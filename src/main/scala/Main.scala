package battleship

import models.{ Coordinates, Ship }
import battleship.{ GameState }

import scala.annotation.tailrec

object Main extends App {
    var ship = Ship(5, true, Coordinates(0, 0))


    def mainLoop = (gameState: GameState) => {

    }

    mainLoop(GameState())
}