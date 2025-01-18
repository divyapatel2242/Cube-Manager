package com.cube.manage.crm.response;

import lombok.Data;

@Data
public class ProductOrderStatusCount {
    private Integer statusCount;
    private String status;
    private String statusDescription;
}
