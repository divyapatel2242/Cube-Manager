package com.cube.manage.crm.repository;

import com.cube.manage.crm.entity.OrderStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusRepository extends CrudRepository<OrderStatus,Integer> {

    @Query(value = "select id from cube.orderstatus where status = ?", nativeQuery = true)
    Integer fetchStatusId(String status);
}
