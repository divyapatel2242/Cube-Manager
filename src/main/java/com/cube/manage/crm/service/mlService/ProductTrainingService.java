package com.cube.manage.crm.service.mlService;

import com.cube.manage.crm.constant.MlConstants;
import com.cube.manage.crm.request.ProductPredictionRequest;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.ml.regression.DecisionTreeRegressionModel;
import org.apache.spark.ml.regression.DecisionTreeRegressor;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductTrainingService {

    @Autowired
    private SparkSession sparkSession;

    @Autowired
    private IndexerService indexerService;

    private DecisionTreeRegressionModel decisionTreeRegressionModel;

    public String trainingInventoryData() throws IOException {
        String path = "C:\\Divya\\DataBaseOfCubeManager\\ProductOrderDetail.csv";
        //Load Data from particular path and convert it into specific data type
        Dataset<Row> dataset = sparkSession.read().format("csv").option("header","true").option("inferSchema","true").load(path);
        dataset = dataset.select(
                new Column(MlConstants.PRODUCT_NAME).cast(DataTypes.StringType),
                new Column(MlConstants.PRODUCT_SELLING_PRICE).cast(DataTypes.DoubleType),
                new Column(MlConstants.BRAND_NAME).cast(DataTypes.StringType),
                new Column(MlConstants.ORDER_COUNT).cast(DataTypes.IntegerType));
        System.out.println("Total Count :"+dataset.count());
        List<String> nameOfColumns = Arrays.asList(dataset.columns());

        //Fetch a Count of Empty/Null Values in Column
        for(String name : nameOfColumns){
            System.out.println("Column : " + name + "Empty Count : " + dataset.filter(new Column(name).isNull()).count());
        }
        Dataset<Row> rows = dataset.describe();
        rows.show();

        Dataset<Row> brandDataSet = indexerService.fetchBrandIndexerModel(dataset);
        Dataset<Row> productDataset = indexerService.fetchProductIndexerModel(brandDataSet);

        productDataset.show();
        //Remove Sex and Embarked Columns from dataset
        dataset = productDataset.drop(MlConstants.BRAND_NAME);
        dataset = dataset.drop(MlConstants.PRODUCT_NAME);
        dataset.show();

        VectorAssembler vectorAssembler = new VectorAssembler()
                .setInputCols(new String[]{MlConstants.PRODUCT,MlConstants.BRAND,MlConstants.PRODUCT_SELLING_PRICE}).setOutputCol(MlConstants.FEATURE);
        Dataset<Row> finalDataSet = vectorAssembler.transform(dataset);

        finalDataSet.show();

        trainModelData(finalDataSet);
        return "Data Trained";
    }

    private String trainModelData(Dataset<Row> rowDataset) throws IOException {
        Dataset<Row>[] splitedData = rowDataset.randomSplit(new double[]{0.95D,0.05D});
        Dataset<Row> trainDataSet = splitedData[0];
        Dataset<Row> testDataSet = splitedData[1];

        DecisionTreeRegressor estimator = new DecisionTreeRegressor()
                .setLabelCol(MlConstants.ORDER_COUNT).setFeaturesCol(MlConstants.FEATURE).setMaxDepth(5);

        decisionTreeRegressionModel = estimator.fit(trainDataSet);

        //make Prediction dataset which will predict
        Dataset<Row> prediction = decisionTreeRegressionModel.transform(testDataSet);
        prediction.printSchema();
        prediction.show();

        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setLabelCol(MlConstants.ORDER_COUNT).setPredictionCol(MlConstants.PREDICTION);

        double accuracy = evaluator.evaluate(prediction);
        decisionTreeRegressionModel.write().overwrite().save("decisionTreeProductOrderCountProjectionModel");
        System.out.println("Accuracy of Model : " + accuracy);
        return String.valueOf(accuracy);
    }

    public Integer fetchPredictedOrderCount(ProductPredictionRequest productPredictionRequest) {
        Dataset<Row> data = sparkSession.createDataFrame(Arrays.asList(
                RowFactory.create(productPredictionRequest.getProductName(),
                        productPredictionRequest.getBrandName(),
                        productPredictionRequest.getSellingPrice())
        ), new StructType(new StructField[]{
                new StructField(MlConstants.PRODUCT_NAME, DataTypes.StringType, false, Metadata.empty()),
                new StructField(MlConstants.BRAND_NAME, DataTypes.StringType, false, Metadata.empty()),
                new StructField(MlConstants.PRODUCT_SELLING_PRICE, DataTypes.DoubleType, false, Metadata.empty()),
        }));
        Dataset<Row> brandDataSet = indexerService.fetchBrandIndexerModel(data);
        Dataset<Row> finalData = indexerService.fetchProductIndexerModel(brandDataSet);
        finalData.show();
        finalData.drop(MlConstants.BRAND_NAME,MlConstants.PRODUCT_NAME);

        VectorAssembler vectorAssembler = new VectorAssembler()
                .setInputCols(new String[]{MlConstants.PRODUCT_SELLING_PRICE,MlConstants.BRAND,MlConstants.PRODUCT}).setOutputCol(MlConstants.FEATURE);
        Dataset<Row> finalDataSet = vectorAssembler.transform(finalData);
        DecisionTreeRegressionModel decisionTreeRegressionModelLoaded = DecisionTreeRegressionModel.load("decisionTreeProductOrderCountProjectionModel");
        Dataset<Row> prediction = decisionTreeRegressionModelLoaded.transform(finalDataSet);
        Integer orderCount = Math.toIntExact(Math.round(prediction.select(MlConstants.PREDICTION).collectAsList().get(0).getDouble(0)));
        return orderCount;
    }

}
