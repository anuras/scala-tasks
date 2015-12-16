## Running the app in docker image

```
{user} docker run -it -p 8088:8088 -p 8042:8042 -v $PWD/target/scala-2.11:/mnt -v $PWD/testdata:/mnt/testdata -h sandbox sequenceiq/spark:1.5.1 bash
{container} mkdir /workdir
{container} cp /mnt/spark-aggregator.jar /workdir/spark-aggregator.jar
{container} hadoop fs -mkdir /testdir
{container} hadoop fs -put /mnt/testdata/tokentestfile.txt /testdir/tokentestfile.txt

{container} spark-submit \
--class TokenAggregatorSpark \
--master yarn-client \
--driver-memory 1g \
--executor-memory 1g \
--executor-cores 1 \
/workdir/spark-aggregator.jar --input /testdir/tokentestfile.txt --output /testdir/tokenoutput

{container} hadoop fs -get /testdir/tokenoutput/ /workdir/tokenoutput
```
