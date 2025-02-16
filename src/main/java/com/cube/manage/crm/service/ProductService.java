package com.cube.manage.crm.service;

import com.cube.manage.crm.dto.ProductDetailDto;
import com.cube.manage.crm.entity.Product;
import com.cube.manage.crm.entity.ProductItem;
//import com.cube.manage.crm.esrepo.ProductEsRepository;
//import com.cube.manage.crm.esrepo.ProductItemEsRepository;
//import com.cube.manage.crm.esdocument.ProductEs;
//import com.cube.manage.crm.esdocument.ProductItemEs;
import com.cube.manage.crm.repository.ProductDataRepository;
import com.cube.manage.crm.repository.ProductItemRepository;
import com.cube.manage.crm.repository.ProductRepository;
import com.cube.manage.crm.request.ProductItemRequest;
import com.cube.manage.crm.request.ProductRequest;
import com.cube.manage.crm.response.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

//    @Autowired
//    private ProductEsRepository productEsRepository;

    @Autowired
    private ProductDataRepository productDataRepository;

    @Autowired
    private ProductItemRepository productItemRepository;

//    @Autowired
//    private ProductItemEsRepository productItemEsRepository;

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
//        ProductEs productEs = new ProductEs();
//        productEs.setId(product.getId().toString());
//        BeanUtils.copyProperties(product,productEs);
//        productEsRepository.save(productEs);

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
//        ProductEs productEs = new ProductEs();
//        productEs.setId(product.getId().toString());
//        BeanUtils.copyProperties(product,productEs);
//        productEsRepository.save(productEs);
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

