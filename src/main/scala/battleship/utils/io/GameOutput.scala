package battleship.utils.io

import java.io.{BufferedWriter, FileWriter}
import battleship.core.models.Player

object GameOutput {

  def writeResults(results: Set[(Player, Player)]): Unit = {
    val outputFile = new BufferedWriter(new FileWriter("results.csv"))
    val csvFields = "Player 1, Result player 1, Result player 2, Player 2\n" + results.map(result => s"${result._1.name}, ${result._1.numberOfWins.toString}, ${result._2.numberOfWins.toString}, ${result._2.name}\n").mkString
    outputFile.write(csvFields)
    outputFile.close()
  }
}
