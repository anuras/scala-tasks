import akka.actor._
import conf.args.JobArguments

object AppTokenAggregator extends App {
  //process input arguments
  val inputArgs = new JobArguments().parseArgs(args.toList)

  val system = ActorSystem("tokenakka")
  //conductor actor runs all child actors
  val conductor = system.actorOf(Props(classOf[Conductor], inputArgs.output), "conductor")
  conductor ! Process(inputArgs)

}