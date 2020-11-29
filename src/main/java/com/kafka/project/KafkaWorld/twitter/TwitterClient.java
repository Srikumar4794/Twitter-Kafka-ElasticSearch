package com.kafka.project.KafkaWorld.twitter;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
@Data
public class TwitterClient {

    @Value("${app.twitter-creds.consumer-key}")
    private String consumerKey;

    @Value("${app.twitter-creds.consumer-secret}")
    private String consumerSecret;

    @Value("${app.twitter-creds.access-token}")
    private String token;

    @Value("${app.twitter-creds.access-token-secret}")
    private String secret;

    private Logger logger = LoggerFactory.getLogger(TwitterClient.class.getName());

    private final TwitterProducer twitterProducer;

    public void run(String searchStr){
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>(1000);
        Client hosebirdClient = createTwitterClient(msgQueue, searchStr);
        hosebirdClient.connect();

        while (!hosebirdClient.isDone()){
            String msg = null;

            try{
                msg = msgQueue.poll(5, TimeUnit.SECONDS);
            }
            catch(InterruptedException exception){
                exception.printStackTrace();
                hosebirdClient.stop();
            }

            if(msg != null)
                twitterProducer.publishTwitterData(msg);
        }
        logger.info("End of app");
    }

    public Client createTwitterClient(BlockingQueue<String> msgQueue, String searchStr){
        /** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
        // Optional: set up some followings and track terms
        List<String> terms = Lists.newArrayList(searchStr);

        //get tweets based on a search term.
        hosebirdEndpoint.trackTerms(terms);

        //get tweets from userIds.
        // hosebirdEndpoint.followings(userIds);

        // These secrets should be read from a config file
        Authentication hosebirdAuth = new OAuth1(consumerKey, consumerSecret, token, secret);

        ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client-01")                              // optional: mainly for the logs
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue));

        return builder.build();


    }
}
