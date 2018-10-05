package battleship.core

import battleship.core.models.Ship._

import scala.collection.immutable.ListMap

object GameConfig {

  val shipsConfig: Map[String, Int] = ListMap(Map(
    DESTROYER -> 2
  ).toSeq.sortBy(- _._2):_*)

  val gridSize: Int = 3
}
