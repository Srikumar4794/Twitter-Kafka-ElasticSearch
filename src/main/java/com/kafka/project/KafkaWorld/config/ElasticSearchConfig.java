package com.kafka.project.KafkaWorld.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

    @Value("${app.elasticSearch.host}")
    private String hostName;

    @Value("${app.elasticSearch.user}")
    private String userName;

    @Value("${app.elasticSearch.password}")
    private String passWord;

    @Value("${app.elasticSearch.port}")
    private Integer port;

    @Value("${app.elasticSearch.scheme}")
    private String scheme;

    @Bean
    public RestHighLevelClient createInstance() {
        return createClient();
    }

    public RestHighLevelClient createClient() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, passWord));

        RestClientBuilder builder = RestClient.builder(new HttpHost(hostName, port, scheme)).setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }
        });

        return new RestHighLevelClient(builder);
    }
}
