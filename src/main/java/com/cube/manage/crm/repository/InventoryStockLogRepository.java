package com.cube.manage.crm.repository;

import com.cube.manage.crm.entity.InventoryStockLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryStockLogRepository extends CrudRepository<InventoryStockLog, Integer> {
}
