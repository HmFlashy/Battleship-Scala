package battleship.core

import battleship.core.models.Player

case class GameState(currentPlayer: Player, opponent: Player, numberOfGames: Int, gameCount: Int) {

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
