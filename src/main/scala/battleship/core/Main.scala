package battleship.core

import battleship.core.models.{HumanPlayer, PlayerTrait, Ship}
import battleship.utils.display.{GameDisplay, PlayerDisplay}
import battleship.utils.io.PlayerInputs

import scala.annotation.tailrec
import scala.util.Random

object Main extends App {

  var initRandom = new Random()

  def initPlayers(): (PlayerTrait, PlayerTrait) = {
    GameDisplay.choiceOfPlayers()
    val choiceOfPlayers = PlayerInputs.choiceOfPlayers()
    choiceOfPlayers match {
      case 1 => (HumanPlayer.createPlayer("Player 1"), HumanPlayer.createPlayer("Player 2"))
      case _ => (HumanPlayer.createPlayer("Player 1"), HumanPlayer.createPlayer("Player 2"))
    }
  }

  @tailrec
  def mainLoop(gameState: GameState, random: Random): Unit = {
    val winner = gameState.isThereAWinner()
    val currentPlayer = gameState.currentPlayer
    var opponent = gameState.opponent
    if(winner.isEmpty) {
      PlayerDisplay.show(currentPlayer)
      val target: (Int, Int) =currentPlayer.shoot()
      val playerShot: Option[PlayerTrait] = opponent.receiveShoot(target)
      if(!playerShot.isEmpty){
        opponent = playerShot.get
        currentPlayer.didShoot(target, didTouch = false)
      } else {
        currentPlayer.didShoot(target, didTouch = true)
      }
      mainLoop(GameState(opponent, currentPlayer, gameState.numberOfTurns + 1), random)

    } else {
      GameDisplay.gameIsOver(winner.get)
    }
  }

  val players = initPlayers()
  mainLoop(GameState(players._1, players._2, 0), initRandom)
}
