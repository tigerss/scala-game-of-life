package gof

import akka.actor._
import scala.swing.{MainFrame, Frame, SimpleSwingApplication}
import gof.GameHandler.GameHandler
import scala.concurrent.duration.FiniteDuration
import java.util.concurrent.TimeUnit
import com.typesafe.config.{Config, ConfigFactory}
import java.awt.Dimension

object Main extends SimpleSwingApplication {

  val data = GameHandler.initBoard()

//  // plot some points
//  data(0)(0) = Color.BLACK
//  data(4)(4) = Color.RED
//  data(0)(4) = Color.GREEN
//  data(4)(0) = Color.BLUE
//
//  // draw a circle
//  import math._
//  {
//    for {
//      t <- Range.Double(0, 2 * Pi, Pi / 60)
//      x = 12.5 + 10 * cos(t)
//      y = 12.5 + 10 * sin(t)
//      c = new Color(0.5f, 0f, (t / 2 / Pi).toFloat)
//    } data(x.toInt)(y.toInt) = c
//  }

  var board = new Board(data) {
    preferredSize = new Dimension(toolkit.getScreenSize.getHeight.toInt, toolkit.getScreenSize.getHeight.toInt)
  }

  override def top: Frame = new MainFrame {
    contents = board

    val config: Config = ConfigFactory.parseString("""akka {
         loglevel = "DEBUG"
         log-dead-letters = on
         actor {
           debug {
             receive = on
             lifecycle = on
           }
         }
       }""").withFallback(ConfigFactory.load())

    val system = ActorSystem("Life", config)
    val messenger = system.actorOf(Props[Messenger], "Messenger")
    val game = system.actorOf(Props[GameHandler], "Game")



    import system.dispatcher
    system.scheduler.schedule(
      new FiniteDuration(50, TimeUnit.MILLISECONDS),
      new FiniteDuration(100, TimeUnit.MILLISECONDS),
      game,
      GameHandler.Next)
//    game.tell(GameHandler.Next, messenger)
  }

  class Messenger extends Actor {
    def receive = {
      case GameHandler.Update(lifeData) =>
        //println("Update game")
        board.data = lifeData
        board.repaint()
    }
  }
}