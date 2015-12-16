package conf.args

import org.apache.spark.SparkConf

case class JobArguments (
  name: String = "TokenAggregator",
  input: String = "",
  output: String = "",
  header: Boolean = false,
  local: Boolean = false
) {

  private def updateArguments(oldargs: JobArguments, args: List[String]): JobArguments = {
    args match {
      case Nil => oldargs
      case "--input" :: value :: tail =>
        updateArguments(oldargs.copy(input = value), tail)
      case "--output" :: value :: tail =>
        updateArguments(oldargs.copy(output = value), tail)
      case "--header" :: tail =>
        updateArguments(oldargs.copy(header = true), tail)
      case "--local-win" :: tail =>
        updateArguments(oldargs.copy(local = true), tail)
      case "--name" :: value :: tail =>
        updateArguments(oldargs.copy(name = value), tail)
      case option :: tail => {
        println("Unknown option " + option)
        updateArguments(oldargs, tail)
      }
    }
  }

  def parseArgs(args: List[String]) =
    updateArguments(this, args)

  def isLocal = this.local
}

object SparkJobConf {
  def autoConfig(jobArgs: JobArguments) = {
    if (jobArgs.isLocal) {
      System.setProperty("hadoop.home.dir", "C:\\winutil\\")
      new SparkConf().setAppName(jobArgs.name).setMaster("local")
    } else {
      new SparkConf().setAppName(jobArgs.name)
    }
  }
}