package com.cube.manage.crm.request;

import lombok.Data;

@Data
public class SearchCustomer {
    private String email;
    private Integer customerId;
}
