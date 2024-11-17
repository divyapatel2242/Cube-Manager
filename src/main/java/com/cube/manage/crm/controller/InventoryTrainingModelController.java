package com.cube.manage.crm.controller;

import com.cube.manage.crm.service.InventoryTrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ml/inventory")
public class InventoryTrainingModelController {

    @Autowired
    private InventoryTrainingService inventoryTrainingService;

    @PostMapping("/train/data")
    public String loadAndTrainData(){
        inventoryTrainingService.trainingInventoryData();
        return "Training of Data has Completed!";
    }

}
