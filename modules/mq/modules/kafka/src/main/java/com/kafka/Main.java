package com.kafka;

import com.kafka.mode.KafkaBroker;
import com.kafka.mode.KafkaConsumerGroup;
import com.kafka.mode.KafkaTopic;
import com.kafka.service.KafkaClient;

import java.util.List;

public class Main {

    public static void main(String args []) throws Exception
    {
        //获取kafka信息
        List<KafkaBroker>  brokersList = KafkaClient.getInstance().getBrokers();
        int brokerCnt =(brokersList==null)?0:brokersList.size();
        System.out.println("----------------broker count ="+brokerCnt+"----------------");
        if(brokerCnt>0)
        {
            for(KafkaBroker broker:brokersList)
            {
                System.out.println(broker.toString());
            }
        }

        //获取topic信息
        List<KafkaTopic> topicList =KafkaClient.getInstance().getTopics();
        int topicCnt =(topicList==null)?0:topicList.size();
        System.out.println("----------------topic count ="+topicCnt+"----------------");
        if(topicCnt>0)
        {
            for(KafkaTopic topic:topicList)
            {
                System.out.println(topic.toString());
            }
        }

        //获取consumer信息
        List<KafkaConsumerGroup> consumerList =KafkaClient.getInstance().getConsumerGroup();
        int consumerCnt =(consumerList==null)?0:consumerList.size();
        System.out.println("----------------consumer count ="+consumerCnt+"----------------");
        if(consumerCnt>0)
        {
            for(KafkaConsumerGroup consumer:consumerList)
            {
                System.out.println(consumer.toString());
            }
        }

        //获取某topic某分区下的partition
        String topic="hello";
        int partition=0;
        long earliestOffset=KafkaClient.getInstance().getEarliestOffset(topic,partition);
        long lastestOffset=KafkaClient.getInstance().getLatestOffset(topic, partition);
        System.out.println("--------topic:"+topic+",partition:"+partition+"--------earliestOffset="+earliestOffset+",lastestOffset="+lastestOffset);

        //改变consumer为aa的offset，让它重置offset,重新从offset获取消息
        String group="aa";
        long groupOffset= KafkaClient.getInstance().getOffset(topic,partition,group);

        long setOffset=9999;
        long afterOffset= KafkaClient.getInstance().setOffset(topic,partition,group,setOffset);

        System.out.println("--------group:"+group+",topic:"+topic+",partition:"+partition+"--------groupOffset="+groupOffset+",afterOffset="+afterOffset);
    }
}
