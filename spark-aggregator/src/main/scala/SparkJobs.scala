import conf.args.JobArguments
import org.apache.spark.SparkContext

object SparkJobs {
  def AggregateTokensJob(inputArgs: JobArguments, sc: SparkContext): Unit = {
    val separator = ","

    //read lines from file
//    val lines = sc
//      .textFile(inputArgs.input)

    //handling input from multiple sources
    val multipleInputs = inputArgs.input.split(",")
    val rdds = multipleInputs.map { path => sc.textFile(path)}
    val lines = sc.union(rdds)

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

  def ReadParquetJob(inputArgs: JobArguments, sc: SparkContext) = {
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    import sqlContext.implicits._
    val parquetFile = sqlContext.read.parquet(inputArgs.input)
    parquetFile.registerTempTable("parquetFile")
    val dt = sqlContext.sql("SELECT username, source_ip, source_host_name, details, protocol FROM parquetFile")
    dt.map(t => "Name: " + t(0) + ", source_ip" + t(1) + ", source_hostname" + t(2) + ", protocol" + t(4) + ", details: " + t(3)).collect().foreach(println)

    dt.write.save(inputArgs.output)
  }

}
