package com.upgrad.kafka.tutorial1;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class NoConsumerGrouping {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(NoConsumerGrouping.class.getName());
        String boot_strap_servers = "localhost:9092";
        String group_id="my-fourth-application";
        String topic="first_topic";
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,boot_strap_servers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, group_id);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        KafkaConsumer <String , String> kafkaConsumer = new KafkaConsumer(properties);
//        kafkaConsumer.subscribe(Collections.singleton());
        kafkaConsumer.subscribe(Arrays.asList(topic));
        while(true){
            ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord record: consumerRecords){
                logger.info("Partition "+record.partition()+" "+
                            "Topic "+record.topic()+" "+
                            "Offset "+record.offset()+" "+
                            "Key "+record.key()+" "+
                            "Value "+record.value());
            }
        }

    }
}
