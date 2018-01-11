import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.util.{Failure, Success}

import akka.Done
import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.typesafe.scalalogging.slf4j.LazyLogging
import kamon.Kamon
import kamon.trace.Tracer

/**
  * Created by jjacobsson on 2018-01-10.
  */
object Main extends App with LazyLogging {
  Kamon.start()

  implicit val system: ActorSystem = ActorSystem("kamon-akka")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val stream =
    Source(1 to 100)
      // Adding throttle will make the propagation work again
      //.throttle(30, 1.second, 1, ThrottleMode.Shaping)
      .via(traceToken)
      .via(asyncFlow)
      .runWith(sink)

  stream.onComplete {
    case Success(Done) => logger.debug(s"Stream completed OK")
    case Failure(e) => logger.debug(s"Stream failed with exc: $e")
  }

  def traceToken = Flow[Int].map { i =>
    // Start a new trace context and set a custom trace token
    Tracer.setCurrentContext(Kamon.tracer.newContext("kamon-context", Some(s"token-$i")))
    checkTraceToken("Init", i)
  }

  def asyncFlow = Flow[Int].mapAsync(10) { i =>
    Future {
      checkTraceToken("Async", i)
    }
  }

  def sink: Sink[Int, Future[Done]] = Sink.foreach { i =>
    checkTraceToken("Sink", i)
    Tracer.currentContext.finish()
    Tracer.clearCurrentContext
  }

  def checkTraceToken(text: String, i: Int): Int = {
    // Read the trace token and compare it to the correct value
    val token = Tracer.currentContext.token
    if (token != s"token-$i")
      logger.error(s"*** $text *** '$i' doesn't match '$token' !!")
    i
  }
}
