package com.cube.manage.crm.repository;

import com.cube.manage.crm.entity.Product;
import com.cube.manage.crm.esRepo.ProductEsRepository;
import com.cube.manage.crm.esdocument.ProductEs;
import com.cube.manage.crm.response.ProductResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class ProductDataRepository {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductEsRepository productEsRepository;

    public ProductResponse fetchProductResponseData(String id) {
        ProductResponse productResponse = new ProductResponse();
        ProductEs productEs = productEsRepository.findById(id).orElse(null);
        if(Objects.nonNull(productEs)){
            BeanUtils.copyProperties(productEs,productResponse);
            productResponse.setProductId(Integer.parseInt(productEs.getId()));
            //ToDo:Search brand as well
            return productResponse;
        }
        Product product = productRepository.findById(Integer.valueOf(id)).orElse(null); //ToDo make query to fetch both brand and product
        if(Objects.nonNull(product)){
            BeanUtils.copyProperties(product,productResponse);
            productResponse.setProductId(product.getId());
            //ToDo:Search brand as well
            return productResponse;
        }
        return null;
    }
}
