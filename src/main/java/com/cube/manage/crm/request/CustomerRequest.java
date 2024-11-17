package com.cube.manage.crm.request;

import lombok.Data;

import java.util.Date;

@Data
public class CustomerRequest {

    private Integer id;

    private String name;

    private String address;

    private String mobile;

    private String email;

    private Date createdDate;

    private String customerType;
}
