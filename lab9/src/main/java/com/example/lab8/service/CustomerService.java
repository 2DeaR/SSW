package com.example.lab8.service;

import com.example.lab8.model.Customer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomerService extends UserDetailsService {
    Customer findByName(String name);
    Customer saveCustomer(Customer customer);

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}