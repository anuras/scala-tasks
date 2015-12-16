import akka.actor.{Props, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.FlatSpecLike
import org.scalatest.Matchers
import scala.concurrent.duration._
import scala.collection.immutable.HashMap


class AppTokenAggregatorTest(_system: ActorSystem)
  extends TestKit(_system)
  with ImplicitSender
  with Matchers
  with FlatSpecLike
  with BeforeAndAfterAll {

  def this() = this(ActorSystem("AppTokenAggrTest"))

  override def afterAll: Unit = {
    system.shutdown()
    system.awaitTermination(10.seconds)
  }

  "Tokenizer" should "be able to tokenize a string" in {
    val tokenizer = system.actorOf(Props[Tokenizer], "tokenizer")
    tokenizer ! LineToProcess("some,line,some", self)
    expectMsgType[GroupedTokens].tokens.toList.sorted.toString() should
      be(HashMap("some" -> 2, "line" -> 1).toList.sorted.toString())
  }

  "Tokenizer" should "confirm when all lines are finished" in {
    val tokenizer = system.actorOf(Props[Tokenizer], "tokenizer2")
    tokenizer ! FileProcessed
    expectMsg(AllLinesProcessed)
  }

  "TokenAggregator" should "be able to aggregate tokens" in {
    val aggregator = system.actorOf(Props[TokenAggregator], "aggregator")
    aggregator ! GroupedTokens(HashMap("some" -> 2, "line" -> 1))
    aggregator ! GroupedTokens(HashMap("some" -> 2, "line" -> 1))
    aggregator ! AskForTokens
    expectMsgType[GroupedTokens].tokens.toList.sorted.toString() should
      be(HashMap("some" -> 4, "line" -> 2).toList.sorted.toString())
  }

  "FileReader" should "be able to skip the header and send back read lines from files" in {
    val filereader = system.actorOf(Props[FileReader], "filereader")
    val testfile = "testdata/tokentestfile.txt"
    filereader ! ProcessFileFor(testfile, self, self, true)
    expectMsgType[LineToProcess].line should be("abc,abc,ced")
  }

}
