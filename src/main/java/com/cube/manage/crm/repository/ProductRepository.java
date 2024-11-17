package com.cube.manage.crm.repository;

import com.cube.manage.crm.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product,Integer> {

    @Query(value = "select * from cube.product where id = :productId", nativeQuery = true)
    Product fetchProductByid(Integer productId);

    @Query(value = "select * from cube.product where id in (:productIds)", nativeQuery = true)
    List<Product> fetchProductListByids(List<Integer> productIds);

    @Query(value = "select * from cube.product where name = :name", nativeQuery = true)
    List<Product> fetchProductsListFromName(String name);
}
