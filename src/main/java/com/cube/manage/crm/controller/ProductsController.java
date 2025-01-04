package com.cube.manage.crm.controller;

import com.cube.manage.crm.request.ProductRequest;
import com.cube.manage.crm.response.BrandIdNameResponse;
import com.cube.manage.crm.response.ProductResponse;
import com.cube.manage.crm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<String> addProduct(@RequestBody ProductRequest productRequest, Model model){
        productService.addProduct(productRequest);
        return ResponseEntity.ok("Successfully Added Product");
    }

    @PostMapping("/add-productItem")
    public ResponseEntity<String> addProductItem(@RequestBody ProductRequest productRequest, Model model) {
        productService.addProductItem(productRequest);
        return ResponseEntity.ok("Successfully Added Item");
    }
    @GetMapping("/get-product")
    public String fetchProduct(@RequestParam(value = "id",required = false) String id, Model model){
       ProductResponse productResponse = productService.fetchProductData(id);
       model.addAttribute("productResponse",productResponse);
       return "product";
    }

    @GetMapping("/get-all-products")
    public ResponseEntity<List<ProductResponse>> fetchAllProduct(@RequestParam("page") Integer pageNo, @RequestParam("pageSize") Integer pageSize){
        return ResponseEntity.ok(productService.fetchProductsAsPerPage(pageNo, pageSize));
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

    @GetMapping("/get-brands")
    @ResponseBody
    public List<BrandIdNameResponse> fetchBrandIdName(Model model){
        return productService.fetchBrands();
    }

}
