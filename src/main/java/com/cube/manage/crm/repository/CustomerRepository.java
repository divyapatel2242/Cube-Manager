package com.cube.manage.crm.repository;

import com.cube.manage.crm.entity.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<Customer,Integer> {

    @Query(value = "SELECT * FROM cube.customer limit ?,?", nativeQuery = true)
    List<Customer> fetch100Customers(int pageNo, int size);
}