//        List<ProductItemEs> productItemEsList = new ArrayList<>();
//        for(ProductItem productItem : productItemList){
//            ProductItemEs productItemEs = new ProductItemEs();
//            BeanUtils.copyProperties(productItem,productItemEs);
//            productItemEsList.add(productItemEs);
//        }
//        productItemEsRepository.saveAll(productItemEsList);
    }

    public ProductResponseData fetchProductData(String id) {
        ProductResponseData productResponseData = productDataRepository.fetchProductResponseData(id);
        if(Objects.isNull(productResponseData))
            throw new RuntimeException("Product is not available");
        return productResponseData;
    }

    public ProductResponse fetchProductsAsPerPage(Integer pageNo, Integer pageSize){
        ProductResponse productResponse = new ProductResponse();
        List<ProductResponseData> productResponseData = productDataRepository.fetchProductResponseDataForPage(pageNo, pageSize);
        Integer totalSize = productDataRepository.totalSizeOfProduct();
        if(Objects.isNull(productResponseData))
            throw new RuntimeException("Product is not available");
        productResponse.setTotalSize(totalSize);
        productResponse.setProductResponseData(productResponseData);
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
        return brandService.fetchBrandsData();
    }

    public ProductDetailResponse fetchProductDetailData(String id) {
        ProductDetailResponse productDetailResponse = new ProductDetailResponse();
        List<ProductDetailDto> productDetailDtos = productDataRepository.fetchProductData(id);
        productDetailResponse.setDescription(productDetailDtos.get(0).getDescription());
        productDetailResponse.setId(Integer.valueOf(id));
        productDetailResponse.setBrandName(productDetailDtos.get(0).getBrandName());
        productDetailResponse.setName(productDetailDtos.get(0).getName());
        productDetailResponse.setImg(productDetailDtos.get(0).getImg());
        productDetailResponse.setCurrentAvailableTotalInventory(productDetailDtos.stream().mapToInt(ProductDetailDto::getAvailableQuantity).sum());
        productDetailResponse.setTotalProfit(productDetailDtos.stream().mapToDouble(ProductDetailDto::getProfit).sum());
        productDetailResponse.setTotalSale(productDetailDtos.stream().mapToInt(ProductDetailDto::getSkuCount).sum());

        Map<String, List<ProductDetailDto>> mapOfSkuProductDetail = productDetailDtos.stream().collect(Collectors.groupingBy(ProductDetailDto::getSku));
        Map<String, List<ProductDetailDto>> mapOfDateProductDetail = productDetailDtos.stream().collect(Collectors.groupingBy(productDetailDto -> productDetailDto.getSaleDate().toString()));
        Map<String, List<ProductDetailDto>> mapOfStatusProductDetail = productDetailDtos.stream().collect(Collectors.groupingBy(ProductDetailDto::getStatus));

        List<ProductItemDetailResponse> productItemDetailResponses = new ArrayList<>();
        for(Map.Entry<String, List<ProductDetailDto>> entry: mapOfSkuProductDetail.entrySet()){
            ProductItemDetailResponse productItemDetailResponse = new ProductItemDetailResponse();
            productItemDetailResponse.setSize(entry.getValue().get(0).getSize());
            productItemDetailResponse.setTotalProfit(entry.getValue().stream().mapToDouble(ProductDetailDto::getProfit).sum());
            productItemDetailResponse.setSku(entry.getKey());
            productItemDetailResponse.setAvailableInventory(entry.getValue().stream().mapToInt(ProductDetailDto::getAvailableQuantity).sum());
            productItemDetailResponse.setTotalSale(entry.getValue().stream().mapToInt(ProductDetailDto::getSkuCount).sum());

            Map<String, List<ProductDetailDto>> mapOfSkuDateProductDetail = entry.getValue().stream().collect(Collectors.groupingBy(productDetailDto -> productDetailDto.getSaleDate().toString()));
            Map<String, List<ProductDetailDto>> mapOfSkuStatusProductDetail = entry.getValue().stream().collect(Collectors.groupingBy(ProductDetailDto::getStatus));
            List<ProductOrderCount> productOrderCounts = new ArrayList<>();
            for(Map.Entry<String, List<ProductDetailDto>> entryDate: mapOfSkuDateProductDetail.entrySet()){
                ProductOrderCount productOrderCount = new ProductOrderCount();
                productOrderCount.setSaleDate(entryDate.getKey());
                productOrderCount.setSaleCount(entryDate.getValue().stream().mapToInt(ProductDetailDto::getSkuCount).sum());
                productOrderCount.setSaleProfit(entryDate.getValue().stream().mapToDouble(ProductDetailDto::getProfit).sum());
                productOrderCounts.add(productOrderCount);
            }
            productItemDetailResponse.setProductOrderCounts(productOrderCounts);

            List<ProductOrderStatusCount> productOrderStatusCounts = new ArrayList<>();
            for(Map.Entry<String, List<ProductDetailDto>> entryStatus: mapOfSkuStatusProductDetail.entrySet()){
                ProductOrderStatusCount productOrderStatusCount = new ProductOrderStatusCount();
                productOrderStatusCount.setStatus(entryStatus.getKey());
                productOrderStatusCount.setStatusDescription(entryStatus.getValue().get(0).getStatusDescription());
                productOrderStatusCount.setStatusCount(entryStatus.getValue().stream().mapToInt(ProductDetailDto::getSkuCount).sum());
                productOrderStatusCounts.add(productOrderStatusCount);
            }
            productItemDetailResponse.setProductOrderStatusCounts(productOrderStatusCounts);
            productItemDetailResponses.add(productItemDetailResponse);
        }
        productDetailResponse.setProductItemDetailResponses(productItemDetailResponses);

        List<ProductOrderCount> productOrderCounts = new ArrayList<>();
        for(Map.Entry<String, List<ProductDetailDto>> entryDate: mapOfDateProductDetail.entrySet()){
            ProductOrderCount productOrderCount = new ProductOrderCount();
            productOrderCount.setSaleDate(entryDate.getKey());
            productOrderCount.setSaleCount(entryDate.getValue().stream().mapToInt(ProductDetailDto::getSkuCount).sum());
            productOrderCount.setSaleProfit(entryDate.getValue().stream().mapToDouble(ProductDetailDto::getProfit).sum());
            productOrderCounts.add(productOrderCount);
        }
        productDetailResponse.setProductOrderCounts(productOrderCounts);

        List<ProductOrderStatusCount> productOrderStatusCounts = new ArrayList<>();
        for(Map.Entry<String, List<ProductDetailDto>> entryStatus: mapOfStatusProductDetail.entrySet()){
            ProductOrderStatusCount productOrderStatusCount = new ProductOrderStatusCount();
            productOrderStatusCount.setStatus(entryStatus.getKey());
            productOrderStatusCount.setStatusDescription(entryStatus.getValue().get(0).getStatusDescription());
            productOrderStatusCount.setStatusCount(entryStatus.getValue().stream().mapToInt(ProductDetailDto::getSkuCount).sum());
            productOrderStatusCounts.add(productOrderStatusCount);
        }
        productDetailResponse.setProductOrderStatusCounts(productOrderStatusCounts);
        return productDetailResponse;

    }
}
