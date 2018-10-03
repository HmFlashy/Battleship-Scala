package battleship.core

import battleship.core.models.PlayerTrait

case class GameState(currentPlayer: PlayerTrait, opponent: PlayerTrait, numberOfTurns: Int) {

  def isThereAWinner(): Option[PlayerTrait] = {
    if(currentPlayer.numberOfShipsLeft() == 0){
      return Option[PlayerTrait](currentPlayer)
    } else if(opponent.numberOfShipsLeft() == 0){
      return Option[PlayerTrait](opponent)
    } else {
      return None
    }
  }


}

object GameState {

  def apply(currentPlayer: PlayerTrait, opponent: PlayerTrait, numberOfTurns: Int): GameState = new GameState(currentPlayer, opponent, numberOfTurns)
}
