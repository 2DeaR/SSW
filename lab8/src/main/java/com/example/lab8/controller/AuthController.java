package com.example.lab8.controller;

import com.example.lab8.model.Customer;
import com.example.lab8.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<Customer> register(@RequestBody Customer customer) {
        String role = customer.getRole();
        if (role == null || (!role.equals("USER") && !role.equals("ADMIN"))) {
            customer.setRole("USER");
        }
        Customer savedCustomer = customerService.saveCustomer(customer);
        return ResponseEntity.ok(savedCustomer);
    }
}