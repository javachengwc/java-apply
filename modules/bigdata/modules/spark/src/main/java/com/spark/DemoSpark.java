package com.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import redis.clients.jedis.Jedis;
import scala.Tuple2;

import java.util.Arrays;

/**
 * 本示例从hdfs读取文件
 * 按照word count程序计算后将结果存入redis
 *
 * sh startDemoSparkTask.sh /tmp/data.out word_key  或
 * spark-submit --class com.spark.DemoSpark --master spark://localhost:7077 /../DemoSpark.jar /tmp/data.out word_key
 * (--driver-class-path ./mysql-connector-java-5.1.39.jar
 * --executor-memory 2G
 * --driver-memory 2G
 * --num-executors 50 )这些选项参数必要的时候可在spark-submit执行时带上
 */
public class DemoSpark {

    public static void main(final String[] args) {

        SparkConf conf = new SparkConf().setAppName(DemoSpark.class.getSimpleName());

        JavaSparkContext ctx = new JavaSparkContext(conf);

        JavaRDD<String> lines = ctx.textFile(args[0], 1);

        JavaRDD<String> words = lines.flatMap(
                new FlatMapFunction<String, String>() {
                    @Override
                    public Iterable<String> call(String x) {
                        return Arrays.asList(x.split(" "));
                    }
                });


        JavaPairRDD<String, Integer> pairs = words.mapToPair(
                new PairFunction<String, String, Integer>() {
                    @Override
                    public Tuple2<String, Integer> call(String s) throws Exception {
                        return new Tuple2<String, Integer>(s, 1);
                    }
                });

        JavaPairRDD<String, Integer> wordCounts = pairs.reduceByKey(
                new Function2<Integer, Integer, Integer>() {
                    @Override
                    public Integer call(Integer i1, Integer i2) throws Exception {
                        return i1 + i2;
                    }
                });

        Jedis jedis = new Jedis("127.0.0.1", 6379);
        try {

            jedis.select(11);

            for (Tuple2<String, Integer> tuple2 : wordCounts.collect()) {

                jedis.hset(args[1] + "_word_count", tuple2._1(), tuple2._2() + "");
            }
        } finally {

            jedis.quit();
        }

        ctx.stop();
    }
}
