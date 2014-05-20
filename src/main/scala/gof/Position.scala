package gof

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class Position(var line: Int, var column: Int) {

  val Same = 0
  val North = -1
  val South = 1
  val West = -1
  val East = 1

  def neighboringCoordinates: mutable.ArrayBuffer[Position] = {
    val neighbors = ArrayBuffer.empty[Position]
    neighbors += getNorthWest()
    neighbors += getNorth()
    neighbors += getNorthEast()
    neighbors += getWest()
    neighbors += getEast()
    neighbors += getSouthEast()
    neighbors += getSouth()
    neighbors += getSouthWest()
    neighbors
  }

  def getNorthWest(): Position = neighbor(North, West)
  def getNorth(): Position = neighbor(North, Same)
  def getNorthEast(): Position = neighbor(North, East)
  def getWest(): Position = neighbor(Same, West)
  def getEast(): Position = neighbor(Same, East)
  def getSouthEast(): Position = neighbor(South, East)
  def getSouth(): Position = neighbor(South, Same)
  def getSouthWest(): Position = neighbor(South, West)

//  def getNorthWest(): Position = neighbor(West, North)
//  def getNorth(): Position = neighbor(North, Same)
//  def getNorthEast(): Position = neighbor(East, North)
//  def getWest(): Position = neighbor(West, Same)
//  def getEast(): Position = neighbor(East, Same)
//  def getSouthEast(): Position = neighbor(East, South)
//  def getSouth(): Position = neighbor(South, Same)
//  def getSouthWest(): Position = neighbor(West, South)

  def neighbor(xDiff: Int, yDiff: Int): Position = new Position(line + xDiff, column + yDiff)
}