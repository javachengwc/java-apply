package com.hive.util;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.LineRecordReader;
import org.apache.hadoop.mapred.RecordReader;

import java.io.IOException;

/**
 * 自定义hive的Inputformat中的reader
 */
public class DemoRecordReader implements RecordReader<LongWritable, Text> {

    private LineRecordReader reader;

    private Text text;

    public DemoRecordReader(LineRecordReader reader) {
        this.reader = reader;
        text = reader.createValue();
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    @Override
    public LongWritable createKey() {
        return reader.createKey();
    }

    @Override
    public Text createValue() {
        return new Text();
    }

    @Override
    public long getPos() throws IOException {
        return reader.getPos();
    }

    @Override
    public float getProgress() throws IOException {
        return reader.getProgress();
    }

    @Override
    public boolean next(LongWritable key, Text value) throws IOException {

        while (reader.next(key, text)) {

            int[] ints=new int[]{10,16,16,20,20,16,10,4,6,10,10,10};

            String str=text.toString();

            DemoInputFormat ift=new DemoInputFormat();

            String strReplace= ift.toUseString(ints, str);

            Text txtReplace = new Text();

            txtReplace.set(strReplace);

            value.set(txtReplace.getBytes(), 0, txtReplace.getLength());

            return true;
        }
        return false;
    }
}