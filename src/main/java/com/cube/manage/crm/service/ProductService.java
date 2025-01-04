package com.cube.manage.crm.service;

import com.cube.manage.crm.entity.Product;
import com.cube.manage.crm.entity.ProductItem;
import com.cube.manage.crm.esrepo.ProductEsRepository;
import com.cube.manage.crm.esrepo.ProductItemEsRepository;
import com.cube.manage.crm.esdocument.ProductEs;
import com.cube.manage.crm.esdocument.ProductItemEs;
import com.cube.manage.crm.repository.ProductDataRepository;
import com.cube.manage.crm.repository.ProductItemRepository;
import com.cube.manage.crm.repository.ProductRepository;
import com.cube.manage.crm.request.ProductItemRequest;
import com.cube.manage.crm.request.ProductRequest;
import com.cube.manage.crm.response.BrandIdNameResponse;
import com.cube.manage.crm.response.ProductResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductEsRepository productEsRepository;

    @Autowired
    private ProductDataRepository productDataRepository;

    @Autowired
    private ProductItemRepository productItemRepository;

    @Autowired
    private ProductItemEsRepository productItemEsRepository;

    @Autowired
    private BrandService brandService;


    @Transactional
    public void addProduct(ProductRequest productRequest) {
        if(Objects.isNull(productRequest.getId())){
            addNewProduct(productRequest);
            return;
        }
        updateProduct(productRequest);
    }

    private void updateProduct(ProductRequest productRequest) {
        Product product =  new Product();
        product.setId(productRequest.getId());
        product.setName(productRequest.getName());
        product.setBrandId(productRequest.getBrand());
        product.setCreatedDate(new Date());
        product.setImgUrl(productRequest.getImgUrl());
        product.setDescription(productRequest.getDescription());
        productRepository.save(product);
        ProductEs productEs = new ProductEs();
        productEs.setId(product.getId().toString());
        BeanUtils.copyProperties(product,productEs);
        productEsRepository.save(productEs);

    }

    private void addNewProduct(ProductRequest productRequest) {
        Product product =  new Product();
        product.setName(productRequest.getName());
        product.setBrandId(productRequest.getBrand());
        product.setCreatedDate(new Date());
        product.setImgUrl(productRequest.getImgUrl());
        product.setDescription(productRequest.getDescription());
        product.setRetailPrice(productRequest.getCostPrice());
        product.setMrp(productRequest.getSalePrice());
        productRepository.save(product);
        ProductEs productEs = new ProductEs();
        productEs.setId(product.getId().toString());
        BeanUtils.copyProperties(product,productEs);
        productEsRepository.save(productEs);
        productRequest.setId(product.getId());
        addProductItemDetails(productRequest);
    }

    private void addProductItemDetails(ProductRequest productRequest) {
        List<ProductItem> productItemList = new ArrayList<>();
        for (ProductItemRequest productItemRequest : productRequest.getProductItemRequests()){
            ProductItem productItem = new ProductItem();
            productItem.setProductId(productRequest.getId());
            productItem.setSku(productItemRequest.getSku());
            productItem.setSize(productItemRequest.getSize());
            productItem.setCreatedDate(new Date());
            productItemList.add(productItem);
        }
        productItemRepository.saveAll(productItemList);

        List<ProductItemEs> productItemEsList = new ArrayList<>();
        for(ProductItem productItem : productItemList){
            ProductItemEs productItemEs = new ProductItemEs();
            BeanUtils.copyProperties(productItem,productItemEs);
            productItemEsList.add(productItemEs);
        }
        productItemEsRepository.saveAll(productItemEsList);
    }

    public ProductResponse fetchProductData(String id) {
        ProductResponse productResponse = productDataRepository.fetchProductResponseData(id);
        if(Objects.isNull(productResponse))
            throw new RuntimeException("Product is not available");
        return productResponse;
    }

    public List<ProductResponse> fetchProductsAsPerPage(Integer pageNo, Integer pageSize){
        List<ProductResponse> productResponse = productDataRepository.fetchProductResponseDataForPage(pageNo, pageSize);
        if(Objects.isNull(productResponse))
            throw new RuntimeException("Product is not available");
        return productResponse;
    }

    @Transactional
    public void addProductItem(ProductRequest productRequest) {
        addProductItemDetails(productRequest);
    }

    public void validateSku(String sku) {
       Integer count = productItemRepository.validateSku(sku);
       if(count != 0)
           throw new RuntimeException("Sku is not Valid");
    }

    public List<BrandIdNameResponse> fetchBrands() {
        return brandService.fetchAllBrands();
    }
}
