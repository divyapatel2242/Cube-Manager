package com.cube.manage.crm.service;

import com.cube.manage.crm.entity.Brand;
import com.cube.manage.crm.repository.BrandDataRepository;
import com.cube.manage.crm.repository.BrandRepository;
import com.cube.manage.crm.request.BrandRequest;
import com.cube.manage.crm.response.BrandIdNameResponse;
import com.cube.manage.crm.response.BrandResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BrandService {


    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private BrandDataRepository brandDataRepository;

    public void addBrand(BrandRequest brandRequest) {
        Brand brand = new Brand();
        brand.setName(brandRequest.getName());
        brand.setCreatedDate(new Date());
        brandRepository.save(brand);
    }

    public BrandResponse fetchBrandData(String id) {
        if(Objects.nonNull(id)){
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

    public List<BrandIdNameResponse> fetchAllBrands() {
        return StreamSupport.stream(
                ((Iterable<Brand>) () -> brandRepository.findAll().iterator()).spliterator(), false
        ).map(brand -> new BrandIdNameResponse(brand.getId(), brand.getName())).collect(Collectors.toList());
    }

    public List<BrandIdNameResponse> fetchBrandsData(){
        return brandDataRepository.fetchBrands();
    }
}
