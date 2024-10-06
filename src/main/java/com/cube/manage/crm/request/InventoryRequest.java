package com.cube.manage.crm.request;

import lombok.Data;

import java.util.List;

@Data
public class InventoryRequest {

    private Integer inventoryId;
    private String sku;
    private List<String> skids;
    private String productId;
    private String warehouse;

}
