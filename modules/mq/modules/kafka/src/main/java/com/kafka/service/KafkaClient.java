package com.kafka.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kafka.KafkaConstant;
import com.kafka.mode.*;
import com.util.lang.RunTimeUtil;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.cluster.Broker;
import kafka.common.TopicAndPartition;
import kafka.javaapi.*;
import kafka.javaapi.consumer.SimpleConsumer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * kafka消费端
 */
public class KafkaClient {

    private static Logger logger = LoggerFactory.getLogger(KafkaClient.class);

    private static String clientName = String.valueOf(new Date().getTime());

    private static KafkaClient inst=null;

    private SimpleConsumer simpleConsumer;

    private int timeout;

    private int bufferSize;

    public static KafkaClient getInstance()
    {
        if(inst==null)
        {
            synchronized (KafkaClient.class)
            {
                if(inst==null)
                {
                    inst=new KafkaClient();
                    inst.init(6000,10000);
                }
            }
        }
        return inst;
    }

    public KafkaClient() {

    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public void init(int timeout,int bufferSize)
    {
        this.timeout=timeout;
        this.bufferSize=bufferSize;
        createSimpleConsumer();
        RunTimeUtil.addShutdownHook(new Runnable() {
            public void run() {
                KafkaClient.getInstance().destory();
            }
        });
    }

    private void createSimpleConsumer() {
        try {
            List<KafkaBroker> brokers = getBrokers();
            if (brokers.isEmpty()) {
                logger.error("KafkaClient createSimpleConsumer brokers is empty ");
                throw new RuntimeException("KafkaClient brokers is empty ");
            }
            KafkaBroker broker = brokers.get(0);
            simpleConsumer = new SimpleConsumer(
                    broker.getHost(),
                    broker.getPort(),
                    timeout,
                    bufferSize,
                    String.valueOf(new Date().getTime()));
        } catch (Exception e) {
            logger.error("KafkaClient create simpleConsumer error,",e);
        }
    }

    public void destory() {

        simpleConsumer.close();
    }

    public List<KafkaBroker> getBrokers() throws Exception {
        try {
            List<String> brokerIds = ZookeeperClient.getInstance().getChildren(KafkaConstant.BROKER_IDS_PATH);
            List<KafkaBroker> kafkaBrokers = new ArrayList<KafkaBroker>();
            for (String brokerId : brokerIds) {
                //类似{"jmx_port":-1,"timestamp":"1468916762306","host":"tuan800-PC","version":1,"port":9092}
                String broker = ZookeeperClient.getInstance().getData(KafkaConstant.BROKER_IDS_PATH + "/" + brokerId);
                JSONObject json = JSON.parseObject(broker);
                KafkaBroker kafkaBroker = new KafkaBroker();
                kafkaBroker.setPort(json.getIntValue("port"));
                kafkaBroker.setHost(json.getString("host"));
                kafkaBroker.setId(Integer.parseInt(brokerId));
                kafkaBrokers.add(kafkaBroker);
            }
            return kafkaBrokers;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<KafkaTopic> getTopics() throws Exception {
        List<String> topics = ZookeeperClient.getInstance().getChildren(KafkaConstant.TOPIC_PATH);
        return getTopicDetail(topics);
    }

    public List<KafkaConsumerGroup> getConsumerGroup() throws Exception {
        List<String> groups =  ZookeeperClient.getInstance().getChildren(KafkaConstant.CONSUMER_PATH);
        if(groups==null)
        {
            return null;
        }

        List<KafkaConsumerGroup> kafkaConsumerGroups = new ArrayList<KafkaConsumerGroup>();
        for (String group : groups) {
            logger.info("KafkaClient group "+group);
            KafkaConsumerGroup kafkaConsumerGroup = new KafkaConsumerGroup();
            kafkaConsumerGroup.setName(group);

            List<String> topics =  ZookeeperClient.getInstance().getChildren(KafkaConstant.CONSUMER_PATH + "/" + group + "/offsets");
            int topicCnt =(topics==null)?0:topics.size();
            kafkaConsumerGroup.setTopicCnt(topicCnt);

            List<KafkaTopicOffset> kafkaTopicOffsetList = new ArrayList<KafkaTopicOffset>();
            if(topicCnt>0) {
                for (String topic : topics) {
                    KafkaTopicOffset kafkaTopicOffset = new KafkaTopicOffset();
                    List<KafkaPartitionOffset> kafkaPartitionOffsetList = new ArrayList<KafkaPartitionOffset>();
                    List<String> partitions = ZookeeperClient.getInstance().getChildren(KafkaConstant.CONSUMER_PATH + "/" + group + "/offsets/" + topic);
                    for (String partition : partitions) {
                        KafkaPartitionOffset kafkaPartitionOffset = new KafkaPartitionOffset();
                        String offset = ZookeeperClient.getInstance().getData(KafkaConstant.CONSUMER_PATH + "/" + group + "/offsets/" + topic + "/" + partition);
                        kafkaPartitionOffset.setId(Integer.parseInt(partition));
                        kafkaPartitionOffset.setLatest(Integer.parseInt(offset));
                        kafkaPartitionOffsetList.add(kafkaPartitionOffset);
                    }
                    kafkaTopicOffset.setName(topic);
                    kafkaTopicOffset.setKafkaPartitionOffsets(kafkaPartitionOffsetList);
                    kafkaTopicOffsetList.add(kafkaTopicOffset);
                }
            }
            kafkaConsumerGroup.setOwners(kafkaTopicOffsetList);
            kafkaConsumerGroups.add(kafkaConsumerGroup);
        }
        return kafkaConsumerGroups;
    }

    public List<KafkaTopic> getTopicDetail(List<String> topics) {
        TopicMetadataRequest topicMetadataRequest = new TopicMetadataRequest(topics);
        List<KafkaTopic> kafkaTopics = new ArrayList<KafkaTopic>();
        TopicMetadataResponse topicMetadataResponse = simpleConsumer.send(topicMetadataRequest);
        List<TopicMetadata> topicMetadatas = topicMetadataResponse.topicsMetadata();
        for (TopicMetadata topicMetadata : topicMetadatas) {
            KafkaTopic kafkaTopic = new KafkaTopic();
            ArrayList<KafkaPartition> kafkaPartitions = new ArrayList<KafkaPartition>();
            kafkaTopic.setName(topicMetadata.topic());
            Iterator<PartitionMetadata> metadataIterator = topicMetadata.partitionsMetadata().iterator();
            while (metadataIterator.hasNext()) {
                KafkaPartition kafkaPartition = new KafkaPartition();
                PartitionMetadata partitionMetadata = metadataIterator.next();
                kafkaPartition.setId(partitionMetadata.partitionId());
                kafkaPartition.setLeader(convertBroker(partitionMetadata.leader()));
                kafkaPartition.setReplicas(batchConvertBroker(partitionMetadata.replicas()));
                kafkaPartitions.add(kafkaPartition);
            }
            kafkaTopic.setPartitions(kafkaPartitions);
            kafkaTopics.add(kafkaTopic);
        }
        return kafkaTopics;
    }

    public Map<Integer, KafkaBroker> getTopicLeaderInfo(String topic) {
        KafkaTopic topicDetail = getTopicDetail(Collections.singletonList(topic)).get(0);
        List<KafkaPartition> partitions = topicDetail.getPartitions();
        Map<Integer, KafkaBroker> leaders = new HashMap<Integer, KafkaBroker>();
        for (KafkaPartition kafkaPartition : partitions) {
            leaders.put(kafkaPartition.getId(), kafkaPartition.getLeader());
        }
        return leaders;
    }

    public KafkaBroker getLeaderInfoByTopicAndPartition(String topic, int partition) {
        KafkaTopic topicDetail = getTopicDetail(Collections.singletonList(topic)).get(0);
        for (KafkaPartition kafkaPartition : topicDetail.getPartitions()) {
            if (kafkaPartition.getId() == partition) {
                return kafkaPartition.getLeader();
            }
        }
        return new KafkaBroker();
    }

    public long getEarliestOffset(String topic, int partition) {
        return execOffsetRequest(topic, partition, kafka.api.OffsetRequest.EarliestTime());
    }

    public long getLatestOffset(String topic, int partition) {
        return execOffsetRequest(topic, partition, kafka.api.OffsetRequest.LatestTime());
    }

    private long execOffsetRequest(String topic, int partition, long time) {
        KafkaBroker leader = getLeaderInfoByTopicAndPartition(topic, partition);
        SimpleConsumer simpleConsumer = createSimpleConsumer(leader.getHost(), leader.getPort());
        Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();
        requestInfo.put(new TopicAndPartition(topic, partition), new PartitionOffsetRequestInfo(time, 1));
        OffsetRequest offsetRequest = new OffsetRequest(requestInfo,kafka.api.OffsetRequest.CurrentVersion(), simpleConsumer.clientId());
        OffsetResponse offsetResponse = simpleConsumer.getOffsetsBefore(offsetRequest);
        simpleConsumer.close();
        if (offsetResponse.hasError()) {
            logger.info("KafkaClient execOffsetRequest has error, " + offsetResponse.errorCode(topic, partition));
            return 0;
        }
        return offsetResponse.offsets(topic, partition)[0];
    }

    //获取某consumer group的offset
    public long getOffset(String topic,int partition,String group) throws Exception
    {
        String offsetStr = ZookeeperClient.getInstance().getData(KafkaConstant.CONSUMER_PATH + "/" + group + "/offsets/" + topic + "/" + partition);
        if(StringUtils.isBlank(offsetStr))
        {
            return getEarliestOffset(topic,partition);
        }
        return  Long.parseLong(offsetStr);
    }

    //重置某consumer group的offset
    public long setOffset(String topic,int partition,String group,long offset) throws Exception
    {
        String path =KafkaConstant.CONSUMER_PATH + "/" + group + "/offsets/" + topic + "/" + partition;
        ZookeeperClient.getInstance().writeData(path,""+offset);
        return getOffset(topic,partition,group);
    }

    public KafkaBroker convertBroker(Broker broker){
        KafkaBroker kafkaBroker = new KafkaBroker();
        kafkaBroker.setId(broker.id());
        kafkaBroker.setHost(broker.host());
        kafkaBroker.setPort(broker.port());
        return kafkaBroker;
    }

    public List<KafkaBroker> batchConvertBroker(List<Broker> brokers){
        List<KafkaBroker> kafkaBrokers = new ArrayList<KafkaBroker>();
        for(Broker broker : brokers){
            kafkaBrokers.add(convertBroker(broker));
        }
        return kafkaBrokers;
    }

    public SimpleConsumer createSimpleConsumer(String host, int port) {
        return new SimpleConsumer(host, port, timeout, bufferSize, clientName);
    }
}
