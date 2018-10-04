package battleship.core

import battleship.core.models._
import battleship.utils.io.PlayerDisplay
import battleship.utils.io.{GameDisplay, PlayerDisplay, PlayerInputs}

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
        val playerOne: HumanPlayer = HumanPlayer.createPlayer(namePlayerOne)
        GameDisplay.choseYourName(2)
        val namePlayerTwo: String = PlayerInputs.choseName()
        val playerTwo: HumanPlayer = HumanPlayer.createPlayer(namePlayerTwo)
        (playerOne, playerTwo)
      }
      case 2 => {
        GameDisplay.choseYourName(1)
        val namePlayer: String = PlayerInputs.choseName()
        val player: HumanPlayer = HumanPlayer.createPlayer(namePlayer)
        val ia: WeakIAPlayer = WeakIAPlayer.generateIA(1)
        (player, ia)
      }
      case 3 => {
        val ia1: WeakIAPlayer = WeakIAPlayer.generateIA(1)
        val ia2: WeakIAPlayer = WeakIAPlayer.generateIA(2)
        (ia1, ia2)
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
      val (currentPlayerShoot, target): (PlayerTrait,(Int, Int)) = currentPlayer.shoot()
      val (newOpponent, touched, sank): (PlayerTrait, Boolean, Boolean) = opponent.receiveShoot(target)
      if(sank) PlayerDisplay.sank() else if(touched) PlayerDisplay.touched() else PlayerDisplay.notTouched()
      val newCurrentPlayer = currentPlayerShoot.didShoot(target, didTouch = touched)
      GameDisplay.opponentsTurn(newCurrentPlayer.name)
      mainLoop(GameState(newOpponent, newCurrentPlayer, gameState.numberOfTurns + 1), random)
    } else {
      GameDisplay.gameIsOver(winner.get)
    }
  }

  val players = initPlayers()
  mainLoop(GameState(players._1, players._2, 0), initRandom)
}
