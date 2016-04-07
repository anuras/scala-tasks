import org.apache.spark.SparkContext
import conf.args.{JobArguments, SparkJobConf}

object TokenAggregatorSpark {
  def main(args: Array[String]): Unit = {

    val inputArgs = new JobArguments().parseArgs(args.toList)
    val sparkConf = SparkJobConf.autoConfig(inputArgs)
    val sc = new SparkContext(sparkConf)
    //main job
    //SparkJobs.AggregateTokensJob(inputArgs, sc)
    SparkJobs.ReadParquetJob(inputArgs, sc)
    sc.stop()

  }
}