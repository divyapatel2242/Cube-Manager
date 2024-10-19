package com.cube.manage.crm.controller;

import com.cube.manage.crm.entity.Customer;
import com.cube.manage.crm.request.CustomerRequest;
import com.cube.manage.crm.request.SearchCustomer;
import com.cube.manage.crm.response.CustomerDataResponse;
import com.cube.manage.crm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
@RequestMapping("/cube-manage/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/")
    public String getCustomerScreen(Model model){
        model.addAttribute("searchCustomer", new SearchCustomer());
        return "customer";
    }
//,
    @GetMapping(value = "/get-customer")
    public String getCustomer(@RequestBody SearchCustomer searchCustomer, Model model){
//        if(Objects.isNull(searchCustomer.getCustomerId()) && Objects.isNull(searchCustomer.getEmail()))
//            return "customer";
        CustomerDataResponse customerDataResponse = customerService.fetchCustomersResponseService(searchCustomer);
        model.addAttribute("customersDataResponse",customerDataResponse);
        return "customer";
    }

    @GetMapping("/add-customer")
    public String showAddCustomerForm(Model model) {
        model.addAttribute("customer", new Customer());
        return "add-customer";
    }
//@ModelAttribute("customer")
    @PostMapping("/add-customer")
    public String addCustomer(@RequestBody CustomerRequest customer, Model model){
        customerService.addCustomerDetails(customer);
        model.addAttribute("message","Customer Details Added Successfully!");
        return "add-customer";
    }

}
