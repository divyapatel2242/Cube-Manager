package com.cube.manage.crm.response;

import lombok.Data;

import java.util.Date;

@Data
public class ProductOrderCount {
    private Integer saleCount;
    private Date saleDate;
    private Double saleProfit;
}
