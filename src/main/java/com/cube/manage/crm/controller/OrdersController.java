package com.cube.manage.crm.controller;

import com.cube.manage.crm.request.OrderRequest;
import com.cube.manage.crm.response.OrderResponse;
import com.cube.manage.crm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    @Autowired
    private OrderService orderService;


    @GetMapping("/available/sku")
    public ResponseEntity<Integer> availableSku(@RequestParam("sku") String sku){
        Integer availableQuantity = orderService.fetchAvailableQuantityOfSku(sku);
        return ResponseEntity.ok(availableQuantity);
    }

    @PostMapping("/place/order")
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequest orderRequest){
        OrderResponse orderResponse = orderService.placeOrder(orderRequest);
        if(orderResponse.getIsSuccess()){
            return ResponseEntity.ok(orderResponse.getSuccessMessage());
        }
        return ResponseEntity.badRequest().body(orderResponse.getErrorMessage());
    }

}
