package com.cube.manage.crm.controller;

import com.cube.manage.crm.request.InventoryRequest;
import com.cube.manage.crm.response.InventoryResponse;
import com.cube.manage.crm.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/")
    public String fetchInventory(Model model){
        return "Inventory";
    }

    @PostMapping("/add-Inventory")
    @ResponseBody
    public String addInventory(@RequestBody InventoryRequest inventoryRequest){
        inventoryService.validateSkidToAdd(inventoryRequest.getSkids());
        inventoryService.addInventory(inventoryRequest);
        return "Successfully Added Inventory";
    }

    @GetMapping("/get-Inventory")
    public String fetchBrand(@RequestParam(value = "id",required = false) String id, Model model){
        InventoryResponse inventoryResponse = inventoryService.fetchInventoryData(id);
        model.addAttribute("inventoryResponse",inventoryResponse);
        return "inventory";
    }

    @GetMapping("/validate-skid")
    public String validateSkid(@RequestParam(value = "skid") String skid, Model model){
        try{
            inventoryService.validateSkid(skid);
        } catch (Exception e){
            model.addAttribute("validationSkid","Skid already Present!");
            return "inventory";
        }
        model.addAttribute("validationSkid","Skid Scanned Successfully");
        return "inventory";

    }

}
