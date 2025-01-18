package com.cube.manage.crm.request;

import lombok.Data;

import java.util.List;

@Data
public class ProductRequest {

    private Integer id;
    private String name;
    private Integer brand;
    private String description;
    private String imgUrl;
    private Double costPrice;
    private Double salePrice;
    private List<ProductItemRequest> productItemRequests;
}
