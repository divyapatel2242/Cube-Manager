package com.cube.manage.crm.repository;

import com.cube.manage.crm.entity.Brand;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends CrudRepository<Brand, Integer> {

    @Query(value = "select id from cube.brand", nativeQuery = true)
    public List<Integer> fetchBrandIds();

}
