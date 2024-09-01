package com.cube.manage.crm.service;

import com.cube.manage.crm.entity.Customer;
import com.cube.manage.crm.repository.CustomerRepository;
import com.cube.manage.crm.request.SearchCustomer;
import com.cube.manage.crm.response.CustomerDataResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public CustomerDataResponse fetchCustomersResponseService(SearchCustomer searchCustomer){
        return null;
    }

    public void addCustomerDetails(Customer customer) {
        customerRepository.save(customer);
    }
}
