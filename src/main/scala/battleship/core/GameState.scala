package battleship.core

import battleship.core.models.Player

/**
  *
  * @param currentPlayer The current player of the game
  * @param opponent The opponent
  * @param numberOfGames The number of games to play during this session
  * @param gameCount The number of games played during this session
  */
case class GameState(currentPlayer: Player, opponent: Player, numberOfGames: Int, gameCount: Int) {

  /**
    * Check if there is a winner for the current game
    * @return An optional player if there is a winner, None otherwise
    */
  def isThereAWinner(): Option[Player] = {
    if (currentPlayer.numberOfShipsLeft() == 0) {
      return Option[Player](opponent)
    } else if (opponent.numberOfShipsLeft() == 0) {
      return Option[Player](currentPlayer)
    } else {
      return None
    }
  }


}

object GameState {

  def apply(currentPlayer: Player, opponent: Player, numberOfGames: Int, gameCount: Int): GameState = new GameState(currentPlayer, opponent, numberOfGames, gameCount)
}
