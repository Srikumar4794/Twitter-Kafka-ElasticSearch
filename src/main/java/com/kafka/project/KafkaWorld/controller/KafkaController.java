package com.kafka.project.KafkaWorld.controller;

import com.kafka.project.KafkaWorld.basicKafka.Producer;
import com.kafka.project.KafkaWorld.twitter.TwitterClient;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Data
public class KafkaController {
    private final Producer producer;
    private final TwitterClient twitterClient;

    @GetMapping(path = "/send-message")
    public void sendMessage() {
        producer.produceMessage();
    }

    @GetMapping(path = "/get-live-tweets/{searchStr}")
    public void sendTweets(@PathVariable String searchStr){
        twitterClient.run(searchStr);
    }
}
