package battleship.utils.display

import battleship.core.models.{PlayerTrait, Ship}

object PlayerDisplay {
  def sank(): Unit = {
    println("SANK !!! Well done !")
  }


  def touched(): Unit = {
    println("HIT !")
  }

  def notTouched(): Unit = {
    println("Not hit, too bad...")
  }

  def shoot(): Unit = {
    println("Choose your target (example: A3) ->")
  }

  def problemPlacingShip(ship: Ship): Unit = {
    println(s"Problem placing the ${ship.name}")
  }


  def placeYourShips(namePlayer: String): Unit = {
    println(s"${namePlayer}")
  }

  def getOriginShip(nameShip: String, sizeShip: Int): Unit = {
    println("What is the origin of your ship (example: A3) ->")
  }


  def setNewShip(nameShip: String, sizeShip: Int): Unit  = {
    println("You have to place the " + nameShip + " -> which is composed of " + sizeShip + " units:\n" +
      "Will it be horizontal ("+ Ship.HORIZONTAL + ") or vertical ("+Ship.VERTICAL +") ?")
  }


  def show(player: PlayerTrait, opponent: PlayerTrait): Unit = {
    println(s"${player.name} it is your turn ->")
    println(s"Here are your ships ->")
    GridDisplay.showPlayerGrid(player.ships, opponent.shots.keys.toSeq)
    println(s"Here are your shots->")
    GridDisplay.showOpponentGrid(player.shots)
  }

}
