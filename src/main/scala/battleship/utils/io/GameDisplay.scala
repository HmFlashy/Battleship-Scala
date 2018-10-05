package battleship.utils.io

import battleship.core.models.PlayerTrait

object GameDisplay {
  def gameNumber(number: Int, oufOf: Int): Unit = {
    println("Game " + number + "/" + oufOf)
  }


  def opponentsTurn(name: String) = {
    println(name + " it is your turn -> Press enter to continue...")
  }

  def gridTooBig() = {
    println("The grid size is too big, change the game config and rebuild, exit...")
  }


  def choseYourName(playerNumber: Int)  = {
    println(s"Player ${playerNumber}, chose your name")
  }


  def choiceOfPlayers(): Unit = {
    println("""Choose the kind of game you want to play:
    1: Human vs Human
    2: Human vs WeakAI
    3: Human vs NormalAI
    4: Human vs StrongAI
    5: Test AIs vs AIs""")
  }


  def gameIsOver(player: PlayerTrait): Unit = {
    println(
      s"""
        |${player.name} won the game !!!
      """.stripMargin)
  }


  def Introduction(): Unit = {

  }

  def clear(): Unit = {
    print("\033[H\033[2J")
  }
}
