package com.example.lab8.service;

import com.example.lab8.model.Customer;
import com.example.lab8.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer findByName(String name) {
        return customerRepository.findByName(name);
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        // Метод теперь просто сохраняет Customer без манипуляций с паролем/ролями
        return customerRepository.save(customer);
    }
}