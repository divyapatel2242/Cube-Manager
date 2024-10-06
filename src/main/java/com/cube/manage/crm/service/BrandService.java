package com.cube.manage.crm.service;

import com.cube.manage.crm.entity.Brand;
import com.cube.manage.crm.repository.BrandRepository;
import com.cube.manage.crm.request.BrandRequest;
import com.cube.manage.crm.response.BrandResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BrandService {


    @Autowired
    private BrandRepository brandRepository;

    public void addBrand(BrandRequest brandRequest) {
        Brand brand = new Brand();
        brand.setName(brandRequest.getName());
        brand.setCreatedDate(new Date());
        brandRepository.save(brand);
    }

    public BrandResponse fetchBrandData(String id) {
        if(Objects.isNull(id)){
           Brand brand = brandRepository.findById(Integer.parseInt(id)).get();
           return new BrandResponse(Arrays.asList(brand));
        }
        Iterable<Brand> iterable = brandRepository.findAll();
        Iterator<Brand> iterator = iterable.iterator();
        BrandResponse brandResponse = new BrandResponse();
        List<Brand> brandList = new ArrayList<>();
        while (iterator.hasNext()){
            brandList.add(iterator.next());
        }
        brandResponse.setBrands(brandList);
        return brandResponse;
    }
}