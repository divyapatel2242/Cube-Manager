package com.cube.manage.crm.service;

import com.cube.manage.crm.request.PredictInputData;
import org.apache.spark.ml.classification.RandomForestClassificationModel;
import org.apache.spark.ml.classification.RandomForestClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.List;

/*
* @auther
* Divya Patel
* */

@Service
public class TitanicDataSetTestingMLService {
    //Testing Service for titanic DataSet
    @Autowired
    private SparkSession sparkSession;

    private RandomForestClassificationModel model;

    public Dataset<Row> loadTrainedData() {
        String path = "C:\\Users\\divya\\Downloads\\titanic\\train.csv";
        //Load Data from particular path and convert it into specific data type
        Dataset<Row> dataset = sparkSession.read().format("csv").option("header","true").option("inferSchema","true").load(path);
        dataset = dataset.select(
               new Column("PassengerId").cast(DataTypes.IntegerType),
                new Column("Survived").cast(DataTypes.IntegerType),
                new Column("Pclass").cast(DataTypes.DoubleType),
                new Column("Sex").cast(DataTypes.StringType),
                new Column("Age").cast(DataTypes.DoubleType),
                new Column("Fare").cast(DataTypes.DoubleType),
                new Column("Embarked").cast(DataTypes.StringType)
                );
        System.out.println("Total Count :"+dataset.count());
        List<String> nameOfColumns = Arrays.asList(dataset.columns());

        //Fetch a Count of Empty/Null Values in Column
        for(String name : nameOfColumns){
            System.out.println("Column : " + name + "Empty Count : " + dataset.filter(new Column(name).isNull()).count());
        }
        Dataset<Row> rows = dataset.describe();
        rows.show();

        //Remove Null Contaning Row from dataSet
        Column dropColumn = null;
        for(String name : nameOfColumns){
            Column filterColumn = new Column(name).isNotNull();
            if(dropColumn == null){
                dropColumn = filterColumn;
                continue;
            }
            dropColumn = dropColumn.and(filterColumn);
        }

        Dataset<Row> nonNullDataSet = dataset.filter(dropColumn);
        nonNullDataSet.show();

        //Convert Set to Gender where Female->1 male->0 so ML can better understand data from number than text
        //Convert Embarked to Boarded
        StringIndexer sexIndexer = new StringIndexer().setInputCol("Sex").setOutputCol("Gender").setHandleInvalid("keep");
        StringIndexer embarkIndexer = new StringIndexer().setInputCol("Embarked").setOutputCol("Boarded").setHandleInvalid("keep");

        Dataset<Row> genderDataSet = sexIndexer.fit(nonNullDataSet).transform(nonNullDataSet);
        Dataset<Row> embarkedDataset = embarkIndexer.fit(genderDataSet).transform(genderDataSet);

        embarkedDataset.show();
        //Remove Sex and Embarked Columns from dataset
        dataset = embarkedDataset.drop("Sex");
        dataset = dataset.drop("Embarked");
        dataset.show();

        //Merge columns which will use for Prediction
        VectorAssembler vectorAssembler = new VectorAssembler()
                .setInputCols(new String[]{"Pclass","Age","Fare","Gender","Boarded"}).setOutputCol("features");
        Dataset<Row> finalDataSet = vectorAssembler.transform(dataset);

        finalDataSet.show();

        trainModelData(finalDataSet);


        return nonNullDataSet;
    }

    private void trainModelData(Dataset<Row> rowDataset) {
        // for testing converted training data set to 80:20 ratio
        Dataset<Row>[] splitedData = rowDataset.randomSplit(new double[]{0.8D,0.2D});
        Dataset<Row> trainDataSet = splitedData[0];
        Dataset<Row> testDataSet = splitedData[1];

        //Train Model from feature column with survived column
        RandomForestClassifier estimator = new RandomForestClassifier()
                .setLabelCol("Survived").setFeaturesCol("features").setMaxDepth(5);

        model = estimator.fit(trainDataSet);

        //make Prediction dataset which will predict
        Dataset<Row> prediction = model.transform(testDataSet);

        prediction.printSchema();
        prediction.show();


        // found accuracy of predicted and actual
        MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
                .setLabelCol("Survived").setPredictionCol("prediction");

        double accuracy = evaluator.evaluate(prediction);

        System.out.println("Accuracy of Model : " + accuracy);


    }

    public Double predictInputData(PredictInputData predictedData){
        Dataset<Row> data = sparkSession.createDataFrame(Arrays.asList(
                RowFactory.create(predictedData.getPclass(),
                        predictedData.getAge(),
                        predictedData.getFare(),
                        predictedData.getGender(),
                        predictedData.getBoarded())
        ), new StructType(new StructField[]{
                new StructField("Pclass", DataTypes.DoubleType, false, Metadata.empty()),
                new StructField("Age", DataTypes.DoubleType, false, Metadata.empty()),
                new StructField("Fare", DataTypes.DoubleType, false, Metadata.empty()),
                new StructField("Gender", DataTypes.StringType, false, Metadata.empty()),
                new StructField("Boarded", DataTypes.StringType, false, Metadata.empty())
        }));

        data.show();

        StringIndexer genderIndexer = new StringIndexer().setInputCol("Gender").setOutputCol("GenderIndex").setHandleInvalid("keep");
        StringIndexer boardedIndexer = new StringIndexer().setInputCol("Boarded").setOutputCol("BoardedIndex").setHandleInvalid("keep");

        Dataset<Row> genderData = genderIndexer.fit(data).transform(data);
        Dataset<Row> finalData = boardedIndexer.fit(genderData).transform(genderData);

        finalData.drop("Gender","Boarded");

        finalData.show();

        VectorAssembler vectorAssembler = new VectorAssembler()
                .setInputCols(new String[]{"Pclass","Age","Fare","GenderIndex","BoardedIndex"}).setOutputCol("features");
        Dataset<Row> finalFetureData = vectorAssembler.transform(finalData);
        finalFetureData.show();

        Dataset<Row> prediction = model.transform(finalFetureData);

        List<Row> response = prediction.select("prediction").collectAsList();
        return response.get(0).getDouble(0);

    }

}
