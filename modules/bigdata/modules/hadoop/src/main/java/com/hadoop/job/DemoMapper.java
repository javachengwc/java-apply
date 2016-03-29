package com.hadoop.job;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class DemoMapper  extends Mapper<LongWritable, Text, Text, IntWritable> {

    private IntWritable one = new IntWritable(1);

    public static String RECORD_KEY = "record";

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        try{
            String line = value.toString();
            if (line == null || "".equals(line.trim())) {
                return;
            }

            InputSplit inputSplit = context.getInputSplit();
            String filePath = ((FileSplit) inputSplit).getPath().toUri().getPath();

            Integer hourOfFile = getHourFromFileName(filePath,"(?<=-)\\d{2}");

            context.write(new Text(RECORD_KEY + "_" + hourOfFile), one);

        }catch (Exception e){

            e.printStackTrace();
        }

    }

    protected static Integer getHourFromFileName(String fileName, String regEx) {

        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(0));
        }
        return null;
    }
}