package com.cube.manage.crm.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ProductDetailDto {

    private Integer id;
    private String name;
    private Double productSellingPrice;
    private Double productCost;
    private String description;
    private String img;
    private Integer size;
    private String sku;
    private String brandName;
    private Integer availableQuantity;
    private Integer skuCount;
    private Double profit;
    private String status;
    private String statusDescription;
    private Date saleDate;

}
