package com.hadoop.job.base;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;

import java.util.Map;

/**
 *
 */
public class BaseJob extends Configured{

    public boolean run(Configuration conf, String filePaths, Path outPath) throws Exception {
        return false;
    }

    public boolean run(Configuration conf, String filePaths, Path outPath, Map<String, ?> paramMap) throws Exception {
        return false;
    }
}
