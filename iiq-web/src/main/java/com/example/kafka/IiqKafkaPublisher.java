package com.example.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import sailpoint.api.Publisher;
import sailpoint.api.SailPointContext;
import sailpoint.integration.publishers.PublisherConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class IiqKafkaPublisher extends Publisher {


    private KafkaProducer<String, String> producer;
    private String topic;
    private List<String> tags = Arrays.asList("kafka", "queue", "event");

    public IiqKafkaPublisher() {
        super();
    }

    @Override
    public void initialize(PublisherConfiguration config, SailPointContext context) throws Exception {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("acks", "all");  // optional, best for durability

        // Add optional configs if needed
//        String additionalConfig = config.getString("kafka.additional.props");
//        if (additionalConfig != null) {
//            for (String line : additionalConfig.split(";")) {
//                String[] kv = line.split("=");
//                if (kv.length == 2) {
//                    props.setProperty(kv[0].trim(), kv[1].trim());
//                }
//            }
//        }

        this.producer = new KafkaProducer<>(props);
        this.topic = "test-topic";
    }

    @Override
    public void publish(String message, SailPointContext context) throws Exception {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);
        RecordMetadata metadata = producer.send(record).get(); // synchronous send
        System.out.println("Published to Kafka: " + metadata.toString());
    }

    @Override
    public List<String> getTags() {
        return tags;
    }

    @Override
    public void close() throws Exception {
        if (producer != null) {
            producer.close();
        }
    }
}
