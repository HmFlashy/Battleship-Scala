package battleship.utils.display

import battleship.core.models.{PlayerTrait, Ship}

import scala.annotation.tailrec

object GridDisplay {

  private val TOUCHED = Console.RED
  private val NOTTOUCHED = Console.GREEN
  private val WATER = Console.BLUE
  private val MISSED = Console.BLUE_B
  private val BLOCK = "███"
  private val BASE_COLOR = Console.WHITE

  private def showGrid(grid: List[List[String]]): Unit = {

    @tailrec
    def showGridTR(grid: List[List[String]]): Unit = {
      val lineOption: Option[List[String]] = grid.headOption
      lineOption match {
        case Some(line) => {
          val textOption: Option[String] = line.headOption
          textOption match {
            case Some(text) => {
              print(text)
              showGridTR(grid.updated(0, line.tail))
            }
            case _ => {
              println()
              showGridTR(grid.tail)
            }
          }
        }
        case _ => { print(BASE_COLOR) }
      }
    }
    showGridTR(grid)
  }

  def showPlayerGrid(ships: Seq[Ship], shots: Seq[(Int, Int)]): Unit = {
    var grid = Array.ofDim[String](10, 10).toList.map((array) => Array.fill[String](10)(WATER + BLOCK).toList)
    shots.foreach((shot) => {
      grid = grid.updated(shot._1, grid(shot._1).updated(shot._2, MISSED + BLOCK))
    })
    ships.foreach((ship) => {
      ship.squares.foreach(point => {
        val touched: Boolean = point._2
        if(touched) {
          grid = grid.updated(point._1._1, grid(point._1._1).updated(point._1._2, TOUCHED + BLOCK))
        } else {
          grid = grid.updated(point._1._1, grid(point._1._1).updated(point._1._2, NOTTOUCHED + BLOCK))
        }
      })
    })
    showGrid(grid)
  }

  def showOpponentGrid(opponent: PlayerTrait): Unit = {
    println("")
  }
}
