import java.io.{FileWriter, BufferedWriter}
import akka.actor.Actor
import scala.collection.immutable.HashMap

class TokenAggregator extends Actor {
  var aggrTokens: HashMap[String, Int] = HashMap.empty[String, Int]

  //merge HashMaps
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
    case DumpToFile(filename) => {
      writeTokensToFile(filename)
      sender ! OutputComplete
    }
    case AskForTokens => sender ! GroupedTokens(aggrTokens)
  }
}
