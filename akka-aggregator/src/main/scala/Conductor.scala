import akka.actor.{PoisonPill, ActorRef, Props, Actor}
import conf.args.JobArguments
import scala.collection.immutable.HashMap

/**
 * Created by A.Marcinkevicius on 12/15/2015.
 */
case class Process(inputArgs: JobArguments)
case class ProcessFileFor(file: String, passTo: ActorRef, nextPassTo: ActorRef, header: Boolean = false)
case class LineToProcess(line: String, passTo: ActorRef)
case class GroupedTokens(tokens: HashMap[String, Int])
case class DumpToFile(filename: String)

case object FileProcessed
case object AllLinesProcessed
case object OutputComplete

class Conductor(outputfile: String) extends Actor {

  val filereader = context.actorOf(Props[FileReader], "reader")
  val tokenizer  = context.actorOf(Props[Tokenizer], "tokenizer")
  val aggregator = context.actorOf(Props[TokenAggregator], "tokenaggregator")

  def receive = {
    case Process(inputArgs) => filereader ! ProcessFileFor(inputArgs.input, tokenizer, aggregator, inputArgs.header)
    case FileProcessed => {
      filereader ! PoisonPill
      tokenizer ! FileProcessed
    }
    case AllLinesProcessed => {
      tokenizer ! PoisonPill
      aggregator ! DumpToFile(outputfile: String)
    }
    case OutputComplete => {
      aggregator ! PoisonPill
      context.system.terminate()
    }
  }
}
