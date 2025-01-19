package com.cube.manage.crm.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class OOStockDateRequest {
    private Integer instockQuantity;
    private String brand;
    private String productType;
    private String warehouse;
    private LocalDate instockDate;
}
