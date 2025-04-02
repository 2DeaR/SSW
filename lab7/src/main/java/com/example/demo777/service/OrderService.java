package com.example.demo777.service;

import com.example.demo777.model.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    List<Order> findOrdersByCriteria(
            String deliveryAddress,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String paymentMethod,
            String customerName,
            String paymentStatus,
            String orderStatus
    );
}