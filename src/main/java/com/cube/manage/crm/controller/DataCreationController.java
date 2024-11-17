package com.cube.manage.crm.controller;

import com.cube.manage.crm.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataCreationController {


    @Autowired
    private DataCreationService dataCreationService;


    //Create Brand Detail
    @PostMapping("/create/random/brand")
    public String createRandomBrand(){
        dataCreationService.createBrands();
        return "Successfully Created Brands";
    }

    //Create Customer Detail
    @PostMapping("/create/random/customer")
    @ResponseBody
    public String createRandom(){
        dataCreationService.createCusomers();
        return "Successfully Created Customer Data";
    }

    //Create Inventory Detail
    @PostMapping("/create/inventory")
    @ResponseBody
    public String createInventory(){
        dataCreationService.createInventory();
        return "Succesfully inventory created";
    }

    //Create Orders Detail
    @PostMapping("/create/orders")
    @ResponseBody
    public String createOrders(){
        dataCreationService.createRandomOrders();
        return "Successfully Completed";
    }

    //Create Products Detail
    @PostMapping("/create/products")
    @ResponseBody
    public String createProducts() throws JsonProcessingException {
        dataCreationService.createProducts();
        return "Successfully created products";
    }

    //Create Product's Prices Detail
    @PostMapping("/add/prices")
    @ResponseBody
    public String addProductPrice(){
        dataCreationService.addProductsPrices();
        return "Successfully added products price";
    }

}
