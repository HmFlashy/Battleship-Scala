package battleship.utils.io

import battleship.core.models.PlayerTrait

object GameDisplay {

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

  def clear(): Unit = {
    print("\u001b[2J")
  }
}
