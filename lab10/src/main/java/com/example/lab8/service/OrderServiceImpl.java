package com.example.lab8.service;

import com.example.lab8.model.Order;
import com.example.lab8.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    public List<Order> findOrdersByCriteria(
            String deliveryAddress,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String paymentMethod,
            String customerName,
            String paymentStatus,
            String orderStatus
    ) {
        List<Order> orders = new ArrayList<>(orderRepository.findAll());

        if (deliveryAddress != null) {
            orders.removeIf(order -> !filterByAddress(order, deliveryAddress));
        }
        if (startDate != null) {
            orders.removeIf(order -> !filterByStartDate(order, startDate));
        }
        if (endDate != null) {
            orders.removeIf(order -> !filterByEndDate(order, endDate));
        }
        if (paymentMethod != null) {
            orders.removeIf(order -> !filterByPaymentMethod(order, paymentMethod));
        }
        if (customerName != null) {
            orders.removeIf(order -> !filterByCustomerName(order, customerName));
        }
        if (paymentStatus != null) {
            orders.removeIf(order -> !filterByPaymentStatus(order, paymentStatus));
        }
        if (orderStatus != null) {
            orders.removeIf(order -> !filterByOrderStatus(order, orderStatus));
        }

        return orders;
    }

    private boolean filterByAddress(Order order, String deliveryAddress) {
        return order.getCustomer() != null &&
                order.getCustomer().getAddress() != null &&
                order.getCustomer().getAddress().getStreet().contains(deliveryAddress);
    }

    private boolean filterByStartDate(Order order, LocalDateTime startDate) {
        return !order.getDate().isBefore(startDate);
    }

    private boolean filterByEndDate(Order order, LocalDateTime endDate) {
        return !order.getDate().isAfter(endDate);
    }

    private boolean filterByPaymentMethod(Order order, String paymentMethod) {
        return order.getPayment() != null &&
                order.getPayment().getClass().getSimpleName().equalsIgnoreCase(paymentMethod);
    }

    private boolean filterByCustomerName(Order order, String customerName) {
        return order.getCustomer() != null &&
                order.getCustomer().getName().contains(customerName);
    }

    private boolean filterByPaymentStatus(Order order, String paymentStatus) {
        if (paymentStatus.equals("PAID")) return order.getPayment() != null;
        if (paymentStatus.equals("UNPAID")) return order.getPayment() == null;
        return true;
    }

    private boolean filterByOrderStatus(Order order, String orderStatus) {
        return order.getStatus().equalsIgnoreCase(orderStatus);
    }
}