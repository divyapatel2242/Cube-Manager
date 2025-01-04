package com.cube.manage.crm.repository;

import com.cube.manage.crm.entity.Product;
import com.cube.manage.crm.esrepo.ProductEsRepository;
import com.cube.manage.crm.esdocument.ProductEs;
import com.cube.manage.crm.response.ProductResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
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
            productResponse.setId(Integer.parseInt(productEs.getId()));
            //ToDo:Search brand as well
            return productResponse;
        }
        Product product = productRepository.findById(Integer.valueOf(id)).orElse(null); //ToDo make query to fetch both brand and product
        if(Objects.nonNull(product)){
            BeanUtils.copyProperties(product,productResponse);
            productResponse.setId(product.getId());
            //ToDo:Search brand as well
            return productResponse;
        }
        return null;
    }

    public List<ProductResponse> fetchProductResponseDataForPage(Integer pageNo, Integer pageSize) {
        List<ProductResponse> productResponseList = new ArrayList<>();
        ProductResponse productResponse1 = new ProductResponse();
        productResponse1.setId(1);
        productResponse1.setCost(390.00);
        productResponse1.setBrandName("Rebook");
        productResponse1.setImgUrl("https://picsum.photos/id/1/5000/3333");
        productResponse1.setName("SparkShoe");
        productResponseList.add(productResponse1);
        ProductResponse productResponse2 = new ProductResponse();
        productResponse2.setId(2);
        productResponse2.setCost(340.00);
        productResponse2.setBrandName("Nike");
        productResponse2.setImgUrl("https://picsum.photos/id/2/5000/3333");
        productResponse2.setName("NikeShoe");
        productResponseList.add(productResponse2);
        ProductResponse productResponse3 = new ProductResponse();
        productResponse3.setId(3);
        productResponse3.setCost(290.00);
        productResponse3.setBrandName("Woodland");
        productResponse3.setImgUrl("https://picsum.photos/id/3/5000/3333");
        productResponse3.setName("ForestShoe");
        productResponseList.add(productResponse3);
        ProductResponse productResponse4 = new ProductResponse();
        productResponse4.setId(4);
        productResponse4.setCost(330.00);
        productResponse4.setBrandName("Jorden");
        productResponse4.setImgUrl("https://picsum.photos/id/4/5000/3333");
        productResponse4.setName("AirShoe");
        productResponseList.add(productResponse4);
        return productResponseList;

    }
}
