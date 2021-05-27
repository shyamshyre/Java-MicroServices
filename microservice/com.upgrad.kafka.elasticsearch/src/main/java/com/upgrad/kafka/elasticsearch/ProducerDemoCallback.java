package com.upgrad.kafka.elasticsearch;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Properties;

public class ProducerDemoCallback {

    public static void main(String[] args) {
          final Logger logger = LoggerFactory.getLogger(ProducerDemoCallback.class);
//        System.out.println("Hello World");
          String boot_strap_servers = "127.0.0.1:9092";
         //Create Producer Properties
          Properties properties = new Properties();

//        Old version of setting properties
//        properties.setProperty("bootstrap.servers", boot_strap_servers);
//        properties.setProperty("key.serializer", StringSerializer.class.getName());
//        properties.setProperty("value.serializer",StringSerializer.class.getName());

          // Setting the configurations through
          properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, boot_strap_servers);
          properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());
          properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

          //High Throughput ProducerSettings
          properties.setProperty(ProducerConfig.COMPRESSION_TYPE_CONFIG,"snappy");
          properties.setProperty(ProducerConfig.LINGER_MS_CONFIG,"20");
          properties.setProperty(ProducerConfig.BATCH_SIZE_CONFIG,Integer.toString(32*1024));

          // Safe Producer
          properties.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG,"true");
          properties.setProperty(ProducerConfig.ACKS_CONFIG,"all");
          properties.setProperty(ProducerConfig.RETRIES_CONFIG,Integer.toString(Integer.MAX_VALUE));
          properties.setProperty(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION,"5");

         // Creating the Producer
          KafkaProducer<String,String> producer = new KafkaProducer<String,String>(properties);
            for (int i=0;i<10000;i++) {
                String topic= "weatherstats";
                final String key= "id"+Integer.toString(i);
                String value="hello world"+Integer.toString(i);

                // Creating a ProducerRecord
                ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic,key,value);
                // Data is send in background asynchronously so we dont see the data being sent  but its not
                // begin received by the consumer.
                producer.send(record, new Callback() {
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                        // executes every time a record is successfully sent or an exception is thrown.
                        logger.info(key);
                        if (e == null) {
                            logger.info("Received new Metadata" +
                                    "Topic" + recordMetadata.topic() + "\n" +
                                    "Partition" + recordMetadata.partition() + "\n" +
                                    "Offset" + recordMetadata.offset() + "\n" +
                                    "TimeStamp" + recordMetadata.timestamp());
                        } else {
                            logger.error("Error during execution", e);
                        }
                    }
                });
                //We need to flush and close inorder to send it now.

            }
        producer.flush();
        producer.close();
    }
}
