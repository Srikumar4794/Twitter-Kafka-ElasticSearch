package com.kafka.project.KafkaWorld.twitter;

import com.kafka.project.KafkaWorld.config.ElasticSearchConfig;
import com.kafka.project.KafkaWorld.basicKafka.Consumer;
import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Data
public class TwitterConsumer {
    private Logger logger = LoggerFactory.getLogger(Consumer.class);

    private final ElasticSearchConfig elasticSearchConfig;

    @KafkaListener(topics = "${app.kafka.twitter-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(ConsumerRecord<String, String> consumerRecord){
        String tweetContent = consumerRecord.value();
        String indexId = consumerRecord.topic() + "_" + consumerRecord.partition() + "_" + consumerRecord.offset();
        logger.info(indexId);
        writeToElasticSearch(indexId, tweetContent);
    }

    private void writeToElasticSearch(String indexId, String tweetContent) {
        RestHighLevelClient restHighLevelClient = elasticSearchConfig.createClient();
        IndexRequest indexRequest = new IndexRequest("twitter_2", "tweets", indexId).source(tweetContent, XContentType.JSON);
        Logger logger = LoggerFactory.getLogger(TwitterConsumer.class);

        try {
            logger.info(restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT).getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
