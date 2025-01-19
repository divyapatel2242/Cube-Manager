//package com.cube.manage.crm.conf;
//
//import org.elasticsearch.client.RestHighLevelClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.client.ClientConfiguration;
//import org.springframework.data.elasticsearch.client.RestClients;
//import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
//import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories;
//
//@Configuration
//@EnableReactiveElasticsearchRepositories(basePackages = "com.cube.manage.crm.esRepo")
//@ComponentScan(basePackages = {"com.cube.manage.crm"})
//public class EsConfig extends AbstractElasticsearchConfiguration {
//
//    @Value("${elasticsearch.url}")
//    public String elasticsearchUrl;
//
//    @Bean
//    @Override
//    public RestHighLevelClient elasticsearchClient() {
//        final ClientConfiguration config = ClientConfiguration.builder().connectedTo(elasticsearchUrl).withSocketTimeout(60000).build();
//        return RestClients.create(config).rest();
//    }
//
//}
