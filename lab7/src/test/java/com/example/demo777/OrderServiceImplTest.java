package com.example.demo777.service;

import com.example.demo777.model.Address;
import com.example.demo777.model.Customer;
import com.example.demo777.model.Order;
import com.example.demo777.model.payment.Cash;
import com.example.demo777.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test") // Указываем профиль для тестов
class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();

        Customer customer1 = new Customer();
        customer1.setName("Ivan Ivanov");
        Address address1 = new Address();
        address1.setStreet("Lenin St 1");
        customer1.setAddress(address1);

        Customer customer2 = new Customer();
        customer2.setName("Petr Petrov");
        Address address2 = new Address();
        address2.setStreet("Mir St 2");
        customer2.setAddress(address2);

        Order order1 = new Order();
        order1.setCustomer(customer1);
        order1.setDate(LocalDateTime.of(2025, 4, 1, 10, 0));
        order1.setStatus("NEW");
        Cash cashPayment = new Cash();
        cashPayment.setAmount(100.0f);
        order1.setPayment(cashPayment);

        Order order2 = new Order();
        order2.setCustomer(customer2);
        order2.setDate(LocalDateTime.of(2025, 4, 2, 15, 0));
        order2.setStatus("COMPLETED");

        orderRepository.saveAll(Arrays.asList(order1, order2));
    }

    @Test
    void shouldFindOrdersByAddress() {
        List<Order> result = orderService.findOrdersByCriteria(
                "Lenin St", null, null, null, null, null, null);

        assertEquals(1, result.size());
        assertEquals("Ivan Ivanov", result.get(0).getCustomer().getName());
    }

    @Test
    void shouldFindOrdersByStartDate() {
        LocalDateTime startDate = LocalDateTime.of(2025, 4, 2, 0, 0);
        List<Order> result = orderService.findOrdersByCriteria(
                null, startDate, null, null, null, null, null);

        assertEquals(1, result.size());
        assertEquals("Petr Petrov", result.get(0).getCustomer().getName());
    }

    @Test
    void shouldFindOrdersByEndDate() {
        LocalDateTime endDate = LocalDateTime.of(2025, 4, 1, 23, 59);
        List<Order> result = orderService.findOrdersByCriteria(
                null, null, endDate, null, null, null, null);

        assertEquals(1, result.size());
        assertEquals("Ivan Ivanov", result.get(0).getCustomer().getName());
    }

    @Test
    void shouldFindOrdersByPaymentMethod() {
        List<Order> result = orderService.findOrdersByCriteria(
                null, null, null, "Cash", null, null, null);

        assertEquals(1, result.size());
        assertEquals("Ivan Ivanov", result.get(0).getCustomer().getName());
    }

    @Test
    void shouldFindOrdersByCustomerName() {
        List<Order> result = orderService.findOrdersByCriteria(
                null, null, null, null, "Petr", null, null);

        assertEquals(1, result.size());
        assertEquals("Petr Petrov", result.get(0).getCustomer().getName());
    }

    @Test
    void shouldFindOrdersByPaymentStatus() {
        List<Order> paidOrders = orderService.findOrdersByCriteria(
                null, null, null, null, null, "PAID", null);
        List<Order> unpaidOrders = orderService.findOrdersByCriteria(
                null, null, null, null, null, "UNPAID", null);

        assertEquals(1, paidOrders.size());
        assertEquals("Ivan Ivanov", paidOrders.get(0).getCustomer().getName());
        assertEquals(1, unpaidOrders.size());
        assertEquals("Petr Petrov", unpaidOrders.get(0).getCustomer().getName());
    }

    @Test
    void shouldFindOrdersByOrderStatus() {
        List<Order> result = orderService.findOrdersByCriteria(
                null, null, null, null, null, null, "COMPLETED");

        assertEquals(1, result.size());
        assertEquals("Petr Petrov", result.get(0).getCustomer().getName());
    }

    @Test
    void shouldReturnAllOrdersWhenNoFilters() {
        List<Order> result = orderService.findOrdersByCriteria(
                null, null, null, null, null, null, null);

        assertEquals(2, result.size());
    }
}