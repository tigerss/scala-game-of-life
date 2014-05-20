package gof

import java.awt.{Graphics2D, Color}
import scala.swing.Panel

class Board(var data: Array[Array[Int]]) extends Panel {

  override def paintComponent(g: Graphics2D) {
    val dx = g.getClipBounds.width.toFloat  / data.length
    val dy = g.getClipBounds.height.toFloat / data.map(_.length).max
    for {
      x <- 0 until data.length
      y <- 0 until data(x).length
      x1 = (x * dx).toInt
      y1 = (y * dy).toInt
      x2 = ((x + 1) * dx).toInt
      y2 = ((y + 1) * dy).toInt
    } {
      val cell = data(x)(y)
      var cellColor = Color.LIGHT_GRAY

      if (cell == GameHandler.CellAlive) {
        cellColor = Color.BLUE
      }

      g.setColor(cellColor)
      g.fillRect(x1, y1, x2 - x1, y2 - y1)
    }
  }
}