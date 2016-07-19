package com.kafka.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kafka.KafkaConstant;
import com.kafka.mode.*;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.cluster.Broker;
import kafka.common.TopicAndPartition;
import kafka.javaapi.*;
import kafka.javaapi.consumer.SimpleConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * kafka消费端
 */
public class KafkaClient {

    private static Logger logger = LoggerFactory.getLogger(KafkaClient.class);

    private static String clientName = String.valueOf(new Date().getTime());

    private SimpleConsumer simpleConsumer;

    private int timeout;

    private int bufferSize;

    public KafkaClient() {

    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
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
        final List<String> topics = ZookeeperClient.getInstance().getChildren(KafkaConstant.TOPIC_PATH);
        return getTopicDetail(topics);
    }

    public List<KafkaConsumerGroup> getConsumerGroup() throws Exception {
        List<String> groups =  ZookeeperClient.getInstance().getChildren(KafkaConstant.CONSUMER_PATH);
       List<KafkaConsumerGroup> kafkaConsumerGroups = new ArrayList<KafkaConsumerGroup>();
        for (String group : groups) {
            List<String> topics =  ZookeeperClient.getInstance().getChildren(KafkaConstant.CONSUMER_PATH + "/" + group + "/offsets");
            KafkaConsumerGroup kafkaConsumerGroup = new KafkaConsumerGroup();
            ArrayList<KafkaTopicOffset> kafkaTopicOffsets = new ArrayList<KafkaTopicOffset>();
            for (String topic : topics) {
                KafkaTopicOffset kafkaTopicOffset = new KafkaTopicOffset();
                ArrayList<KafkaPartitionOffset> kafkaPartitionOffsets = new ArrayList<KafkaPartitionOffset>();
                List<String> partitions =  ZookeeperClient.getInstance().getChildren(KafkaConstant.CONSUMER_PATH + "/" + group + "/offsets/" + topic);
                for (String partition : partitions) {
                    KafkaPartitionOffset kafkaPartitionOffset = new KafkaPartitionOffset();
                    String offset = ZookeeperClient.getInstance().getData(KafkaConstant.CONSUMER_PATH + "/" + group + "/offsets/" + topic + "/"+ partition);
                    kafkaPartitionOffset.setId(Integer.parseInt(partition));
                    kafkaPartitionOffset.setLatest(Integer.parseInt(offset));
                    kafkaPartitionOffsets.add(kafkaPartitionOffset);
                }
                kafkaTopicOffset.setName(topic);
                kafkaTopicOffset.setKafkaPartitionOffsets(kafkaPartitionOffsets);
                kafkaTopicOffsets.add(kafkaTopicOffset);
            }
            kafkaConsumerGroup.setName(group);
            kafkaConsumerGroup.setOwners(kafkaTopicOffsets);
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

    public List<KafkaTopicOffset> getEarliestOffset(List<String> topic) {
        List<KafkaTopic> topicDetail = getTopicDetail(topic);
        List<KafkaTopicOffset> kafkaTopicOffsets = new ArrayList<KafkaTopicOffset>();
        for (KafkaTopic kafkaTopic : topicDetail) {
            KafkaTopicOffset kafkaTopicOffset = new KafkaTopicOffset();
            ArrayList<KafkaPartitionOffset> kafkaPartitionOffsets = new ArrayList<KafkaPartitionOffset>();
            for (KafkaPartition kafkaPartition : kafkaTopic.getPartitions()) {
                KafkaPartitionOffset kafkaPartitionOffset = new KafkaPartitionOffset();
                kafkaPartitionOffset.setEarliest(getEarliestOffset(kafkaTopic.getName(), kafkaPartition.getId()));
                kafkaPartitionOffset.setLatest(getLatestOffset(kafkaTopic.getName(), kafkaPartition.getId()));
                kafkaPartitionOffsets.add(kafkaPartitionOffset);
            }
            kafkaTopicOffset.setName(kafkaTopic.getName());
            kafkaTopicOffset.setKafkaPartitionOffsets(kafkaPartitionOffsets);
            kafkaTopicOffsets.add(kafkaTopicOffset);
        }
        return kafkaTopicOffsets;
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
