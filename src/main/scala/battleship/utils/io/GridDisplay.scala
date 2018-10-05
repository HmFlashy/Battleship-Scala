package battleship.utils.io

import battleship.core.GameConfig
import battleship.core.models.Ship

import scala.annotation.tailrec

object GridDisplay {

  private val TOUCHED: String = Console.RED
  private val NOT_TOUCHED: String  = Console.BLACK
  private val WATER: String  = Console.BLUE
  private val MISSED: String  = Console.YELLOW
  private val BLOCK: String  = "███"
  private val BASE_COLOR: String  = Console.WHITE

  private def showGrid(grid: List[List[String]]): Unit = {

    println()
    @tailrec
    def printLineNumbers(actual: Int, n: Int): Unit ={
      actual match {
        case _ if actual < n => print(s" ${actual} "); printLineNumbers(actual + 1, n)
        case _ => { println() }
      }
    }
    printLineNumbers(0, GameConfig.gridSize)

    @tailrec
    def showGridTR(grid: List[List[String]], x: Int, y: Int): Unit = {
      if(x != GameConfig.gridSize){
        if(y != GameConfig.gridSize) {
          print(grid(x)(y))
          showGridTR(grid, x, y + 1)
        } else {
          print(s"${ BASE_COLOR } ${(x + 'A').toChar }") ; println()
          showGridTR(grid, x + 1, 0)
        }
      } else {
        print(BASE_COLOR)
      }
    }
    showGridTR(grid, 0, 0)
    println()
  }

  def showPlayerGrid(ships: Seq[Ship], shots: Seq[(Int, Int)]): Unit = {
    println(s"Here are your ships ->")

    var grid = Array.ofDim[String](GameConfig.gridSize, GameConfig.gridSize).toList.map((array) => Array.fill[String](GameConfig.gridSize)(WATER + BLOCK).toList)
    shots.foreach((shot) => {
      grid = grid.updated(shot._1, grid(shot._1).updated(shot._2, MISSED + BLOCK))
    })
    ships.foreach((ship) => {
      ship.squares.foreach(point => {
        val touched: Boolean = point._2
        if(touched) {
          grid = grid.updated(point._1._1, grid(point._1._1).updated(point._1._2, TOUCHED + BLOCK))
        } else {
          grid = grid.updated(point._1._1, grid(point._1._1).updated(point._1._2, NOT_TOUCHED + BLOCK))
        }
      })
    })
    showGrid(grid)
  }

  def showOpponentGrid(shots: Map[(Int, Int),Boolean]):Unit = {
    println(s"Here are your shots->")

    var grid = Array.ofDim[String](GameConfig.gridSize, GameConfig.gridSize).toList.map((array) => Array.fill[String](GameConfig.gridSize)(WATER + BLOCK).toList)
    shots.foreach((shot) => {
      grid = grid.updated(shot._1._1, grid(shot._1._1).updated(shot._1._2, if(shot._2) TOUCHED + BLOCK else NOT_TOUCHED + BLOCK))
    })
    showGrid(grid)
  }
}
