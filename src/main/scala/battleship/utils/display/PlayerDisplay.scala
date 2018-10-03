package battleship.utils.display

import battleship.core.models.{PlayerTrait, Ship}

object PlayerDisplay {

  def touched() = {
    println("HIT !")
  }

  def notTouched(): Unit = {
    println("Not hit, too bad...")
  }

  def shoot() = {
    println("Choose your target: x y")
  }

  def problemPlacingShip(ship: Ship) = {
    println(s"Problem placing the ${ship.name}")
  }


  def placeYourShips(namePlayer: String) = {
    println(s"${namePlayer}")
  }

  def getOriginShip(nameShip: String, sizeShip: Int) = {
    println("What is the origin of your ship: x y")
  }


  def setNewShip(nameShip: String, sizeShip: Int) = {
    println("You have to place the " + nameShip + " -> which is composed of " + sizeShip + " units:\n" +
      "Will it be horizoontal ("+ Ship.HORIZONTAL + ") or vertical ("+Ship.VERTICAL +") ?")
  }


  def show(player: PlayerTrait): Unit = {
    println(s"${player.name} it is your turn ->")
    GridDisplay.showPlayerGrid(player.ships, player.shots.keys.toSeq)
  }

  case class Touched()

}
