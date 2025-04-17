package com.example.lab8.service;

import com.example.lab8.model.Order;

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