import conf.args.JobArguments
import scala.collection.immutable.HashMap
import TokenAggrHelpers._

object TokenAggregatorBasic extends App {

  val inputArgs = new JobArguments().parseArgs(args.toList)

  //read lines from file
  val lines = scala.io.Source.fromFile(inputArgs.input).getLines()

  //skip header and count tokens
  val tokenCounts = countTokens(skipHeader(lines, inputArgs.header))

  writeTokensToFile(tokenCounts, inputArgs.output)
}