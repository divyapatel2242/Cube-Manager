package com.cube.manage.crm.repository;

import com.cube.manage.crm.entity.Inventory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends CrudRepository<Inventory, Integer> {

    @Query(value = "select available_quantity from cube.inventory where sku = :sku",nativeQuery = true)
    public Integer fetchAvailableQuantity(String sku);

    @Query(value = "select * from cube.inventory where sku = :sku", nativeQuery = true)
    Inventory fetchInventoryBySku(String sku);

    @Query(value = "select * from cube.inventory where sku in (:skus)", nativeQuery = true)
    List<Inventory> fetchInventoryBySkus(List<String> skus);
}
