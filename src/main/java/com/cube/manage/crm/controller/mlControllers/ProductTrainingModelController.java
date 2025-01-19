package com.cube.manage.crm.controller.mlControllers;

import com.cube.manage.crm.request.ProductPredictionRequest;
import com.cube.manage.crm.service.mlService.ProductTrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/ml/product")
public class ProductTrainingModelController {

    @Autowired
    private ProductTrainingService productTrainingService;

    @PostMapping("/train/data")
    public String trainProductDataToPredict() throws IOException {
        productTrainingService.trainingInventoryData();
        return "Product Training Done!!";
    }

    @PostMapping("/predict/order-count")
    public ResponseEntity<Integer> predictProductOrderCount(@RequestBody ProductPredictionRequest productPredictionRequest){
        return ResponseEntity.ok(productTrainingService.fetchPredictedOrderCount(productPredictionRequest));
    }

}
