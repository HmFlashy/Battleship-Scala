package battleship.utils.io

import battleship.core.models.Player

object GameDisplay {
  def winner(name: String) = {
    println(name + " won this game, good job.")
    println()
  }

  def continue() = {
    println("Press a key to play a new game (q to quit)")
  }


  def end(results: Set[(Player, Player)]): Unit = {
    println()
    results.zipWithIndex.foreach(resultZip => {
      val game = resultZip._1
      val index = resultZip._2
      println("Fight " + (index + 1) + s": ${game._1.name}  VS ${game._2.name}".toUpperCase())
      println()
      println(s"${game._1.name} won ${game._1.numberOfWins} time${if (game._1.numberOfWins > 1) 's' else ""} ")
      println(s"${game._2.name} won ${game._2.numberOfWins} time${if (game._2.numberOfWins > 1) 's' else ""} ")
      println()
      println()
    })
    println("You can see the results in the results.csv file.\n")
  }


  def endOfTurn() = {
    println(s"End of your turn, press enter to continue...")
  }


  def gameNumber(number: Int, oufOf: Int): Unit = {
    println("Game " + number + "/" + oufOf)
  }


  def opponentsTurn(name: String) = {
    println(name + " it is your turn -> Press enter to continue...")
  }

  def gridTooBig() = {
    println("The grid size is too big, change the game config and rebuild, exit...")
  }


  def choseYourName(playerNumber: Int) = {
    println(s"Player ${playerNumber}, chose your name")
  }


  def choiceOfPlayers(): Unit = {
    println(
      """Choose the kind of game you want to play:
    1: Human vs Human
    2: Human vs WeakAI
    3: Human vs NormalAI
    4: Human vs StrongAI
    5: Test AIs vs AIs""")
  }


  def gameIsOver(player: Player): Unit = {
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
