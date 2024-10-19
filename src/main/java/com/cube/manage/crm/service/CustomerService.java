package com.cube.manage.crm.service;

import com.cube.manage.crm.entity.Customer;
import com.cube.manage.crm.esrepo.CustomerEsRepository;
import com.cube.manage.crm.esdocument.CustomerEs;
import com.cube.manage.crm.repository.CustomerRepository;
import com.cube.manage.crm.request.CustomerRequest;
import com.cube.manage.crm.request.SearchCustomer;
import com.cube.manage.crm.response.CustomerData;
import com.cube.manage.crm.response.CustomerDataResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerEsRepository customerEsRepository;

    public CustomerDataResponse fetchCustomersResponseService(SearchCustomer searchCustomer){
        CustomerDataResponse customerDataResponse = new CustomerDataResponse();
        Iterable<CustomerEs> customerEs = customerEsRepository.findAll();
        Iterator<CustomerEs> iterator = customerEs.iterator();
        List<CustomerData> customerData = new ArrayList<>();
        if (iterator.hasNext()) {
            customerData.add(fetchCustomerData(iterator.next()));
        }
        while (iterator.hasNext()) {
            customerData.add(fetchCustomerData(iterator.next()));
        }
        customerDataResponse.setCustomerData(customerData);
        return customerDataResponse;
    }

    private CustomerData fetchCustomerData(CustomerEs customer){
        CustomerData customerData = new CustomerData();
        BeanUtils.copyProperties(customer,customerData);
        return customerData;
    }

    public void addCustomerDetails(CustomerRequest customerRequest) {
        CustomerEs customerEs = new CustomerEs();
        BeanUtils.copyProperties(customerRequest,customerEs);
        customerRequest.setCreatedDate(new Date());
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerRequest,customer);
        customerRepository.save(customer);
        customerEs.setId(customer.getId());
        customerEsRepository.save(customerEs);
    }
}
