package com.example.demo777.controller;

import com.example.demo777.model.Order;
import com.example.demo777.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/search")
    public ResponseEntity<List<Order>> searchOrders(
            @RequestParam(required = false) String deliveryAddress,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String paymentMethod,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String paymentStatus,
            @RequestParam(required = false) String orderStatus
    ) {
        List<Order> orders = orderService.findOrdersByCriteria(
                deliveryAddress, startDate, endDate, paymentMethod, customerName, paymentStatus, orderStatus
        );
        return ResponseEntity.ok(orders);
    }
}