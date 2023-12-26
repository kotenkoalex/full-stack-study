package com.kotenko.fullstack.customer;

import com.kotenko.fullstack.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerJdbcDataAccessServiceTest extends AbstractTestcontainers {

    private CustomerJdbcDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJdbcDataAccessService(
                getJdbcTemplate(),
                customerRowMapper);
    }

    @Test
    void fetchAllCustomers() {
    }

    @Test
    void fetchCustomerById() {
    }

    @Test
    void persistCustomer() {
    }

    @Test
    void updateCustomer() {
    }

    @Test
    void deleteCustomer() {
    }

    @Test
    void customerWithEmailExists() {
    }

    @Test
    void customerWithIdExists() {
    }
}
