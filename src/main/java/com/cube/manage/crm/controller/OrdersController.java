package com.cube.manage.crm.controller;

import com.cube.manage.crm.request.OrderRequest;
import com.cube.manage.crm.response.OrderResponse;
import com.cube.manage.crm.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cube-manage/orders")
public class OrdersController {

    @Autowired
    private OrderService orderService;


    @GetMapping("/available/sku")
    public String availableSku(@RequestParam("sku") String sku, Model model){
        Integer availableQuantity = orderService.fetchAvailableQuantityOfSku(sku);
        model.addAttribute("availableSku",availableQuantity);
        return "order";
    }

    @PostMapping("/place/order")
    public String placeOrder(@RequestBody OrderRequest orderRequest, Model model){
        OrderResponse orderResponse = orderService.placeOrder(orderRequest);
        if(orderResponse.getIsSuccess()){
            model.addAttribute("orderSuccessResponse",orderResponse.getSuccessMessage());
        } else{
            model.addAttribute("orderErrorResponse",orderResponse.getErrorMessage());
        }
        return "order";

    }

}
