package com.cube.manage.crm.controller;

import com.cube.manage.crm.entity.Brand;
import com.cube.manage.crm.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping
public class SpringSecurityLearningController {

    @Autowired
    private BrandRepository brandRepository;

    @PostMapping("/brands")
    public List<Brand> getBrands(){
        return StreamSupport.stream(
                ((Iterable<Brand>) () -> brandRepository.findAll().iterator()).spliterator(), false
        ).collect(Collectors.toList());
    }

}
