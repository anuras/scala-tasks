## Run the app

```
sbt assembly
java -jar target\scala-2.11\akka-aggregator.jar --input testdata\tokentestfile.txt --output testdata\outputfilename.txt
```