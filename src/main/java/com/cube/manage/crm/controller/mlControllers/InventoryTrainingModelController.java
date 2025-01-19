package com.cube.manage.crm.controller.mlControllers;

import com.cube.manage.crm.request.OOStockDateRequest;
import com.cube.manage.crm.service.mlService.InventoryTrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/ml/inventory")
public class InventoryTrainingModelController {

    @Autowired
    private InventoryTrainingService inventoryTrainingService;

    @PostMapping("/train/data")
    public String loadAndTrainData() throws IOException {
        inventoryTrainingService.trainingInventoryData();
        return "Training of Data has Completed!";
    }

    @PostMapping("/projected/oos")
    public LocalDate fetchProjectedOutOfStockDate(@RequestBody OOStockDateRequest ooStockDateRequest){
        return inventoryTrainingService.fetchOutOfStockDate(ooStockDateRequest);
    }

}
