package battleship.utils.io

import battleship.core.models.Ship

import scala.annotation.tailrec

object GridDisplay {

  private val TOUCHED: String = Console.RED
  private val NOT_TOUCHED: String = Console.BLACK
  private val WATER: String = Console.BLUE
  private val MISSED: String = Console.YELLOW
  private val BLOCK: String = "███"
  private val BASE_COLOR: String = Console.WHITE

  private def showGrid(grid: List[List[String]], gridSize: Int): Unit = {

    println()

    @tailrec
    def printLineNumbers(actual: Int, n: Int): Unit = {
      actual match {
        case _ if actual < n => print(s" ${actual} "); printLineNumbers(actual + 1, n)
        case _ => {
          println()
        }
      }
    }

    printLineNumbers(0, gridSize)

    @tailrec
    def showGridTR(grid: List[List[String]], x: Int, y: Int, gridSize: Int): Unit = {
      if (x != gridSize) {
        if (y != gridSize) {
          print(grid(x)(y))
          showGridTR(grid, x, y + 1, gridSize)
        } else {
          print(s"${BASE_COLOR} ${(x + 'A').toChar}");
          println()
          showGridTR(grid, x + 1, 0, gridSize)
        }
      } else {
        print(BASE_COLOR)
      }
    }

    showGridTR(grid, 0, 0, gridSize)
    println()
  }

  def showPlayerGrid(ships: Seq[Ship], shots: Seq[(Int, Int)], gridSize: Int): Unit = {
    println(s"Here are your ships ->")

    var grid = List.fill[List[String]](gridSize)(List.fill(gridSize)(WATER + BLOCK))
    shots.foreach((shot) => {
      grid = grid.updated(shot._1, grid(shot._1).updated(shot._2, MISSED + BLOCK))
    })
    ships.foreach((ship) => {
      ship.squares.foreach(point => {
        val touched: Boolean = point._2
        if (touched) {
          grid = grid.updated(point._1._1, grid(point._1._1).updated(point._1._2, TOUCHED + BLOCK))
        } else {
          grid = grid.updated(point._1._1, grid(point._1._1).updated(point._1._2, NOT_TOUCHED + BLOCK))
        }
      })
    })
    showGrid(grid, gridSize)
  }

  def showOpponentGrid(shots: Map[(Int, Int), Boolean], gridSize: Int): Unit = {
    println(s"Here are your shots->")

    var grid = List.fill[List[String]](gridSize)(List.fill(gridSize)(WATER + BLOCK))
    shots.foreach((shot) => {
      grid = grid.updated(shot._1._1, grid(shot._1._1).updated(shot._1._2, if (shot._2) TOUCHED + BLOCK else NOT_TOUCHED + BLOCK))
    })
    showGrid(grid, gridSize)
  }
}
