package com.cube.manage.crm.conf;

import com.cube.manage.crm.dbutil.JDBCAccess;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.StringIndexerModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
 * @auther
 * Divya Patel
 * */

@Configuration
public class MLConfig {

    @Inject
    JDBCAccess jdbcAccess;

    // Configure Spark Session Properties

    @Bean
    public SparkSession sparkSessionTesting(){
        System.setProperty("spark.driver.extraJavaOptions", "--add-opens java.base/sun.nio.ch=ALL-UNNAMED");
        System.setProperty("spark.executor.extraJavaOptions", "--add-opens java.base/sun.nio.ch=ALL-UNNAMED");
        System.setProperty("hadoop.home.dir", "C:\\hadoop");
        return SparkSession.builder().appName("RecommendationTesting").master("local[1]").getOrCreate();
    }

    @Bean
    public StringIndexerModel brandIndexerModel(SparkSession sparkSession) {
        List<String> predefinedBrands = jdbcAccess.find("select name from cube.brand", (rs,num) -> {return rs.getString("name");});
        Dataset<Row> predefinedData = sparkSession.createDataFrame(
                predefinedBrands.stream()
                        .map(predefinedBrand -> RowFactory.create(predefinedBrand)).collect(Collectors.toList())
        , new StructType(new StructField[]{
                new StructField("brand_name", DataTypes.StringType, false, Metadata.empty()),
        }));
        StringIndexer stringIndexer = new StringIndexer()
                .setInputCol("brand_name")
                .setOutputCol("brand")
                .setHandleInvalid("keep");

        return stringIndexer.fit(predefinedData);
    }

    @Bean
    public StringIndexerModel productIndexerModel(SparkSession sparkSession) {
        List<String> predefinedProducts = jdbcAccess.find("select distinct(name) from cube.product", (rs,num) -> {return rs.getString("name");});

        Dataset<Row> predefinedData = sparkSession.createDataFrame(predefinedProducts.stream()
                .map(predefinedProduct -> RowFactory.create(predefinedProduct)).collect(Collectors.toList())
                , new StructType(new StructField[]{
                new StructField("product_name", DataTypes.StringType, false, Metadata.empty()),
        }));
        StringIndexer stringIndexer = new StringIndexer()
                .setInputCol("product_name")
                .setOutputCol("product")
                .setHandleInvalid("keep");

        return stringIndexer.fit(predefinedData);
    }

}
