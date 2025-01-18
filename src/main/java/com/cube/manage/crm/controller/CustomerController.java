package com.cube.manage.crm.controller;

import com.cube.manage.crm.entity.Customer;
import com.cube.manage.crm.request.CustomerRequest;
import com.cube.manage.crm.request.SearchCustomer;
import com.cube.manage.crm.request.UserRegisterRequest;
import com.cube.manage.crm.response.CustomerDataResponse;
import com.cube.manage.crm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping(value = "/get-customer")
    public CustomerDataResponse getCustomer(@RequestBody SearchCustomer searchCustomer){
        return customerService.fetchCustomersResponseService(searchCustomer);
    }

    @PostMapping("/add-customer")
    public ResponseEntity<String> addCustomer(@RequestBody CustomerRequest customer){
        customerService.addCustomerDetails(customer);
        return ResponseEntity.ok("Customer Added");
    }

}
