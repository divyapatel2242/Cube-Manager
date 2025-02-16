package com.cube.manage.crm.controller;

import com.cube.manage.crm.request.BrandRequest;
import com.cube.manage.crm.response.BrandResponse;
import com.cube.manage.crm.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @PostMapping("/add-Brand")
    public String addBrand(@RequestBody BrandRequest brandRequest){
        brandService.addBrand(brandRequest);
        return "Successfully Added Inventory";
    }

    @GetMapping("/get-Brand")
    public BrandResponse fetchBrand(@RequestParam(value = "id",required = false) String id){
        return brandService.fetchBrandData(id);
    }

}
