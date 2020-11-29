package com.kafka.project.KafkaWorld.twitter;

import lombok.Data;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Data
public class TwitterProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private Logger logger = LoggerFactory.getLogger(TwitterProducer.class.getName());

    @Value("${app.kafka.twitter-topic}")
    private String TOPIC;

    public void publishTwitterData(String msg){
        ProducerRecord<String, String> producerRecord = new ProducerRecord(TOPIC, msg);
        kafkaTemplate.send(producerRecord);
    }
}
