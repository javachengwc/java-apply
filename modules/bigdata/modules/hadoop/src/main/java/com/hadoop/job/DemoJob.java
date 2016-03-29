package com.hadoop.job;

import com.hadoop.job.base.BaseJob;
import com.hadoop.job.base.BaseReduce;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.joda.time.DateTime;

/**
 *
 */
public class DemoJob extends BaseJob {

    @Override
    public boolean run(Configuration conf, String filePaths, Path outPath) throws Exception {

        Job job = Job.getInstance(conf);
        job.setJobName("demo_job " + new DateTime().toString("YYYY/MM/dd HH:mm:ss"));
        job.setJarByClass(DemoJob.class);
        job.setInputFormatClass(com.hadoop.mapreduce.LzoTextInputFormat.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setMapperClass(DemoMapper.class);

        job.setCombinerClass(BaseReduce.class);
        job.setReducerClass(BaseReduce.class);

        FileInputFormat.setInputPaths(job, filePaths);
        FileOutputFormat.setOutputPath(job, outPath);

        job.setOutputFormatClass(TextOutputFormat.class);

        return job.waitForCompletion(true);
    }
}

