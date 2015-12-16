import conf.args.JobArguments
import org.scalatest.FunSuite

import scala.collection.immutable.HashMap

class JobArgumentsTest extends FunSuite {

   val inputArguments = List(
     "--input", "input0",
     "--output", "output0",
     "--header",
     "--name", "name0", "something")

  val parsedArgs1 = new JobArguments().parseArgs(inputArguments)
  val parsedArgs2 = new JobArguments().parseArgs(List())

  test("Arguments should be parsed correctly") {
    assert(parsedArgs1.name == "name0")
    assert(parsedArgs1.input == "input0")
    assert(parsedArgs1.output == "output0")
    assert(parsedArgs1.header)
  }

  test("Empty arguments should result in defaults") {
    assert(parsedArgs2.name == "TokenAggregator")
    assert(parsedArgs2.input == "")
    assert(parsedArgs2.output == "")
    assert(!parsedArgs2.header)
  }

 }

