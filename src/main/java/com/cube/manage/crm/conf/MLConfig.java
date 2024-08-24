package com.cube.manage.crm.conf;

import org.apache.spark.sql.SparkSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MLConfig {

    @Bean
    public SparkSession sparkSessionTesting(){
        return SparkSession.builder().appName("RecommendationTesting").getOrCreate();
    }

}
