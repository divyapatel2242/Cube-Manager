package com.cube.manage.crm.controller;

import com.cube.manage.crm.request.InventoryRequest;
import com.cube.manage.crm.response.InventoryResponse;
import com.cube.manage.crm.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/add-Inventory")
    public ResponseEntity<String> addInventory(@RequestBody InventoryRequest inventoryRequest){
        inventoryService.validateSkidToAdd(inventoryRequest.getSkids());
        inventoryService.addInventory(inventoryRequest);
        return ResponseEntity.ok("Inventory Added");
    }

    @GetMapping("/get-Inventory")
    public InventoryResponse fetchBrand(@RequestParam(value = "id",required = false) String id){
        return inventoryService.fetchInventoryData(id);
    }

    @GetMapping("/validate-skid")
    public ResponseEntity<String> validateSkid(@RequestParam(value = "skid") String skid){
        try{
            inventoryService.validateSkid(skid);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok("Valid Skid");

    }

}
