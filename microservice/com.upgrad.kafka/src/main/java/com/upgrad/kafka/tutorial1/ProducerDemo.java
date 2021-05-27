package com.upgrad.kafka.tutorial1;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class ProducerDemo {
    public static void main(String[] args) {
        //        System.out.println("Hello World");
        String bootstrapservers = "127.0.0.1:9092";
       //Create Producer Properties
        Properties properties = new Properties();

//        Old version of setting properties
//        properties.setProperty("bootstrap.servers", bootstrapservers);
//        properties.setProperty("key.serializer", StringSerializer.class.getName());
//        properties.setProperty("value.serializer",StringSerializer.class.getName());

          // Setting the configurations through
          properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapservers);
          properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
          properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

         // Creating the Producer
        KafkaProducer<String,String> producer = new KafkaProducer<String,String>(properties);

        // Creating a ProducerRecord
        ProducerRecord<String,String> record=  new ProducerRecord<String,String>("first_topic", "hello world");
        // Data is send in background asynchronously so we dont see the data being sent  but its not
        // begin received by the consumer.
        producer.send(record);
        //We need to flush and close inorder to send it now.
        producer.flush();
        producer.close();

    }



}
