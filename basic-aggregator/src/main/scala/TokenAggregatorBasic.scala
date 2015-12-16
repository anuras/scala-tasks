import java.io.{FileWriter, BufferedWriter}
import conf.args.JobArguments
import scala.collection.immutable.HashMap
import TokenAggrHelpers._

object TokenAggregatorBasic extends App {

  val inputArgs = new JobArguments().parseArgs(args.toList)

  val lines = scala.io.Source.fromFile(inputArgs.input).getLines()
    .drop(if (inputArgs.header) 1 else 0) //filter out header
  val tokenCounts = lines.foldLeft(HashMap.empty[String, Int]){
    (tempMap, line) => extractPlusHashMap(line, tempMap)
  }

  writeTokensToFile(tokenCounts, inputArgs.output)
}