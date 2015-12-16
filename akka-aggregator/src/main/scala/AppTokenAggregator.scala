import akka.actor._
import conf.args.JobArguments

object AppTokenAggregator extends App {
  val inputArgs = new JobArguments().parseArgs(args.toList)

  val system = ActorSystem("tokenakka")

  val conductor = system.actorOf(Props(classOf[Conductor], inputArgs.output), "conductor")

  conductor ! Process(inputArgs)

}