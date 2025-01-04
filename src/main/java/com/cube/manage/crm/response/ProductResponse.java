package com.cube.manage.crm.response;

import lombok.Data;

@Data
public class ProductResponse {

    private Integer id;
    private String name;
    private String brandName;
    private String imgUrl;
    private Double cost;

}
