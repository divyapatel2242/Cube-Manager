package com.cube.manage.crm.request;

import lombok.Data;

@Data
public class ProductPredictionRequest {

    private String productName;
    private String brandName;
    private Double sellingPrice;

}
