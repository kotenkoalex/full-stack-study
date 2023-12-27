package com.kotenko.fullstack.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class CustomerJpaDataAccessServiceTest {
    private CustomerJpaDataAccessService underTest;
    @Mock
    private CustomerRepository customerRepository;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJpaDataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void fetchAllCustomers() {
        underTest.fetchAllCustomers();
        Mockito.verify(customerRepository).findAll();
    }

    @Test
    void fetchCustomerById() {
        var id = 1;
        underTest.fetchCustomerById(id);
        Mockito.verify(customerRepository).findById(id);
    }

    @Test
    void persistCustomer() {
        Customer customer = new Customer(1, "Alex", "alex@gmail.com", 22);
        underTest.persistCustomer(customer);
        Mockito.verify(customerRepository).save(customer);
    }

    @Test
    void updateCustomer() {
        Customer customer = new Customer(1, "Alex", "alex@gmail.com", 22);
        underTest.updateCustomer(customer);
        Mockito.verify(customerRepository).save(customer);
    }

    @Test
    void deleteCustomer() {
        var id = 1;
        underTest.deleteCustomer(id);
        Mockito.verify(customerRepository).deleteById(id);
    }

    @Test
    void customerWithEmailExists() {
        var email = "alex@gmail.com";
        underTest.customerWithEmailExists(email);
        Mockito.verify(customerRepository).existsByEmail(email);
    }

    @Test
    void customerWithIdExists() {
        var id = 1;
        underTest.customerWithIdExists(id);
        Mockito.verify(customerRepository).existsById(id);
    }
}
