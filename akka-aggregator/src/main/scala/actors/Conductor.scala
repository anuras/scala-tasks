import akka.actor.{PoisonPill, ActorRef, Props, Actor}
import conf.args.JobArguments
import scala.collection.immutable.HashMap

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
    //actors are chained with ProcessFileFor()
    case Process(inputArgs) => filereader ! ProcessFileFor(inputArgs.input, tokenizer, aggregator, inputArgs.header)
    //checking for completion and shutting down actors in order
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
