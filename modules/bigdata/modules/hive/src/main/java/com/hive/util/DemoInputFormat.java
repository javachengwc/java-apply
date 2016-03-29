package com.hive.util;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.io.Text;

import java.io.IOException;

/**
 * 自定义hive的Inputformat
 * 打成jar包，放到hive_home/lib即hive/lib里面
 * 由于hive是基于hadoop集群运行的，所以hadoop/lib里面也必须放入DemoInputFormat.jar
 *
 * 1.上传到HIVE服务器上JAVAC编译
 * javac -cp ./:/usr/lib/hadoop/hadoop-common.jar:/home/op1/hadoop/hadoop-core-1.0.3.jar:/usr/lib/hadoop/lib/commons-logging-1.1.1.jar *\/**\/*\/*\/*
 *
 * 2.jar打包类文件
 * jar -cf DemoInputFormat.jar /home/src/
 *
 * 3.复制DemoInputFormat.jar到 Hive/lib, Hadoop/lib 目录下
 */
public class DemoInputFormat extends TextInputFormat implements JobConfigurable {

    public String toUseString(int[] ints, String a) {
        int beginIndex = 0;
        int endIndex = 0;
        int sum = 0;
        StringBuffer s = new StringBuffer("");
        for (int i = 0; i < ints.length; i++) {
            sum += ints[i];
            endIndex = sum;
            String c = a.substring(beginIndex, endIndex);
            beginIndex = sum;
            s.append(c + "\001");
        }
        return s.toString();
    }

    @Override
    public RecordReader<LongWritable, Text> getRecordReader(InputSplit genericSplit, JobConf job, Reporter reporter) throws IOException {

        reporter.setStatus(genericSplit.toString());

        Demo2RecordReader reader = new Demo2RecordReader((FileSplit) genericSplit,job);

        return reader;

    }
}
