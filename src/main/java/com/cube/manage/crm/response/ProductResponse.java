package com.cube.manage.crm.response;

import lombok.Data;

import java.util.List;

@Data
public class ProductResponse {

    private Integer totalSize;
    private List<ProductResponseData> productResponseData;

}
