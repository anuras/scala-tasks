import conf.args.JobArguments
import org.apache.spark.SparkContext

object SparkJobs {
  def AggregateTokensJob(inputArgs: JobArguments, sc: SparkContext): Unit = {
    val separator = ","

    //read lines from file
    val lines = sc
      .textFile(inputArgs.input)

    //skip header
    val filteredLines = if (inputArgs.header) {
      val head = lines.first()
      lines.filter(line => line != head) // will skip any lines that are equal to header
    } else lines

    //extract tokens
    val tokens = filteredLines
      .flatMap(line => line.split(separator))

    //count tokens
    val tokenCounts = tokens
      .map(token => (token, 1))
      .keyBy({ case (key, value) => key })
      .reduceByKey({ case ((key1, value1), (key2, value2)) => (key1, value1 + value2)})
      .values

    tokenCounts.saveAsTextFile(inputArgs.output)
  }
}
