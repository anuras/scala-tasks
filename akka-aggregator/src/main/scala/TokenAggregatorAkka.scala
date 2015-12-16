import java.io.{FileWriter, BufferedWriter}

import akka.actor.{ ActorRef, ActorSystem, Props, Actor, Inbox }
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.collection.immutable.HashMap
import scala.util.{Failure, Success}

case class ProcessFileFor(file: String, passTo: ActorRef, nextPassTo: ActorRef)
case class LineToProcess(line: String, passTo: ActorRef)
case class GroupedTokens(tokens: HashMap[String, Int])
case class DumpToFile(filename: String)

class FileReader extends Actor {
  def receive = {
    case ProcessFileFor(filename, sendTo, nextSendTo) =>
      scala.io.Source.fromFile(filename).getLines.foreach(line => sendTo ! LineToProcess(line, nextSendTo))
  }
}

class Tokenizer extends Actor {
  import context.dispatcher

  def extractAndGroup(line: String): Future[HashMap[String, Int]] = {
    val separator = " "
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
  }
}

class TokenAggregator extends Actor {
  var aggrTokens: HashMap[String, Int] = HashMap.empty[String, Int]

  def joinMaps(hm1: HashMap[String, Int], hm2: HashMap[String, Int]): HashMap[String, Int] = {
    hm1.merged(hm2){
      case ((key1, value1), (key2, value2)) => (key1, value1 + value2)
    }
  }

  def writeTokensToFile(filename: String) = {
    val writer = new BufferedWriter(new FileWriter(filename))
    aggrTokens.foreach {
      case (key, value) => {
        writer.write('(' + key + ',' + value + ')')
        writer.newLine()
      }
    }
    writer.flush()
    writer.close()
  }

  def receive = {
    case GroupedTokens(tokens: HashMap[String, Int]) => aggrTokens = joinMaps(aggrTokens, tokens)
    case DumpToFile(filename) => writeTokensToFile(filename)
  }
}

object TokenAggregatorAkka extends App {

  // Create the 'helloakka' actor system
  val system = ActorSystem("tokenakka")

  // Create the 'greeter' actor
  val filereader = system.actorOf(Props[FileReader], "reader")

  val tokenizer = system.actorOf(Props[Tokenizer], "tokenizer")

  val aggregator = system.actorOf(Props[TokenAggregator], "tokenaggregator")

  val inputfile = "tokenfile.txt"
  val outputfile = "tokenoutputfile.txt"

  filereader ! ProcessFileFor(inputfile, tokenizer, aggregator)

  Thread.sleep(2000)

  aggregator ! DumpToFile(outputfile)
  system.terminate()
}