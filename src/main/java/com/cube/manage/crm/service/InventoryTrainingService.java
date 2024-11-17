package com.cube.manage.crm.service;

import org.apache.spark.ml.classification.RandomForestClassificationModel;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.regression.DecisionTreeRegressionModel;
import org.apache.spark.ml.regression.DecisionTreeRegressor;
import org.apache.spark.sql.*;
import org.apache.spark.sql.expressions.UserDefinedFunction;
import org.apache.spark.sql.types.DataTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class InventoryTrainingService {

    @Autowired
    private SparkSession sparkSession;

    private DecisionTreeRegressionModel decisionTreeRegressionModel;

    public String trainingInventoryData(){
        String path = "C:\\Divya\\DataBaseOfCubeManager\\InventoryStockDetail.csv";
        //Load Data from particular path and convert it into specific data type
        Dataset<Row> dataset = sparkSession.read().format("csv").option("header","true").option("inferSchema","true").load(path);
        dataset = dataset.select(
                new Column("instock_quantity").cast(DataTypes.IntegerType),
                new Column("instock_date").cast(DataTypes.DateType),
                new Column("outofstock_date").cast(DataTypes.DateType),
                new Column("brand_name").cast(DataTypes.StringType),
                new Column("product_type").cast(DataTypes.StringType),
                new Column("warehouse").cast(DataTypes.StringType));
        System.out.println("Total Count :"+dataset.count());
        List<String> nameOfColumns = Arrays.asList(dataset.columns());

        //Fetch a Count of Empty/Null Values in Column
        for(String name : nameOfColumns){
            System.out.println("Column : " + name + "Empty Count : " + dataset.filter(new Column(name).isNull()).count());
        }
        Dataset<Row> rows = dataset.describe();
        rows.show();


        StringIndexer brandIndexer = new StringIndexer().setInputCol("brand_name").setOutputCol("brand").setHandleInvalid("keep");
        StringIndexer productIndexer = new StringIndexer().setInputCol("product_type").setOutputCol("product").setHandleInvalid("keep");
        StringIndexer cityIndexer = new StringIndexer().setInputCol("warehouse").setOutputCol("city").setHandleInvalid("keep");


        Dataset<Row> brandDataSet = brandIndexer.fit(dataset).transform(dataset);
        Dataset<Row> productDataset = productIndexer.fit(brandDataSet).transform(brandDataSet);
        Dataset<Row> cityDataset = cityIndexer.fit(productDataset).transform(productDataset);

        cityDataset.show();
        //Remove Sex and Embarked Columns from dataset
        dataset = cityDataset.drop("brand_name");
        dataset = dataset.drop("product_type");
        dataset = dataset.drop("warehouse");
        dataset.show();

        List<Integer> list = dateDiff(dataset, dataset.col("instock_date"), dataset.col("outofstock_date"));
        UserDefinedFunction assignValue = functions.udf((Integer index) -> list.get(index), DataTypes.IntegerType);

        // Add the new column based on row index using the UDF
        dataset = dataset.withColumn("date_diff", functions.monotonically_increasing_id())
                .withColumn("date_diff", assignValue.apply(functions.col("date_diff").cast("int")));

        dataset.show();
        dataset = dataset.drop("instock_date");
        dataset = dataset.drop("outofstock_date");
        dataset.show();

        //Merge columns which will use for Prediction
        VectorAssembler vectorAssembler = new VectorAssembler()
                .setInputCols(new String[]{"instock_quantity","brand","product","city"}).setOutputCol("features");
        Dataset<Row> finalDataSet = vectorAssembler.transform(dataset);

        finalDataSet.show();

        trainModelData(finalDataSet);


        return "Data Trained";
    }

    private List<Integer> dateDiff(Dataset<Row> dataset, Column startDate, Column endDate) {
        List<Date> startDateList = dataset.select(startDate)
                .as(Encoders.DATE())
                .collectAsList();

        List<Date> endDateList = dataset.select(endDate)
                .as(Encoders.DATE())
                .collectAsList();

        List<Integer> list = IntStream.range(0, startDateList.size())
                .mapToObj(i -> Integer.parseInt(""+ChronoUnit.DAYS.between(startDateList.get(i).toLocalDate(), endDateList.get(i).toLocalDate())))
                .collect(Collectors.toList());
        return list;
    }

    private String trainModelData(Dataset<Row> rowDataset){
        Dataset<Row>[] splitedData = rowDataset.randomSplit(new double[]{0.8D,0.2D});
        Dataset<Row> trainDataSet = splitedData[0];
        Dataset<Row> testDataSet = splitedData[1];

        //Train Model from feature column with survived column
        DecisionTreeRegressor estimator = new DecisionTreeRegressor()
                .setLabelCol("date_diff").setFeaturesCol("features").setMaxDepth(5);

        decisionTreeRegressionModel = estimator.fit(trainDataSet);

        //make Prediction dataset which will predict
        Dataset<Row> prediction = decisionTreeRegressionModel.transform(testDataSet);

        prediction.printSchema();
        prediction.show();


        // found accuracy of predicted and actual
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setLabelCol("date_diff").setPredictionCol("prediction");

        double accuracy = evaluator.evaluate(prediction);

        System.out.println("Accuracy of Model : " + accuracy);
        return String.valueOf(accuracy);
    }

}
