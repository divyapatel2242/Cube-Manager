package com.cube.manage.crm.repository;

import com.cube.manage.crm.entity.OrderItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends CrudRepository<OrderItem, Integer> {
}
