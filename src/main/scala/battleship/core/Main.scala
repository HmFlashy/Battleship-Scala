package battleship.core

import battleship.core.models.{HumanPlayer, PlayerTrait, Ship, ShipInformation}
import battleship.utils.display.{GameDisplay, PlayerDisplay}
import battleship.utils.io.PlayerInputs

import scala.annotation.tailrec
import scala.util.Random

object Main extends App {

  val initRandom = new Random()
  if(GameConfig.gridSize > 10) {
    GameDisplay.gridTooBig()
    System.exit(1)
  }
  def initPlayers(): (PlayerTrait, PlayerTrait) = {
    GameDisplay.choiceOfPlayers()
    val choiceOfPlayers = PlayerInputs.choiceOfPlayers()
    choiceOfPlayers match {
      case 1 => {
        GameDisplay.choseYourName(1)
        val namePlayerOne: String = PlayerInputs.choseName()
        GameDisplay.choseYourName(2)
        val namePlayerTwo: String = PlayerInputs.choseName()
        val playerOne: HumanPlayer = HumanPlayer.createPlayer(namePlayerOne)
        val playerTwo: HumanPlayer = HumanPlayer.createPlayer(namePlayerTwo)
        (playerOne, playerTwo)
      }
      case _ => (HumanPlayer.createPlayer("Player 1"), HumanPlayer.createPlayer("Player 2"))
    }
  }

  @tailrec
  def mainLoop(gameState: GameState, random: Random): Unit = {
    val winner = gameState.isThereAWinner()
    val currentPlayer = gameState.currentPlayer
    val opponent = gameState.opponent
    if(winner.isEmpty) {
      GameDisplay.clear()
      PlayerDisplay.show(currentPlayer, opponent)
      PlayerDisplay.shoot()
      val target: (Int, Int) = currentPlayer.shoot()
      val (newOpponent, touched, sank): (PlayerTrait, Boolean, Boolean) = opponent.receiveShoot(target)
      if(sank) PlayerDisplay.sank() else if(touched) PlayerDisplay.touched() else PlayerDisplay.notTouched()
      val newCurrentPlayer = currentPlayer.didShoot(target, didTouch = touched)
      GameDisplay.opponentsTurn(newCurrentPlayer.name)
      mainLoop(GameState(newOpponent, newCurrentPlayer, gameState.numberOfTurns + 1), random)
    } else {
      GameDisplay.gameIsOver(winner.get)
    }
  }

  val players = initPlayers()
  mainLoop(GameState(players._1, players._2, 0), initRandom)
}
