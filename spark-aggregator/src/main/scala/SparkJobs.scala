import conf.args.JobArguments
import org.apache.spark.SparkContext

object SparkJobs {
  def AggregateTokensJob(inputArgs: JobArguments, sc: SparkContext): Unit = {
    val separator = " "

    val tokens = sc
      .textFile(inputArgs.input)
      .flatMap(line => line.split(separator))

    val tokenCounts = tokens
      .map(token => (token, 1))
      .keyBy({ case (key, value) => key })
      .reduceByKey({ case ((key1, value1), (key2, value2)) => (key1, value1 + value2)})
      .values

    tokenCounts.saveAsTextFile(inputArgs.output)
  }
}
