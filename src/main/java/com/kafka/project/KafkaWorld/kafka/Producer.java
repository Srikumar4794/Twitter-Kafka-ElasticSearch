package com.kafka.project.KafkaWorld.kafka;

import lombok.Data;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Data
public class Producer {
    private Logger logger = LoggerFactory.getLogger(Producer.class);

    @Value("${app.kafka.topic}")
    private String TOPIC;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void produceMessage(){
        /*
         id 9,6,4  -> Partition-0
         id 8,7,5,3,2,1   -> Partition-1
         */
        logger.info("Producer properties: " + kafkaTemplate.getProducerFactory().getConfigurationProperties());
        for(int i=1; i<10; i++){
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TOPIC, "id_" + i, "Hello world" + i);
            kafkaTemplate.send(producerRecord);
        }
    }

}
