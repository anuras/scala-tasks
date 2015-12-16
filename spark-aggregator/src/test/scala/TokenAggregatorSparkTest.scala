import org.apache.spark.SparkContext
import org.specs2._
import scala.util.Random
import conf.args.{SparkJobConf, JobArguments}
import scalax.file.Path

object TokenAggregatorTest {

  def RunTestSkipHeader(sc: SparkContext): (Boolean, Boolean) = {

    val outputDirectory = "testdata/" + Random.nextString(10)
    val testArgs = Array("--name", "Aggregator Test", "--input", "testdata/tokentestfile.txt", "--output", outputDirectory, "--local-win", "--header")
    val testJobArgs = new JobArguments().parseArgs(testArgs.toList)
    SparkJobs.AggregateTokensJob(testJobArgs, sc)

    val testOutput = sc
      .textFile(outputDirectory)
      .collect()

    val testpath: Path = Path(outputDirectory)
    try {
      testpath.deleteRecursively(continueOnFailure = false)
    }

    val targetTestFile = "testdata/tokentestoutputskipheader.txt"
    val targetOutput = sc
      .textFile(targetTestFile)
      .collect()

    //
    //testOutput.foreach(el => println("test:" + el))
    //targetOutput.foreach(el => println("target:" + el))
    //compare strings
    (testOutput.length == targetOutput.length, testOutput.sorted.sameElements(targetOutput.sorted))

  }

  def RunTestWithHeader(sc: SparkContext): (Boolean, Boolean) = {

    val outputDirectory = "testdata/" + Random.nextString(10)
    val testArgs = Array("--name", "Aggregator Test", "--input", "testdata/tokentestfile.txt", "--output", outputDirectory, "--local-win")
    val testJobArgs = new JobArguments().parseArgs(testArgs.toList)
    SparkJobs.AggregateTokensJob(testJobArgs, sc)

    val testOutput = sc
      .textFile(outputDirectory)
      .collect()

    val testpath: Path = Path(outputDirectory)
    try {
      testpath.deleteRecursively(continueOnFailure = false)
    }

    val targetTestFile = "testdata/tokentestoutput.txt"
    val targetOutput = sc
      .textFile(targetTestFile)
      .collect()

    //compare strings
    (testOutput.length == targetOutput.length, testOutput.sorted.sameElements(targetOutput.sorted))

  }
}

class SparkJobsTest extends Specification {

  def is = s2"""
                Token aggregator job should:
                Return correct number of unique tokens $t1
                Return correct counts of tokens $t2
                Skip header when asked to and return correct number of unique tokens $t3
                Skip header when asked to and return correct counts of token $t4
  """

  val sparkTestConf = SparkJobConf.autoConfig(new JobArguments().parseArgs(List("--name", "Token Aggregator Spark Jobs Local Test", "--local-win")))
  val testContext = new SparkContext(sparkTestConf)

  val (t1, t2) = TokenAggregatorTest.RunTestWithHeader(testContext)
  val (t3, t4) = TokenAggregatorTest.RunTestSkipHeader(testContext)

  testContext.stop()
}
