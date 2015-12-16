package conf.args

case class JobArguments (
  name: String = "TokenAggregator",
  input: String = "",
  output: String = "",
  header: Boolean = false
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