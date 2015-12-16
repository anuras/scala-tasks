import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.SparkContext._
import conf.args.{JobArguments, SparkJobConf}

object TokenAggregatorSpark {
  def main(args: Array[String]): Unit = {

    val inputArgs = new JobArguments().parseArgs(args.toList)
    val sparkConf = SparkJobConf.autoConfig(inputArgs)
    val sc = new SparkContext(sparkConf)
    SparkJobs.AggregateTokensJob(inputArgs, sc)
    sc.stop()

  }
}