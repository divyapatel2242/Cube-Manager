package com.cube.manage.crm.conf;

import org.apache.spark.sql.SparkSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * @auther
 * Divya Patel
 * */

@Configuration
public class MLConfig {

    // Configure Spark Session Properties

    @Bean
    public SparkSession sparkSessionTesting(){
        System.setProperty("spark.driver.extraJavaOptions", "--add-opens java.base/sun.nio.ch=ALL-UNNAMED");
        System.setProperty("spark.executor.extraJavaOptions", "--add-opens java.base/sun.nio.ch=ALL-UNNAMED");
        System.setProperty("hadoop.home.dir", "C:\\hadoop");
        return SparkSession.builder().appName("RecommendationTesting").master("local[1]").getOrCreate();
    }

}
