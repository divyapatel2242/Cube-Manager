package com.cube.manage.crm.request;

import lombok.Data;

@Data
public class OrderRequest {

    private Integer customerId;
    private String name;
    private String address;
    private String city;
    private String state;
    private String pinCode;
    private String sku;
    private Integer quantity;
    private Double payableAmount;
    private String typeOfPayment;
    private Double payViaCredits;
    private Double payViaOnline;

}
