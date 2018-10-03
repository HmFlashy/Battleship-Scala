package battleship.utils.display

import battleship.core.GameState
import battleship.core.models.PlayerTrait


object GameDisplay extends Display[GameState] {

  def choiceOfPlayers(): Unit = {
    println("""Choose the kind of game you want to play:
    1: Human vs Human
    2: Human vs AI
    3: AI vs AI""")
  }


  def gameIsOver(player: PlayerTrait): Unit = {
    println(
      s"""
        |${player.name} won the game !!!
      """.stripMargin)
  }


  def Introduction(): Unit = {

  }

  def show(gameState: GameState) = {

  }
}
