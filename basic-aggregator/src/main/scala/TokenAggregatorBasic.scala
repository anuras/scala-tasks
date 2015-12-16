import java.io.{FileWriter, BufferedWriter}

import scala.collection.immutable.HashMap

object TokenAggregatorBasic extends App {

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

    val inputfile = "tokenfile.txt"
    val outputfile = "tokenoutputfile.txt"

    val lines = scala.io.Source.fromFile(inputfile).getLines
    val tokenCounts = lines.foldLeft(HashMap.empty[String, Int]){
      (tempMap, line) => extractPlusHashMap(line, tempMap)
    }

    writeTokensToFile(tokenCounts, outputfile)
}