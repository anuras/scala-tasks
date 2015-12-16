import conf.args.JobArguments
import scala.collection.immutable.HashMap
import TokenAggrHelpers._

object TokenAggregatorBasic extends App {

  val inputArgs = new JobArguments().parseArgs(args.toList)

  //read lines from file
  val lines = scala.io.Source.fromFile(inputArgs.input).getLines()
    .drop(if (inputArgs.header) 1 else 0) //filter out header
  //fold on lines producing HashMaps
  val tokenCounts = lines.foldLeft(HashMap.empty[String, Int]){
    (tempMap, line) => extractPlusHashMap(line, tempMap)
  }

  writeTokensToFile(tokenCounts, inputArgs.output)
}