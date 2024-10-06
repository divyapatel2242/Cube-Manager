package com.cube.manage.crm.repository;

import com.cube.manage.crm.entity.Productunit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductUnitRepository extends CrudRepository<Productunit,Integer> {

    @Query(value = "Select count(id) as count from cube.productunit where skid = ? ", nativeQuery = true)
    Integer countOfSkid(String skid);

    @Query(value = "select * from cube.productunit where sku = ? and status = ? order by id asce limit ?")
    List<Productunit> fetchAllocatingUnits(String sku, String status, Integer quantity);
}
