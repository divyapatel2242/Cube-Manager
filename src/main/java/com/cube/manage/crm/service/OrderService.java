package com.cube.manage.crm.service;

import com.cube.manage.crm.repository.InventoryRepository;
import com.cube.manage.crm.request.OrderRequest;
import com.cube.manage.crm.response.OrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private OrderHelperService orderHelperService;

    @Autowired
    private OrderAllocationService orderAllocationService;

    public Integer fetchAvailableQuantityOfSku(String sku) {
        return inventoryRepository.fetchAvailableQuantity(sku);
    }

    @Transactional
    public OrderResponse placeOrder(OrderRequest orderRequest) {
        OrderResponse orderResponse = new OrderResponse();
        try{
            orderHelperService.validateOrderRequest(orderRequest);
            orderAllocationService.assignSkidsToOrder(orderRequest);
            orderAllocationService.updateInventory(orderRequest);
            orderResponse.setIsSuccess(true);
            orderResponse.setSuccessMessage("Order Placed Successfully!");
        } catch (Exception e){
            orderResponse.setIsSuccess(false);
            orderResponse.setErrorMessage(e.getMessage());
        }
        return orderResponse;
    }
}
