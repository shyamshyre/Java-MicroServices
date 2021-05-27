package com.upgrad.kafka.elasticsearch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.prominence.openweathermap.api.OpenWeatherMapClient;
import com.github.prominence.openweathermap.api.enums.Language;
import com.github.prominence.openweathermap.api.enums.UnitSystem;
import org.apache.http.HttpHost;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.time.Duration;
import java.util.*;


public class ElasticSearchConsumer {
    // The config parameters for creating the elasticsearch connection
    // Elastic Search configuration  parameters.
    private static final String HOST = "localhost";
    private static final int PORT_ONE = 9200;
    private static final int PORT_TWO = 9201;
    private static final String SCHEME = "http";
    private static RestHighLevelClient restHighLevelClient;
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static final String INDEX = "persondata";
    private static final String TYPE = "person";
    //Configuring the Logger.
    Logger logger = LoggerFactory.getLogger(ElasticSearchConsumer.class.getName());
    // Initializing the OpenWeather Client
    OpenWeatherMapClient openWeatherClient = new OpenWeatherMapClient("6a0903114d7dc2102572933cb25a4d3e");
    /**
     * Implemented Singleton pattern here
     * so that there is just one connection at a time.
     * @return RestHighLevelClient
     */
    private static synchronized RestHighLevelClient makeConnection() {

        if(restHighLevelClient == null) {
            restHighLevelClient = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost(HOST, PORT_ONE, SCHEME),
                            new HttpHost(HOST, PORT_TWO, SCHEME)));
        }
        return restHighLevelClient;
    }

    private static synchronized void closeConnection() throws IOException {
        restHighLevelClient.close();
        restHighLevelClient = null;
    }

    private static Person insertPerson(Person person){
        person.setPersonId(UUID.randomUUID().toString());
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("personId", person.getPersonId());
        dataMap.put("name", person.getName());
        IndexRequest indexRequest = new IndexRequest(INDEX, TYPE, person.getPersonId())
                .source(dataMap);
        try {
            IndexResponse response = restHighLevelClient.index(indexRequest);
            String id= response.getId();
            System.out.println(id);

        } catch(ElasticsearchException e) {
            e.getDetailedMessage();
        } catch (java.io.IOException ex){
            ex.getLocalizedMessage();
        }
        return person;
    }

    private static Person getPersonById(String id){
        GetRequest getPersonRequest = new GetRequest(INDEX, TYPE, id);
        GetResponse getResponse = null;
        try {
            getResponse = restHighLevelClient.get(getPersonRequest);
        } catch (java.io.IOException e){
            e.getLocalizedMessage();
        }
        return getResponse != null ?
                objectMapper.convertValue(getResponse.getSourceAsMap(), Person.class) : null;
    }

    private static Person updatePersonById(String id, Person person){
        UpdateRequest updateRequest = new UpdateRequest(INDEX, TYPE, id)
                .fetchSource(true);    // Fetch Object after its update
        try {
            String personJson = objectMapper.writeValueAsString(person);
            updateRequest.doc(personJson, XContentType.JSON);
            UpdateResponse updateResponse = restHighLevelClient.update(updateRequest);
            return objectMapper.convertValue(updateResponse.getGetResult().sourceAsMap(), Person.class);
        }catch (JsonProcessingException e){
            e.getMessage();
        } catch (java.io.IOException e){
            e.getLocalizedMessage();
        }
        System.out.println("Unable to update person");
        return null;
    }

    private static void deletePersonById(String id) {
        DeleteRequest deleteRequest = new DeleteRequest(INDEX, TYPE, id);
        try {
            DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest);
        } catch (java.io.IOException e){
            e.getLocalizedMessage();
        }
    }

//    Kafka Producer configuration and sending data to kafka topic
    private void writeDataToKafka(String weather_data){
        //Configuration parameters for Kafka.
        String boot_strap_servers = "127.0.0.1:9092";
        String topic= "currentweather";

        Properties properties = new Properties();
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
        for (int i=0;i<5;i++) {

            final String key= "id"+Integer.toString(i);
            String value="hello world"+Integer.toString(i);

            // Creating a ProducerRecord
            ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic,key,weather_data);
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

    private void readDataFromServer(){
        String boot_strap_servers = "localhost:9092";
        String group_id="weather-group";
        String topic="currentweather";
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,boot_strap_servers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, group_id);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,"earliest");
        KafkaConsumer<String , String> kafkaConsumer = new KafkaConsumer(properties);
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



    private String  fetchDataFromServer(String city) {
        final String weatherJson = openWeatherClient
                .currentWeather()
                .single()
                .byCityName(city)
                .language(Language.ENGLISH)
                .unitSystem(UnitSystem.IMPERIAL)
                .retrieve()
                .asJSON();
        return weatherJson;
    }

    public static void main(String[] args) throws IOException {

        ElasticSearchConsumer elasticSearchConsumer = new ElasticSearchConsumer();
        String weatherData="Mumbai";
        int j=0;
//        makeConnection();
//
//        System.out.println("Inserting a new Person with name Shubham...");
//        Person person = new Person();
//        person.setName("Shubham");
//        person = insertPerson(person);
//        System.out.println("Person inserted --> " + person);
          String[] cities= new String[]{"Hyderabad","Lucknow","Pune","Chennai","Kakinada","Tokyo"};
          for (j=0;j<cities.length;j++){
                 weatherData = elasticSearchConsumer.fetchDataFromServer(cities[j]);
            }
            elasticSearchConsumer.writeDataToKafka(weatherData);
           // elasticSearchConsumer.readDataFromServer();

//        System.out.println("Changing name to `Shubham Aggarwal`...");
//        person.setName("Shubham Aggarwal");
//        updatePersonById(person.getPersonId(), person);
//        System.out.println("Person updated  --> " + person);
//
//        System.out.println("Getting Shubham...");
//        Person personFromDB = getPersonById(person.getPersonId());
//        System.out.println("Person from DB  --> " + personFromDB);

//        System.out.println("Deleting Shubham...");
//        deletePersonById(personFromDB.getPersonId());
//        System.out.println("Person Deleted");

//        closeConnection();
    }
}
