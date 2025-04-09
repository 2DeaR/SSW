package com.example.lab8.service;

import com.example.lab8.model.Customer;
import com.example.lab8.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService, UserDetailsService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Customer findByName(String name) {
        return customerRepository.findByName(name);
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        return customerRepository.save(customer);
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Customer customer = getCustomerByName(name);
        return buildUserDetails(customer);
    }

    private Customer getCustomerByName(String name) throws UsernameNotFoundException {
        Customer customer = findByName(name);
        if (customer == null) {
            throw new UsernameNotFoundException("Клиент не найден: " + name);
        }
        return customer;
    }

    private UserDetails buildUserDetails(Customer customer) {
        return User
                .withUsername(customer.getName())
                .password(customer.getPassword())
                .roles(customer.getRole())
                .build();
    }
}