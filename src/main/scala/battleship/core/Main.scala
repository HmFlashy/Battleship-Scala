package battleship.core

import battleship.core.models._
import battleship.utils.io.{GameDisplay, PlayerDisplay, PlayerInputs}

import scala.annotation.tailrec
import scala.util.Random

object Main extends App {

  val randoms = Seq[Random](new Random(), new Random())
  if(GameConfig.gridSize > 10) {
    GameDisplay.gridTooBig()
    System.exit(1)
  }
  GameDisplay.choiceOfPlayers()
  val gameType = PlayerInputs.choiceOfPlayers()


  def initPlayers(gameType: Int, randoms: Seq[Random]): (PlayerTrait, PlayerTrait) = {
    gameType match {
      case 1 => {
        GameDisplay.choseYourName(1)
        val namePlayerOne: String = PlayerInputs.choseName()
        val playerOne: HumanPlayer = HumanPlayer.createPlayer(namePlayerOne, randoms(0))
        GameDisplay.choseYourName(2)
        val namePlayerTwo: String = PlayerInputs.choseName()
        val playerTwo: HumanPlayer = HumanPlayer.createPlayer(namePlayerTwo, randoms(0))
        (playerOne, playerTwo)
      }
      case 2 => {
        GameDisplay.choseYourName(1)
        val namePlayer: String = PlayerInputs.choseName()
        val player: HumanPlayer = HumanPlayer.createPlayer(namePlayer, randoms(0))
        val ia: WeakIAPlayer = WeakIAPlayer.generateIA(1, randoms(1))
        (player, ia)
      }
      case 3 => {
        val ia1: WeakIAPlayer = WeakIAPlayer.generateIA(1, randoms(0))
        val ia2: NormalIAPlayer = NormalIAPlayer.generateIA(2, randoms(1))
        (ia1, ia2)
      }
      case _ => (HumanPlayer.createPlayer("Player 1", randoms(0)), HumanPlayer.createPlayer("Player 2", randoms(1)))
    }
  }

  @tailrec
  def mainLoop(gameState: GameState, gameType: Int): Unit = {
    val currentPlayer = gameState.currentPlayer
    val opponent = gameState.opponent
    val winner = gameState.isThereAWinner()
    if(winner.isEmpty) {
      GameDisplay.clear()
      GameDisplay.opponentsTurn(currentPlayer.name)
      PlayerDisplay.show(currentPlayer, opponent)
      PlayerDisplay.shoot()
      val target: (Int, Int) = currentPlayer.shoot()
      val (newOpponent, touched, sank): (PlayerTrait, Boolean, Boolean) = opponent.receiveShoot(target)
      if(sank) PlayerDisplay.sank() else if(touched) PlayerDisplay.touched() else PlayerDisplay.notTouched()
      val newCurrentPlayer = currentPlayer.didShoot(target, didTouch = touched)
      mainLoop(GameState(newOpponent, newCurrentPlayer, gameState.numberOfGames), gameType)
    } else {
      if(gameState.numberOfGames < 100) {
        mainLoop(GameState(currentPlayer.reset(), winner.get.addVictory().reset() , gameState.numberOfGames + 1), gameType)
      } else {
        println(currentPlayer.name + " won " + currentPlayer.numberOfWins + " times.")
        println(opponent.name + " won " + opponent.numberOfWins + " times.")
      }
    }
  }

  val players = initPlayers(gameType, randoms)
  mainLoop(GameState(players._1, players._2, 0), gameType)
}
