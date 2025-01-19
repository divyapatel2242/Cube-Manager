package com.cube.manage.crm.service.mlService;

import org.apache.spark.ml.feature.StringIndexerModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.stereotype.Service;

@Service
public class IndexerService {

    private final StringIndexerModel brandIndexerModel;
    private final StringIndexerModel productIndexerModel;

    public IndexerService(StringIndexerModel brandIndexerModel, StringIndexerModel productIndexerModel){
        this.brandIndexerModel = brandIndexerModel;
        this.productIndexerModel = productIndexerModel;
    }

    public Dataset<Row> fetchBrandIndexerModel(Dataset<Row> data){
        return brandIndexerModel.setHandleInvalid("keep").transform(data);
    }

    public Dataset<Row> fetchProductIndexerModel(Dataset<Row> data){
        return productIndexerModel.setHandleInvalid("keep").transform(data);
    }

}
