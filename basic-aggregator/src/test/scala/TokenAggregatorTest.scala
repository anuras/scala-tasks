import org.scalatest.FunSuite
import TokenAggrHelpers._
import scala.collection.immutable.HashMap

class TokenAggregatorTest extends FunSuite {

  val rawfile = Iterator(
    "sample,header",
    "token1,abc,x",
    "a,b,c,abc",
    "something,x")
  val emptyfile = Iterator()

  val finalMap = HashMap("sample" -> 1, "header" -> 1, "token1" -> 1, "abc" -> 2,
  "x" -> 2, "a" -> 1, "b" -> 1, "c" -> 1, "something" -> 1)
  val skippedHeaderMap = HashMap("token1" -> 1, "abc" -> 2,
    "x" -> 2, "a" -> 1, "b" -> 1, "c" -> 1, "something" -> 1)

  test("Token Aggregator should count tokens correctly") {
    val testTokens = countTokens(rawfile)
    val a = testTokens.toList
    val b = finalMap.toList
    assert(a.sorted.sameElements(b.sorted))
  }

  test("Header should be skipped when requested") {
    assert(skipHeader(rawfile, true).length == 3)
    assert(skipHeader(rawfile, false).length == 4)
  }

  test("Header skipping should work with empty file") {
    assert(skipHeader(emptyfile, true).length == 0)
    assert(skipHeader(emptyfile, false).length == 0)
  }
}

