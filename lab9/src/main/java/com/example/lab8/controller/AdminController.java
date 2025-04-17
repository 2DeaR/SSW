package com.example.lab8.controller;

import com.example.lab8.model.Order;
import com.example.lab8.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final OrderService orderService;

    @GetMapping("/monitor")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Order>> monitorOrders() {
        List<Order> orders = orderService.findOrdersByCriteria(
                null, null, null, null, null, null, null
        );
        return ResponseEntity.ok(orders);
    }
}