package com.example.lab8.service;

import com.example.lab8.model.Customer;

public interface CustomerService {
    Customer findByName(String name);
    Customer saveCustomer(Customer customer);
}