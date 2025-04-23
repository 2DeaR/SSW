package com.example.lab8;

import com.example.lab8.model.value.Address;
import com.example.lab8.model.Customer;
import com.example.lab8.model.Order;
import com.example.lab8.repository.CustomerRepository;
import com.example.lab8.repository.OrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SecurityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private Customer testUser;
    private Customer testAdmin;
    private Order testOrder;


    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        customerRepository.deleteAll();

        testUser = new Customer();
        testUser.setName("regular_joe");
        testUser.setPassword(passwordEncoder.encode("userPass123"));
        testUser.setRole("USER");
        Address userAddress = new Address("1 User Ave", "Userton", "U1U 1U1", "Userland");
        testUser.setAddress(userAddress);
        testUser = customerRepository.save(testUser);

        testAdmin = new Customer();
        testAdmin.setName("super_admin");
        testAdmin.setPassword(passwordEncoder.encode("adminPass456"));
        testAdmin.setRole("ADMIN");
        Address adminAddress = new Address("1 Admin Blvd", "Adminville", "A1A 1A1", "Adminland");
        testAdmin.setAddress(adminAddress);
        testAdmin = customerRepository.save(testAdmin);

        testOrder = new Order();
        testOrder.setCustomer(testUser);
        testOrder.setDate(LocalDateTime.of(2024, 5, 1, 12, 0));
        testOrder.setStatus("PENDING");
        orderRepository.save(testOrder);
    }

    @Test
    void shouldRegisterNewCustomerSuccessfully() throws Exception {
        Customer freshCustomer = new Customer();
        freshCustomer.setName("newbie");
        freshCustomer.setPassword("freshPass");
        freshCustomer.setRole("USER");
        Address freshAddress = new Address("1 Fresh St", "Newtown", "N3W B13", "Newland");
        freshCustomer.setAddress(freshAddress);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(freshCustomer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("newbie"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void shouldPermitUserToAccessOrderSearch() throws Exception {
        String authHeader = "Basic " + Base64.getEncoder().encodeToString("regular_joe:userPass123".getBytes());

        mockMvc.perform(get("/api/orders/search")
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldPermitAdminToAccessOrderSearch() throws Exception {
        String authHeader = "Basic " + Base64.getEncoder().encodeToString("super_admin:adminPass456".getBytes());

        mockMvc.perform(get("/api/orders/search")
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldPermitAdminToAccessAdminMonitor() throws Exception {
        String authHeader = "Basic " + Base64.getEncoder().encodeToString("super_admin:adminPass456".getBytes());

        mockMvc.perform(get("/api/admin/monitor")
                        .header("Authorization", authHeader))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldForbidUserFromAccessingAdminMonitor() throws Exception {
        String authHeader = "Basic " + Base64.getEncoder().encodeToString("regular_joe:userPass123".getBytes());

        mockMvc.perform(get("/api/admin/monitor")
                        .header("Authorization", authHeader))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnUnauthorizedForOrderSearchWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/orders/search"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedForAdminMonitorWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/admin/monitor"))
                .andExpect(status().isUnauthorized());
    }
}