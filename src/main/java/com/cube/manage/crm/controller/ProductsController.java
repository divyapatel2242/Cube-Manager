package com.cube.manage.crm.controller;

import com.cube.manage.crm.request.ProductRequest;
import com.cube.manage.crm.response.BrandIdNameResponse;
import com.cube.manage.crm.response.ProductDetailResponse;
import com.cube.manage.crm.response.ProductResponse;
import com.cube.manage.crm.response.ProductResponseData;
import com.cube.manage.crm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductsController {

    @Autowired
    private ProductService productService;

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
    public ProductResponseData fetchProduct(@RequestParam(value = "id",required = false) String id){
       return productService.fetchProductData(id);
    }

    @GetMapping("/get-product-detail")
    public ProductDetailResponse fetchProductDetailResponse(@RequestParam(value = "id") String id){
        return productService.fetchProductDetailData(id);
    }

    @GetMapping("/get-all-products")
    public ResponseEntity<ProductResponse> fetchAllProduct(@RequestParam("page") Integer pageNo, @RequestParam("pageSize") Integer pageSize){
        return ResponseEntity.ok(productService.fetchProductsAsPerPage(pageNo, pageSize));
    }

    @GetMapping("/validate-sku")
    public ResponseEntity<String> validateSku(@RequestParam(value = "sku",required = true) String sku){
        try{
            productService.validateSku(sku);
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok("valid Sku");
    }

    @GetMapping("/get-brands")
    public List<BrandIdNameResponse> fetchBrandIdName(){
        return productService.fetchBrands();
    }

}
