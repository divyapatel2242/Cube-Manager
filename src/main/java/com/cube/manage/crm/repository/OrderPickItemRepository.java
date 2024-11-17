package com.cube.manage.crm.repository;

import com.cube.manage.crm.entity.OrderPickItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderPickItemRepository extends CrudRepository<OrderPickItem,Integer> {
}
