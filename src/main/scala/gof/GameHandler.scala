package gof

import akka.actor.Actor

object GameHandler {

    case object Next
    case class Update(data: Array[Array[Int]])

  val BoardDimension = 200

  val CellDead = 0
  val CellAlive = 1
  val AliveChance = 0.95

  var data = Array.ofDim[Int](BoardDimension, BoardDimension)

  var initialized = false

  class GameHandler extends Actor {

    def receive = {
      case GameHandler.Next =>
        nextIteration()
    }

    def nextIteration() = {
      //println("compute next iteration")
      if (initialized) {
        computeNextGeneration()
      } else {
        data = initBoard()
        initialized = true
      }

      context.system.actorSelection("/user/Messenger") ! GameHandler.Update(data)
    }
  }

  def computeNextGeneration() = {
    val nextGeneration = Array.ofDim[Int](BoardDimension, BoardDimension)
    for (line <- 0 until data.length) {
      for (column <- 0 until data(line).length) {
        var sum = 0
        val cellPosition = new Position(line, column)
        val neighbours = cellPosition.neighboringCoordinates
        //println("cell(" + line + ")(" + column + ")")
        neighbours.foreach {
          item =>
            val cellValue = getCellValue(item)
            //println("neighbour(" + item.line + ")(" + item.column + ") = " + cellValue)
            sum += cellValue
        }
        //println("Sum = " + sum)
        nextGeneration(line)(column) = sum
      }
    }

    for (line <- 0 until nextGeneration.length) {
      for (column <- 0 until nextGeneration(line).length) {
        val sumOfNeighbours = nextGeneration(line)(column)

        var value = CellDead
        if (isCellAlive(line, column)) {
          if ((sumOfNeighbours == 2) || (sumOfNeighbours == 3)) {
            value = CellAlive
          }
        } else {
          if (sumOfNeighbours == 3) {
            value = CellAlive
          }
        }

        data(line)(column) = value
      }
    }
  }

  def isCellAlive(line: Int, column: Int) : Boolean = {
    CellAlive == data(line)(column)
  }

  def getCellValue(p: Position) : Int = {
    try {
      data(p.line)(p.column)
    } catch {
      case e: Exception => 0
    }
  }

  def initBoard() : Array[Array[Int]] = {
//    for (x <- 0 until data.length) {
//      for (y <- 0 until data(x).length) {
//        data(x)(y) = if (Math.random() < AliveChance) {
//          CellDead
//        } else {
//          CellAlive
//        }
//      }
//    }

    // Oscillator
    data(1)(0) = CellAlive
    data(1)(1) = CellAlive
    data(1)(2) = CellAlive

    // Acorn
    val startX = 100
    val startY = 150
    data(startX)(startY) = CellAlive
    data(startX + 1)(startY + 2) = CellAlive
    data(startX + 2)(startY - 1) = CellAlive
    data(startX + 2)(startY) = CellAlive
    data(startX + 2)(startY + 3) = CellAlive
    data(startX + 2)(startY + 4) = CellAlive
    data(startX + 2)(startY + 5) = CellAlive

    data
  }

}
