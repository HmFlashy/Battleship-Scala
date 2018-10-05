package battleship.core

import java.io.{BufferedWriter, FileWriter}

import battleship.core.models._
import battleship.utils.io.{GameDisplay, GridDisplay, PlayerDisplay, PlayerInputs}

import scala.annotation.tailrec
import scala.util.Random

object Main extends App {

  if(GameConfig.gridSize > 10) {
    GameDisplay.gridTooBig()
    System.exit(1)
  }
  GameDisplay.choiceOfPlayers()
  val gameType = PlayerInputs.choiceOfPlayers()
  val randoms = if(gameType < 5) Seq[Random](new Random(), new Random()) else Seq[Random](new Random(), new Random(), new Random(), new Random(), new Random(), new Random())


  def initGameStates(gameType: Int, numberOfGames: Int, randoms: Seq[Random]): Set[GameState] = {
    gameType match {
      case 1 => {
        GameDisplay.choseYourName(1)
        val namePlayerOne: String = PlayerInputs.choseName()
        val playerOne: HumanPlayer = HumanPlayer.createPlayer(namePlayerOne, randoms(0))
        GameDisplay.choseYourName(2)
        val namePlayerTwo: String = PlayerInputs.choseName()
        val playerTwo: HumanPlayer = HumanPlayer.createPlayer(namePlayerTwo, randoms(0))
        Set(GameState(playerOne, playerTwo, numberOfGames, 1))
      }
      case 2 => {
        GameDisplay.choseYourName(1)
        val namePlayer: String = PlayerInputs.choseName()
        val player: HumanPlayer = HumanPlayer.createPlayer(namePlayer, randoms(0))
        val ia: WeakIAPlayer = WeakIAPlayer.generateIA(1, randoms(1))
        Set(GameState(player, ia, numberOfGames, 1))
      }
      case 3 => {
        GameDisplay.choseYourName(1)
        val namePlayer: String = PlayerInputs.choseName()
        val player: HumanPlayer = HumanPlayer.createPlayer(namePlayer, randoms(0))
        val ia: NormalIAPlayer = NormalIAPlayer.generateIA(1, randoms(1))
        Set(GameState(player, ia, numberOfGames, 1))
      }
      case 4 => {
        GameDisplay.choseYourName(1)
        val namePlayer: String = PlayerInputs.choseName()
        val player: HumanPlayer = HumanPlayer.createPlayer(namePlayer, randoms(0))
        val ia: StrongAIPlayer = StrongAIPlayer.generateIA(1, randoms(1))
        Set(GameState(player, ia, numberOfGames, 1))
      }
      case _ => {
        val ia1: WeakIAPlayer = WeakIAPlayer.generateIA(1, randoms(0))
        val ia2: NormalIAPlayer = NormalIAPlayer.generateIA(1, randoms(1))
        val ia3: NormalIAPlayer = NormalIAPlayer.generateIA(2, randoms(2))
        val ia4: StrongAIPlayer = StrongAIPlayer.generateIA(1, randoms(3))
        val ia5: WeakIAPlayer = WeakIAPlayer.generateIA(2, randoms(4))
        val ia6: StrongAIPlayer = StrongAIPlayer.generateIA(2, randoms(5))
        Set(GameState(ia1, ia2, numberOfGames, 1), GameState(ia3, ia4, numberOfGames, 1), GameState(ia5, ia6, numberOfGames, 1))
      }
    }
  }

  @tailrec
  def mainLoop(gameState: GameState): (Player, Player) = {
    val currentPlayer = gameState.currentPlayer
    val opponent = gameState.opponent
    val isCurrentPlayerHuman: Boolean = currentPlayer.isInstanceOf[HumanPlayer]
    val winner = gameState.isThereAWinner()
    if(winner.isEmpty) {
      if(isCurrentPlayerHuman) {
        GameDisplay.clear()
        PlayerDisplay.show(currentPlayer, opponent)
        GridDisplay.showPlayerGrid(currentPlayer.ships, opponent.shots.keys.toSeq)
        GridDisplay.showOpponentGrid(currentPlayer.shots)
        PlayerDisplay.shoot()
      }
      val target: (Int, Int) = currentPlayer.shoot()
      val (newOpponent, touched, shipSunk): (Player, Boolean, Option[Ship]) = opponent.receiveShoot(target)
      if(isCurrentPlayerHuman) {
        if(shipSunk.isDefined) PlayerDisplay.sunk(shipSunk.get.name) else if(touched) PlayerDisplay.touched() else PlayerDisplay.notTouched()
        GameDisplay.endOfTurn()
        PlayerInputs.pressAKey()
      }
      val newCurrentPlayer = currentPlayer.didShoot(target, didTouch = touched)
      mainLoop(GameState(newOpponent, newCurrentPlayer, gameState.numberOfGames, gameState.gameCount))
    } else {
      val continue: Boolean = if(currentPlayer.isInstanceOf[HumanPlayer] || opponent.isInstanceOf[HumanPlayer]){
        GameDisplay.continue()
        PlayerInputs.continue() != "q"
      } else {
        gameState.gameCount <= gameState.numberOfGames
      }
      val addedVictoryWinner = winner.get.addVictory()
      if(continue) {
        GameDisplay.clear()
        GameDisplay.gameNumber(gameState.gameCount, gameState.numberOfGames)
        mainLoop(GameState(currentPlayer.reset(), addedVictoryWinner.reset(), gameState.numberOfGames, gameState.gameCount + 1))
      } else {
        (currentPlayer, addedVictoryWinner)
      }
    }
  }
  val gameStates = initGameStates(gameType, 100, randoms)
  val results = gameStates.map(gameState => mainLoop(gameState))
  GameDisplay.end(results)
  val outputFile = new BufferedWriter(new FileWriter("results.csv"))
  val csvFields = "Player 1, Result player 1, Result player 2, Player 2\n" + results.map( result =>  s"${result._1.name}, ${result._1.numberOfWins.toString}, ${result._2.numberOfWins.toString}, ${result._2.name}\n").mkString
  outputFile.write(csvFields)
  outputFile.close()
}
