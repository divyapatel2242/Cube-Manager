package com.cube.manage.crm.repository;

import com.cube.manage.crm.dbutil.JDBCAccess;
import com.cube.manage.crm.response.BrandIdNameResponse;
import com.cube.manage.crm.response.BrandResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BrandDataRepository {

    @Autowired
    private JDBCAccess jdbcAccess;

    public List<BrandIdNameResponse> fetchBrands(){
        return jdbcAccess.find("select id, name from cube.brand", (resultSet, num)->{
            return new BrandIdNameResponse(resultSet.getInt("id"), resultSet.getString("name"));
        });
    }

}
