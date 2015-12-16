import java.io.{FileWriter, BufferedWriter}
import scala.collection.immutable.HashMap

/**
 * Created by A.Marcinkevicius on 12/15/2015.
 */
object TokenAggrHelpers {
  def extractPlusHashMap(line: String, hmap: HashMap[String, Int], separator: String = " "): HashMap[String, Int] = {
    val tokens = line.split(separator)
    tokens.foldLeft(hmap){
      (tempMap, token) => tempMap + (token -> (tempMap.getOrElse(token, 0) + 1))
    }
  }

  def writeTokensToFile(hmap: HashMap[String, Int], filename: String) = {
    val writer = new BufferedWriter(new FileWriter(filename))
    hmap.foreach {
      case (key, value) => {
        writer.write('(' + key + ',' + value + ')')
        writer.newLine()
      }
    }
    writer.flush()
    writer.close()
  }
}