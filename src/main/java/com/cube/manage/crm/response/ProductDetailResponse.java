package com.cube.manage.crm.response;

import lombok.Data;

import java.util.List;

@Data
public class ProductDetailResponse {

    private Integer id;
    private String name;
    private String img;
    private String description;
    private List<ProductItemDetailResponse> productItemDetailResponses;
    private List<ProductOrderCount> productOrderCounts;
    private Integer currentAvailableTotalInventory;
    private String brandName;
    private List<ProductOrderStatusCount> productOrderStatusCounts;
    private Integer totalSale;
    private Double totalProfit;

}
