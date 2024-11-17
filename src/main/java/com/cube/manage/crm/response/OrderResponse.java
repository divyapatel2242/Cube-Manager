package com.cube.manage.crm.response;

import lombok.Data;

@Data
public class OrderResponse {

    private Boolean isSuccess;
    private String successMessage;
    private String errorMessage;

}
