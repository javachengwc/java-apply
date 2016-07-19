package com.kafka.demo;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

/**
 * 分区规则类
 */
public class DemoPartitioner implements Partitioner {

    public DemoPartitioner(VerifiableProperties props) {
        //注意:构造函数的函数体没有东西，但是不能没有构造函数
    }

    /**
     *
     * @param key   在producer中put消息的key
     * @param numPartitions  分区数
     * @return
     */
    @Override
    public int partition(Object key, int numPartitions) {
        try {
            long partitionNum = Long.parseLong((String) key);
            return (int) Math.abs(partitionNum % numPartitions);
        } catch (Exception e) {
            return Math.abs(key.hashCode() % numPartitions);
        }
    }
}