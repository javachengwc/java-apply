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
 * kafka的consumer接口，有两种版本
 *     high-level版本,此程序的consumer就是high-level
 *     low-level版本,就是SimpleConsumer
 */
public class Consumer1 {

    public static void main(String[] args) throws Exception {

        Properties props = PropertiesLoader.load("consumer.properties");
        //指定kafka等待多久zookeeper回复（ms）以便放弃并继续消费。
        props.put("zookeeper.session.timeout.ms", "4000");
        //指定zookeeper同步最长延迟多久再产生异常
        props.put("zookeeper.sync.time.ms", "2000");
        // 如果为真，consumer所fetch的消息的offset将会自动的同步到zookeeper。
        props.put("auto.commit.enable", "true");
        //指定多久消费者更新offset到zookeeper中。默认是1分钟，太长了，此处设定为1秒钟
        //注意offset更新时基于time而不是每次获得的消息。一旦在更新zookeeper发生异常并重启，将可能拿到已拿到过的消息
        props.put("auto.commit.interval.ms", "1000");
        ConsumerConnector connector = Consumer.createJavaConsumerConnector(new ConsumerConfig(props));

        HashMap<String, Integer> topicConnect = new HashMap<String, Integer>();
        String topic = "mult";
        //只有一个consumer消费多分区topic的消息时候，下面设置的消费数量(其实就是消费线程数量)会均匀分配多分区消息
        //如果消费数量跟分区数量相等，那每个消费线程将获取固定对应一个分区的消息
        //如果是同一个消费组有多个consumer(每个consumer一个进程)消费一个多分区的topic消息的时候,
        //分区消息会均匀分配到各个consumer,如果consumer数量等于分区数，每个consumer将独立消费一个分区的消息。
        topicConnect.put(topic,2); //设置topic和消费者数量

        Map<String, List<KafkaStream<byte[], byte[]>>> streams= connector.createMessageStreams(topicConnect);

        List<KafkaStream<byte[], byte[]>> list = streams.get(topic);
        int cnt = (list==null)?0:list.size();
        System.out.println("Consumer1 list size="+cnt);
        for (KafkaStream<byte[], byte[]> stream : list) {
            new Thread(new Workers( connector,stream.iterator() )).start();
        }
    }

    static class Workers implements Runnable{

        private ConsumerIterator<byte[], byte[]> iterator ;

        private ConsumerConnector connector;

        public Workers(ConsumerConnector connector,ConsumerIterator<byte[], byte[]> iterator) {
            this.connector=connector;
            this.iterator = iterator;
        }

        public void run() {
            System.out.println("Workers run start,thread-->"+Thread.currentThread().getName());
            //线程一直不会退出，因为如果莫有消息，iterator.hasNext()会阻塞
            while ( iterator.hasNext() ){

                MessageAndMetadata<byte[], byte[]> next = iterator.next();
                String key = (next.key() == null)?"": new String(next.key());
                String message = (next.message() == null)?"": new String(next.message());

                //手动提交offset,当autocommit.enable=false时使用,
                //一定要在iterator.next()执行后在commitOffsets(),如果在之前提交，最后一次迭代的消息的offset将漏提交
                //connector.commitOffsets();

                System.out.println("Workers key:"+key+",msg:"+message+
                        ",partition="+next.partition()+",offset="+next.offset()+","+Thread.currentThread().getName() );

            }
        }
    }

}
