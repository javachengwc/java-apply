package com.kafka.demo;

import kafka.api.FetchRequestBuilder;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.cluster.Broker;
import kafka.common.TopicAndPartition;
import kafka.javaapi.*;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.message.MessageAndOffset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.*;

/**
 * kafka底层api实现的消费者1
 * 注意：
 *    1.必须自己实现当停止消费时如何持久化offset
 *    2.必须自己找到哪个broker是leader以便处理topic和分区
 *    3.必须自己处理leader变更
 * 使用阶段：
 *    1.找到那些broker是leader以便读取topic和partition
 *    2.自己决定哪个副本作为你的topic和分区
 *    3.建立自己需要请求并自定义获取你感兴趣的数据
 *    4.获取数据
 *    5.当leader变更时自己识别和恢复。
 */
public class SimpleConsumer1 {

    private static Logger logger = LoggerFactory.getLogger(SimpleConsumer1.class);

    public static void main(String args[]) throws Exception
    {
        String topic = "mult";
        int partition = 0;
        String brokers = "127.0.0.1:9092";

        PartitionMetadata metadata = findPartitionMetadata(brokers,topic,partition);
        if (metadata == null || metadata.leader() == null) {
            logger.info("SimpleConsumer1 findPartitionMetadata meta data or leader not found ");
            return;
        }
        // 获取leader
        Broker leadBroker = metadata.leader();
        int relicasCnt = (metadata.replicas()==null)?0:metadata.replicas().size();
        System.out.println("-------- broker replicas count="+relicasCnt);
        System.out.println(metadata.replicas());

        //获取offset
        long offset =getOffset(leadBroker,topic,partition);
        System.out.println("-------- broker offset="+offset);

        //获取数据
        long beginSet =offset-100;
        beginSet=(beginSet<0)?0:beginSet;
        Map<Long,String> data= fetchData(leadBroker,topic,partition,beginSet);
        int dataCnt = (data==null)?0:data.size();
        System.out.println("-------- data count="+dataCnt);
        if(dataCnt>0)
        {
            for(Long key:data.keySet())
            {
                System.out.println(key+"-->"+data.get(key));
            }
        }
    }

    //查找PartitionMetadata
    public static PartitionMetadata findPartitionMetadata(String brokers,String topic,int partition)
    {
        PartitionMetadata metadata = null;
        for (String ipPort : brokers.split(",")) {
            //无需要把所有的brokers列表加进去，目的只是为了获得metedata信息，故只要有broker可连接即可
            SimpleConsumer consumer = null;
            try {
                String[] ipPortArray = ipPort.split(":");
                consumer = new SimpleConsumer(ipPortArray[0], Integer.parseInt(ipPortArray[1]), 100000, 64 * 1024,"findPartitionMetadata");
                List<String> topics = new ArrayList<String>();
                topics.add(topic);
                TopicMetadataRequest req = new TopicMetadataRequest(topics);
                // 取meta信息
                TopicMetadataResponse resp = consumer.send(req);
                //获取topic的metedate信息
                List<TopicMetadata> metaData = resp.topicsMetadata();
                for (TopicMetadata item : metaData) {
                    for (PartitionMetadata part : item.partitionsMetadata()) {
                        //获取每个meta信息的分区信息
                        System.out.println("----topic="+item.topic()+",partition="+part.partitionId());
                        if (part.partitionId() == partition) {
                            metadata = part;
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("SimpleConsumer1 findPartitionMetadata error,",e);
            } finally {
                if (consumer != null)
                    consumer.close();
            }
        }
        return metadata;
    }

    //获取lastOffset
    public static long getOffset( Broker leadBroker ,String topic,int partition)
    {
        long whichTime = kafka.api.OffsetRequest.LatestTime();
        System.out.println("lastTime:"+whichTime);
        String clientName = "Client_" + topic + "_" + partition;
        SimpleConsumer consumer =null;
        try {
            consumer=new SimpleConsumer(leadBroker.host(), leadBroker.port(), 100000, 64 * 1024, clientName);
            TopicAndPartition topicAndPartition = new TopicAndPartition(topic, partition);
            Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();
            requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(whichTime, 1));
            OffsetRequest request = new OffsetRequest(requestInfo, kafka.api.OffsetRequest.CurrentVersion(), clientName);
            // 获取指定时间前有效的offset列表
            OffsetResponse response = consumer.getOffsetsBefore(request);
            if (response.hasError()) {
                logger.error("SimpleConsumer1 getOffset hasError," + response.errorCode(topic, partition));
                return 0;
            }
            //offset不一定是从0开始的
            long[] offsets = response.offsets(topic, partition);
            System.out.println("offset list:" + Arrays.toString(offsets));
            long offset = offsets[0];
            return offset;
        }finally
        {
            if(consumer!=null)
            {
                consumer.close();
            }
        }
    }

    //获取数据
    public  static Map<Long,String> fetchData(Broker leadBroker,String topic,int partition,long offset)
    {
        Map<Long,String> resultMap = new HashMap<Long,String>();
        // 读数据
        String clientName =  "ClientFetch_" + topic + "_" + partition;
        SimpleConsumer consumer=null;
        try {
            consumer=new SimpleConsumer(leadBroker.host(), leadBroker.port(), 100000, 64 * 1024, clientName);
            // 注意不要调用里面的replicaId()方法，这是内部使用的。
            kafka.api.FetchRequest req = new FetchRequestBuilder().clientId(clientName).addFetch(topic, partition, offset, 100000).build();
            FetchResponse fetchResponse = consumer.fetch(req);
            if (fetchResponse.hasError()) {
                // 出错处理。可以根据出错的类型进行判断，如code == ErrorMapping.OffsetOutOfRangeCode()表示拿到的offset错误
                // 一般出错处理可以再次拿offset,或重新找leader，重新建立consumer。可以将上面的操作都封装成方法。再在该循环来进行消费
                // 当然，在取所有leader的同时可以用metadata.replicas()更新最新的节点信息。
                // 另外zookeeper可能不会立即检测到有节点挂掉，故如果发现老的leader和新的leader一样，可能是leader根本没挂，也可能是zookeeper还没检测到，总之需要等等。
                short code = fetchResponse.errorCode(topic, partition);
                logger.error("SimpleConsumer1 fetch date hasError,code=" + code);
                return resultMap;
            }
            for (MessageAndOffset messageAndOffset : fetchResponse.messageSet(topic, partition)) {
                long curOffset = messageAndOffset.offset();
                //下面这个检测有必要，因为当消息是压缩的时候，通过fetch获取到的是一个整块数据。
                //块中解压后不一定第一个消息就是offset所指定的。就是说存在再次取到已读过的消息。
                if (curOffset < offset) {
                    logger.error("SimpleConsumer1 fetch date  old offset:" + curOffset + " Expecting: " + offset);
                    continue;
                }
                // 可以通过当前消息知道下一条消息的offset是多少
                offset = messageAndOffset.nextOffset();
                ByteBuffer payload = messageAndOffset.message().payload();
                byte[] bytes = new byte[payload.limit()];
                payload.get(bytes);
                String msg = new String(bytes, "UTF-8");
                System.out.println("---------fetch data offset=" + curOffset + ",msg=" + msg);
                resultMap.put(curOffset,msg);
            }
            return resultMap;
        }catch(Exception e)
        {
            logger.error("SimpleConsumer1 fetchData error,",e);
            return resultMap;
        }finally
        {
            if (consumer != null)
                consumer.close();
        }
    }
}
