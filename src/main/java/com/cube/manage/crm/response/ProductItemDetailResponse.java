package com.cube.manage.crm.response;

import lombok.Data;

import java.util.List;

@Data
public class ProductItemDetailResponse {
    private String sku;
    private Integer size;
    private Integer availableInventory;
    private List<ProductOrderCount> productOrderCounts;
    private List<ProductOrderStatusCount> productOrderStatusCounts;
    private Integer totalSale;
    private Double totalProfit;
}
