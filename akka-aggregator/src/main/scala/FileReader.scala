import akka.actor.Actor

/**
 * Created by A.Marcinkevicius on 12/15/2015.
 */

class FileReader extends Actor {
  def receive = {
    case ProcessFileFor(filename, sendTo, nextSendTo, header) =>
      scala.io.Source.fromFile(filename).getLines()
        .drop(if (header) 1 else 0) //filter out header
        .foreach(line => sendTo ! LineToProcess(line, nextSendTo))
      sender ! FileProcessed
  }
}
