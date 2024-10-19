package com.cube.manage.crm.repository;

import com.cube.manage.crm.entity.ProductItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductItemRepository extends CrudRepository<ProductItem,Integer> {

    @Query(value = "SELECT count(id) as count FROM cube.product_item where sku = :sku ;", nativeQuery = true)
    Integer validateSku(String sku);
}
