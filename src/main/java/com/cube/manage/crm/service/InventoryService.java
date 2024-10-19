package com.cube.manage.crm.service;

import com.cube.manage.crm.entity.Inventory;
import com.cube.manage.crm.entity.Productunit;
import com.cube.manage.crm.enums.ProductUnitEnum;
import com.cube.manage.crm.enums.WarehousesEnum;
import com.cube.manage.crm.repository.InventoryRepository;
import com.cube.manage.crm.repository.ProductUnitRepository;
import com.cube.manage.crm.request.InventoryRequest;
import com.cube.manage.crm.response.InventoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class InventoryService {

    @Autowired
    private ProductUnitRepository productUnitRepository;

    @Autowired
    private InventoryRepository inventoryRepository;


    public void addInventory(InventoryRequest inventoryRequest) {
        Inventory inventory = inventoryRepository.fetchInventoryBySku(inventoryRequest.getSku());
        if(Objects.isNull(inventory)){
            addProductInInventory(inventoryRequest);
            return;
        }
        addQuantityOfProductInInventory(inventoryRequest, inventory);
    }

    private void addQuantityOfProductInInventory(InventoryRequest inventoryRequest, Inventory inventory) {
        inventory.setAvailableQuantity(inventory.getAvailableQuantity() + inventoryRequest.getSkids().size());
        inventory.setUpdatedDate(new Date());
        inventoryRepository.save(inventory);

        addProductUnit(inventoryRequest);
    }

    private void addProductInInventory(InventoryRequest inventoryRequest) {
        Inventory inventory = new Inventory();
        inventory.setSku(inventoryRequest.getSku());
        inventory.setAvailableQuantity(inventoryRequest.getSkids().size());
        inventory.setCreatedDate(new Date());
        inventory.setWarehouse(WarehousesEnum.valueOf(inventoryRequest.getWarehouse()).value);
        inventoryRepository.save(inventory);

        addProductUnit(inventoryRequest);

    }

    private void addProductUnit(InventoryRequest inventoryRequest) {
        List<Productunit> productunitList = new ArrayList<>();
        for (String skid : inventoryRequest.getSkids()){
            Productunit productunit = new Productunit();
            productunit.setSkid(skid);
            productunit.setSku(inventoryRequest.getSku());
            productunit.setWarehouse(WarehousesEnum.valueOf(inventoryRequest.getWarehouse()).value);
            productunit.setStatus(ProductUnitEnum.IN_STOCK.value);
            productunit.setCreatedDate(new Date());
            productunitList.add(productunit);
        }
        productUnitRepository.saveAll(productunitList);
    }

    public InventoryResponse fetchInventoryData(String id) {
        return null;
    }

    public void validateSkid(String skid) {
        Integer count = productUnitRepository.countOfSkid(skid);
        if(count != 0)
            throw new RuntimeException("Skid is not valid");
    }

    public void validateSkidToAdd(List<String> skids) {
        for(String skid:skids){
            validateSkid(skid);
        }
    }
}
