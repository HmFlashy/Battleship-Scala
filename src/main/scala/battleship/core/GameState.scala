package battleship.core

import battleship.core.models.PlayerTrait

case class GameState(currentPlayer: PlayerTrait, opponent: PlayerTrait, numberOfGames:Int, gameCount: Int) {

  def isThereAWinner(): Option[PlayerTrait] = {
    if(currentPlayer.numberOfShipsLeft() == 0){
      return Option[PlayerTrait](opponent)
    } else if(opponent.numberOfShipsLeft() == 0){
      return Option[PlayerTrait](currentPlayer)
    } else {
      return None
    }
  }


}

object GameState {

  def apply(currentPlayer: PlayerTrait, opponent: PlayerTrait, numberOfGames: Int, gameCount: Int): GameState = new GameState(currentPlayer, opponent, numberOfGames, gameCount)
}
