package com.cube.manage.crm.controller;

import com.cube.manage.crm.request.PredictInputData;
import com.cube.manage.crm.service.TitanicDataSetTestingMLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/*
 * @auther
 * Divya Patel
 * */

@RestController
public class MLTestingController {

    @Autowired
    private TitanicDataSetTestingMLService titanicDataSetTestingMLService;
//Testing
    @GetMapping("/getTitanicData")
    public String getTitanicData(){
        titanicDataSetTestingMLService.loadTrainedData();
        return "Data Loaded";
    }

    @PostMapping("/predictInputData")
    public Double predictData(@RequestBody PredictInputData predictedData){
        return titanicDataSetTestingMLService.predictInputData(predictedData);
    }

}
