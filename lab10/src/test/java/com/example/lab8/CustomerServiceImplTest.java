package com.example.lab8;

import com.example.lab8.model.value.Address;
import com.example.lab8.model.Customer;
import com.example.lab8.repository.CustomerRepository;
import com.example.lab8.service.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CustomerServiceImplTest {

    @Autowired
    private CustomerServiceImpl customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Customer primaryTestUser;
    private final String primaryUsername = "test_principal";
    private final String primaryPassword = "securePassword!1";
    private final String primaryRole = "USER";


    @BeforeEach
    void setUp() {
        customerRepository.deleteAll();

        primaryTestUser = new Customer();
        primaryTestUser.setName(primaryUsername);
        primaryTestUser.setPassword(primaryPassword);
        primaryTestUser.setRole(primaryRole);
        Address primaryAddress = new Address("742 Evergreen Terrace", "Springfield", "12345", "USA");
        primaryTestUser.setAddress(primaryAddress);

        primaryTestUser = customerService.saveCustomer(primaryTestUser);
    }

    @Test
    void shouldPersistCustomerWithEncodedPassword() {
        Customer secondaryUser = new Customer();
        secondaryUser.setName("Charlie Brown");
        secondaryUser.setPassword("goodGrief");
        secondaryUser.setRole("ADMIN");
        Address secondaryAddress = new Address("1 Peanuts Ln", "Cartoonville", "54321", "USA");
        secondaryUser.setAddress(secondaryAddress);

        Customer savedSecondaryUser = customerService.saveCustomer(secondaryUser);

        assertNotNull(savedSecondaryUser.getId());
        assertTrue(passwordEncoder.matches("goodGrief", savedSecondaryUser.getPassword()));
        assertNotEquals("goodGrief", savedSecondaryUser.getPassword());
        assertEquals("Charlie Brown", savedSecondaryUser.getName());
        assertEquals("ADMIN", savedSecondaryUser.getRole());
    }

    @Test
    void shouldRetrieveCustomerByExactName() {
        Customer found = customerService.findByName(primaryUsername);

        assertNotNull(found);
        assertEquals(primaryUsername, found.getName());
        assertEquals(primaryTestUser.getId(), found.getId());
    }

    @Test
    void shouldReturnNullIfCustomerNameDoesNotExist() {
        Customer found = customerService.findByName("non_existent_user_name");
        assertNull(found);
    }

    @Test
    void loadUserByUsernameShouldReturnCorrectDetailsWhenUserFound() {
        UserDetails userDetails = customerService.loadUserByUsername(primaryUsername);

        assertNotNull(userDetails);
        assertEquals(primaryUsername, userDetails.getUsername());
        assertEquals(primaryTestUser.getPassword(), userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());

        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + primaryRole)));
        assertEquals(1, userDetails.getAuthorities().size());
    }

    @Test
    void loadUserByUsernameShouldThrowExceptionWhenUserNotFound() {
        String phantomUsername = "phantom_user";
        assertThrows(UsernameNotFoundException.class, () -> customerService.loadUserByUsername(phantomUsername));
    }

    @Test
    void loadUserByUsernameShouldReflectAdminRoleCorrectly() {
        Customer adminUser = new Customer();
        adminUser.setName("AdminPrime");
        adminUser.setPassword("rootPass");
        adminUser.setRole("ADMIN");
        adminUser.setAddress(new Address("1 Admin Way", "SysCity", "A1A 1A1", "Rootland"));
        customerService.saveCustomer(adminUser);

        UserDetails adminDetails = customerService.loadUserByUsername("AdminPrime");

        assertNotNull(adminDetails);
        assertEquals("AdminPrime", adminDetails.getUsername());
        assertTrue(adminDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        assertEquals(1, adminDetails.getAuthorities().size());
    }
}