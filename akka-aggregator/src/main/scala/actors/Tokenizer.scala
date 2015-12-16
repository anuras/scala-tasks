import akka.actor.Actor
import scala.collection.immutable.HashMap
import scala.concurrent.Future
import scala.util.{Failure, Success}

class Tokenizer extends Actor {
  import context.dispatcher

  //split lines by separator to Future[HashMap[String, Int]]
  def extractAndGroup(line: String): Future[HashMap[String, Int]] = {
    val separator = ","
    Future(
      line
        .split(separator)
        .foldLeft(HashMap.empty[String, Int]){
        (tempMap, token) => tempMap + (token -> (tempMap.getOrElse(token, 0) + 1))
      })
  }

  def receive = {
    case LineToProcess(line, passTo) => {
      val tokenMap = extractAndGroup(line)
      tokenMap.onComplete{
        case Success(tokens) => passTo ! GroupedTokens(tokens)
        case Failure(ex) => println(ex.getMessage) // modify
      }
    }
    case FileProcessed => sender ! AllLinesProcessed
  }
}
