package com.cube.manage.crm.controller;

import com.cube.manage.crm.request.ProductRequest;
import com.cube.manage.crm.response.ProductResponse;
import com.cube.manage.crm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/product")
public class ProductsController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String fetchProduct(Model model){
        return "product";
    }

    @PostMapping("/add-product")
    public String addProduct(@RequestBody ProductRequest productRequest){
        productService.addProduct(productRequest);
        return "Successfully Added Product";
    }

    @PostMapping("/add-productItem")
    public String addProductItem(@RequestBody ProductRequest productRequest) {
        productService.addProductItem(productRequest);
        return "Successfully Added Item";
    }
    @GetMapping("/get-product")
    public String fetchProduct(@RequestParam(value = "id",required = false) String id, Model model){
       ProductResponse productResponse = productService.fetchProductData(id);
       model.addAttribute("productResponse",productResponse);
       return "product";
    }

    @GetMapping("/validate-sku")
    public String validateSku(@RequestParam(value = "sku",required = true) String sku, Model model){
        try{
            productService.validateSku(sku);
        } catch (Exception e){
            model.addAttribute("validationSku","Sku already Present!");
            return "product";
        }
        model.addAttribute("validationSku","Scanned Successfully");
        return "product";
    }

}
