package com.cube.manage.crm.service;

import jakarta.annotation.PostConstruct;
import org.apache.spark.ml.recommendation.ALS;
import org.apache.spark.ml.recommendation.ALSModel;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommandationService {
    @Autowired
    private SparkSession sparkSession;

    private ALSModel alsModel;

    @PostConstruct
    private void trainModel(){
        Dataset<Row> rowDataset = loadTrainedData();
        alsModel = trainModelData(rowDataset);
    }

    private ALSModel trainModelData(Dataset<Row> rowDataset) {
        ALS als = new ALS().setMaxIter(10).setRegParam(0.1).setUserCol("userId").setItemCol("movieId").setRatingCol("rating");
        ALSModel alsModel = als.fit(rowDataset);
        alsModel.setColdStartStrategy("drop");
        return alsModel;
    }

    private Dataset<Row> loadTrainedData() {
        String path = "";
        return sparkSession.read().format("csv").option("header","true").option("inferSchema","true").load(path);
    }

    public List<String> getRecommanded(Integer userId){
        Dataset<Row> dataset = sparkSession.createDataFrame(Collections.singletonList(userId),Integer.class).toDF("userId");
        Dataset<Row> rowDataset = alsModel.recommendForUserSubset(dataset,10);
        List<Row> list = rowDataset.collectAsList();
        Row row = list.get(0);
        List<Row> listOfROw = row.getList(1);
        List<String> recommandedList = new ArrayList<>();
        for (Row row1: listOfROw){
            recommandedList.add(row1.getAs(0).toString());
        }
        return recommandedList;
    }

}
