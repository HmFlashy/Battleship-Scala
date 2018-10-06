import battleship.core.GameConfig
import battleship.core.models.{Player, WeakIAPlayer}
import org.scalatest.{FlatSpec, Matchers}

import scala.util.Random

class PlayerSpec extends  FlatSpec with Matchers {


  val gameConfig: GameConfig = GameConfig(GameConfig.DEFAULT)

  var newPlayer: Player = WeakIAPlayer.generateIA(1, new Random(), gameConfig.shipsConfig, gameConfig.gridSize)
  var opponent: Player = WeakIAPlayer.generateIA(2, new Random(), gameConfig.shipsConfig, gameConfig.gridSize)

  "Depending of this game configuration, a new player" should s"have ${gameConfig.shipsConfig.size} all his ships not sunk or touched" in {
    newPlayer.numberOfShipsLeft() should equal(gameConfig.shipsConfig.size)
    newPlayer.ships.flatMap(ship => ship.squares).filter(square => square._2) should have size 0
  }

  "A player that shot" should "have shot" in {
    newPlayer = newPlayer.didShoot((0, 0), true)
    newPlayer.shots should have size 1
  }

  "A player hit" should "have received a hit" in {
    val shot = opponent.receiveShoot(opponent.ships.head.squares.keys.toSeq.head)
    opponent = shot._1
    opponent.ships.flatMap(ship => ship.squares).filter(square => square._2) should have size 1
  }

  "A player who won" should "have one more victory" in {
    newPlayer = newPlayer.addVictory()
    newPlayer.numberOfWins should equal(1)
  }

  "A player that has been reseted" should "have a new healthy fleet" in {
    val playerReseted = newPlayer.reset(gameConfig.shipsConfig, gameConfig.gridSize)
    playerReseted.ships.flatMap(ship => ship.squares).filter(square => square._2) should have size 0
  }
  it should "have the same number of victories" in {
    val playerReseted = newPlayer.reset(gameConfig.shipsConfig, gameConfig.gridSize)
    playerReseted.numberOfWins should equal(newPlayer.numberOfWins)
  }


}
