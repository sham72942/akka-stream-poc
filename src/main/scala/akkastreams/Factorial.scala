package akkastreams

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.IOResult
import akka.stream.scaladsl._
import akka.util.ByteString

import java.nio.file.Paths
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
object Factorial extends App {

  implicit val system: ActorSystem = ActorSystem("AkkaStreams")
  implicit val ec = system.dispatcher

  val range = 1 to 20
  val source: Source[Int, NotUsed] = Source(range)

  val factorials = source.scan(BigInt(1))((acc, next) => acc * next)

  def stringSink(filename: String): Sink[String, Future[IOResult]] =
    Flow[String]
      .map(s => ByteString(s + "\n"))
      .toMat(FileIO.toPath(Paths.get(filename)))(Keep.right)

  factorials
    .zipWith(Source(range))((num, idx) => s"${idx - 1}! = $num")
    .throttle(1, 100.milli)
    .runWith(stringSink("factorials.txt"))
    .onComplete { ioResult =>
      println(ioResult)
      system.terminate()
    }
}
