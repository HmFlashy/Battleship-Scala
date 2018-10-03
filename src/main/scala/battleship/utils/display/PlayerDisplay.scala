package battleship.utils.display

import battleship.core.models.{PlayerTrait, Ship}

object PlayerDisplay {

  def getOriginShip(nameShip: String, sizeShip: Int) = {
    println("What is the origin of your ship: x y")
  }


  def setNewShip(nameShip: String, sizeShip: Int) = {
    println("You have to place the " + nameShip + " -> which is composed of " + sizeShip + " units:\n" +
      "Will it be horizoontal ("+ Ship.HORIZONTAL + ") or vertical ("+Ship.VERTICAL +") ?")
  }


  def show(player: PlayerTrait): Unit = {

  }
}
