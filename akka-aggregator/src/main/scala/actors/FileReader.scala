import akka.actor.Actor

class FileReader extends Actor {
  def receive = {
    case ProcessFileFor(filename, sendTo, nextSendTo, header) =>
      scala.io.Source.fromFile(filename).getLines()
        .drop(if (header) 1 else 0) //filter out header
        .foreach(line => sendTo ! LineToProcess(line, nextSendTo))
      // respond once all lines are processed
      sender ! FileProcessed
  }
}
