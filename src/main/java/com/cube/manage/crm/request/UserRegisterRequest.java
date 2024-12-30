package com.cube.manage.crm.request;

import lombok.Data;

@Data
public class UserRegisterRequest {

    private Integer id;
    private String name;
    private String address;
    private String mobile;
    private String email;
    private String username;
    private String password;
    private String usertype;

}
