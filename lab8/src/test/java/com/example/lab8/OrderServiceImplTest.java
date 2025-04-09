package com.example.lab8;

import com.example.lab8.model.value.Address;
import com.example.lab8.model.Customer;
import com.example.lab8.model.Order;
import com.example.lab8.model.payment.Cash;
import com.example.lab8.model.payment.Payment;
import com.example.lab8.repository.CustomerRepository;
import com.example.lab8.repository.OrderRepository;
import com.example.lab8.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class OrderServiceImplTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer savedCustomerAlpha;
    private Customer savedCustomerBeta;
    private Order savedOrderOne;
    private Order savedOrderTwo;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        customerRepository.deleteAll();

        Customer customerAlpha = new Customer();
        customerAlpha.setName("Alice Green");
        customerAlpha.setPassword("passAlpha");
        customerAlpha.setRole("USER");
        Address addressAlpha = new Address("10 Downing St", "London", "SW1A 2AA", "UK");
        customerAlpha.setAddress(addressAlpha);

        Customer customerBeta = new Customer();
        customerBeta.setName("Bob White");
        customerBeta.setPassword("passBeta");
        customerBeta.setRole("USER");
        Address addressBeta = new Address("221B Baker St", "London", "NW1 6XE", "UK");
        customerBeta.setAddress(addressBeta);

        List<Customer> savedCustomers = customerRepository.saveAll(Arrays.asList(customerAlpha, customerBeta));
        savedCustomerAlpha = savedCustomers.get(0);
        savedCustomerBeta = savedCustomers.get(1);

        Order orderOne = new Order();
        orderOne.setCustomer(savedCustomerAlpha);
        orderOne.setDate(LocalDateTime.of(2024, 1, 15, 9, 30));
        orderOne.setStatus("PROCESSING");

        Cash cashPayment = new Cash();
        cashPayment.setAmount(150.75f);
        cashPayment.setCashTendered(160.0f);
        orderOne.setPayment(cashPayment);

        Order orderTwo = new Order();
        orderTwo.setCustomer(savedCustomerBeta);
        orderTwo.setDate(LocalDateTime.of(2024, 1, 16, 14, 0));
        orderTwo.setStatus("SHIPPED");

        List<Order> savedOrders = orderRepository.saveAll(Arrays.asList(orderOne, orderTwo));
        savedOrderOne = savedOrders.get(0);
        savedOrderTwo = savedOrders.get(1);
    }

    @Test
    void shouldFindOrdersByDeliveryAddressStreet() {
        List<Order> result = orderService.findOrdersByCriteria("Downing St", null, null, null, null, null, null);
        assertEquals(1, result.size());
        assertEquals(savedCustomerAlpha.getId(), result.get(0).getCustomer().getId());
    }

    @Test
    void shouldFindOrdersAfterStartDate() {
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 16, 0, 0);
        List<Order> result = orderService.findOrdersByCriteria(null, startDate, null, null, null, null, null);
        assertEquals(1, result.size());
        assertEquals(savedCustomerBeta.getId(), result.get(0).getCustomer().getId());
    }

    @Test
    void shouldFindOrdersBeforeEndDate() {
        LocalDateTime endDate = LocalDateTime.of(2024, 1, 15, 23, 59);
        List<Order> result = orderService.findOrdersByCriteria(null, null, endDate, null, null, null, null);
        assertEquals(1, result.size());
        assertEquals(savedCustomerAlpha.getId(), result.get(0).getCustomer().getId());
    }

    @Test
    void shouldFindOrdersByExactPaymentMethodClassName() {
        List<Order> result = orderService.findOrdersByCriteria(null, null, null, "Cash", null, null, null);
        assertEquals(1, result.size());
        assertEquals(savedCustomerAlpha.getId(), result.get(0).getCustomer().getId());
        assertTrue(result.get(0).getPayment() instanceof Cash);
    }

    @Test
    void shouldNotFindOrdersByIncorrectPaymentMethodClassName() {
        List<Order> result = orderService.findOrdersByCriteria(null, null, null, "CheckPayment", null, null, null);
        assertEquals(0, result.size());
    }

    @Test
    void shouldFindOrdersByPartialCustomerName() {
        List<Order> result = orderService.findOrdersByCriteria(null, null, null, null, "Bob", null, null);
        assertEquals(1, result.size());
        assertEquals(savedCustomerBeta.getId(), result.get(0).getCustomer().getId());
    }

    @Test
    void shouldFindOrdersByPaymentPresenceStatus() {
        List<Order> paidOrders = orderService.findOrdersByCriteria(null, null, null, null, null, "PAID", null);
        List<Order> unpaidOrders = orderService.findOrdersByCriteria(null, null, null, null, null, "UNPAID", null);

        assertEquals(1, paidOrders.size());
        assertEquals(savedCustomerAlpha.getId(), paidOrders.get(0).getCustomer().getId());
        assertNotNull(paidOrders.get(0).getPayment());

        assertEquals(1, unpaidOrders.size());
        assertEquals(savedCustomerBeta.getId(), unpaidOrders.get(0).getCustomer().getId());
        assertNull(unpaidOrders.get(0).getPayment());
    }

    @Test
    void shouldFindOrdersBySpecificOrderStatus() {
        List<Order> result = orderService.findOrdersByCriteria(null, null, null, null, null, null, "SHIPPED");
        assertEquals(1, result.size());
        assertEquals(savedCustomerBeta.getId(), result.get(0).getCustomer().getId());
        assertEquals("SHIPPED", result.get(0).getStatus());
    }

    @Test
    void shouldReturnAllAvailableOrdersWhenNoCriteriaProvided() {
        List<Order> result = orderService.findOrdersByCriteria(null, null, null, null, null, null, null);
        assertEquals(2, result.size());
    }

    @Test
    void shouldCombineMultipleCriteriaCorrectly() {
        List<Order> result = orderService.findOrdersByCriteria("Baker St", null, null, null, "Bob White", "UNPAID", "SHIPPED");
        assertEquals(1, result.size());
        assertEquals(savedCustomerBeta.getId(), result.get(0).getCustomer().getId());
    }
}