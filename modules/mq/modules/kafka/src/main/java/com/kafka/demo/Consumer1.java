package com.kafka.demo;

import com.util.PropertiesLoader;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 消费者1
 * 先启动消费者程序，再启动生产者程序
 */
public class Consumer1 {

    public static void main(String[] args) throws Exception {

        Properties props = PropertiesLoader.load("consumer.properties");
        ConsumerConnector connector = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));

        HashMap<String, Integer> topicConnect = new HashMap<String, Integer>();
        String topic = "hello";
        topicConnect.put(topic,1); //设置topic和消费者数量

        Map<String, List<KafkaStream<byte[], byte[]>>> streams= connector.createMessageStreams(topicConnect);

        List<KafkaStream<byte[], byte[]>> list = streams.get(topic);
        int cnt = (list==null)?0:list.size();
        System.out.println("Consumer1 list size="+cnt);
        for (KafkaStream<byte[], byte[]> stream : list) {
            new Thread(new Workers( stream.iterator() )).start();
        }
    }

    static class Workers implements Runnable{

        private ConsumerIterator<byte[], byte[]> iterator ;

        public Workers(ConsumerIterator<byte[], byte[]> iterator) {
            this.iterator = iterator;
        }

        public void run() {
            while ( iterator.hasNext() ){
                MessageAndMetadata<byte[], byte[]> next = iterator.next();
                String key = (next.key() == null)?"": new String(next.key());
                String message = (next.message() == null)?"": new String(next.message());

                System.out.println("Workers key:"+key+",msg:"+message+
                        ",partition="+next.partition()+",offset="+next.offset()+","+Thread.currentThread().getName() );

            }
        }
    }

}
