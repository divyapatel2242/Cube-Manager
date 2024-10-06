package com.cube.manage.crm.service;

import com.cube.manage.crm.repository.InventoryRepository;
import com.cube.manage.crm.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderHelperService {


    @Autowired
    private InventoryRepository inventoryRepository;



    @Transactional
    public void validateOrderRequest(OrderRequest orderRequest) {
        Integer availableQuantity = inventoryRepository.fetchAvailableQuantity(orderRequest.getSku());
        if(orderRequest.getQuantity() > availableQuantity){
            throw new RuntimeException("Ordered Quantity is more than available quantity!");
        }
    }
}
