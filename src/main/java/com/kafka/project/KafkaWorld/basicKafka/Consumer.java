package com.kafka.project.KafkaWorld.basicKafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {

    private Logger logger = LoggerFactory.getLogger(Consumer.class);

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ConsumerRecord<String, String> consumerRecord) {
        logger.info(consumerRecord.partition() + "," + consumerRecord.offset() + "," + consumerRecord.key() + "," + consumerRecord.value());
    }
}
