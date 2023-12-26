package com.kotenko.fullstack.customer;

import com.kotenko.fullstack.AbstractTestcontainers;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
        Customer customer = new Customer(
                FAKER.name().firstName(),
                FAKER.internet().safeEmailAddress(), //better add UUID to be shore it's unique
                FAKER.number().numberBetween(10, 70)
        );
        underTest.persistCustomer(customer);
        List<Customer> customers = underTest.fetchAllCustomers();
        Assertions.assertThat(customers).isNotEmpty();
    }

    @Test
    void fetchCustomerById() {
        String email = FAKER.internet().safeEmailAddress();
        Customer customer = new Customer(
                FAKER.name().firstName(),
                email,
                FAKER.number().numberBetween(10, 70)
        );
        underTest.persistCustomer(customer);
        Integer id = underTest.fetchAllCustomers().stream()
                .filter(it -> it.getEmail().equals(email))
                .map(Customer::getId).findFirst().orElseThrow();
        Optional<Customer> actual = underTest.fetchCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(it -> {
                    assertThat(it.getId()).isEqualTo(id);
                    assertThat(it.getName()).isEqualTo(customer.getName());
                    assertThat(it.getEmail()).isEqualTo(customer.getEmail());
                    assertThat(it.getAge()).isEqualTo(customer.getAge());
                }
        );
    }

    @Test
    void willReturnEmptyWhenFetchCustomerById() {
        Integer id = -1;
        var actual = underTest.fetchCustomerById(id);
        assertThat(actual).isEmpty();
    }

    @Test
    void persistCustomer() {
        Integer dbSize = underTest.fetchAllCustomers().size();
        Customer customer = new Customer(
                FAKER.name().firstName(),
                FAKER.internet().safeEmailAddress(),
                FAKER.number().numberBetween(10, 70)
        );
        underTest.persistCustomer(customer);
        Integer actualDbSize = underTest.fetchAllCustomers().size();
        assertThat(dbSize).isLessThan(actualDbSize);
    }

    @Test
    void updateCustomer() {
        String email = FAKER.internet().safeEmailAddress();
        Customer customer = new Customer(
                FAKER.name().firstName(),
                email,
                FAKER.number().numberBetween(10, 70)
        );
        underTest.persistCustomer(customer);
        Customer persistedCustomer = underTest.fetchAllCustomers().stream()
                .filter(it -> it.getEmail().equals(email))
                .findFirst()
                .orElseThrow();
        String updatedEmail = customer.getEmail() + "-update";
        Customer updatedCustomer = new Customer(
                persistedCustomer.getId(),
                customer.getName() + "-update",
                updatedEmail,
                customer.getAge() + 1);
        underTest.updateCustomer(updatedCustomer);
        Optional<Customer> actualCustomer = underTest.fetchAllCustomers().stream()
                .filter(it -> it.getEmail().equals(updatedEmail))
                .findFirst();
        assertThat(actualCustomer).isPresent().hasValueSatisfying(it -> {
                    assertThat(it.getId()).isEqualTo(updatedCustomer.getId());
                    assertThat(it.getName()).isEqualTo(updatedCustomer.getName());
                    assertThat(it.getEmail()).isEqualTo(updatedCustomer.getEmail());
                    assertThat(it.getAge()).isEqualTo(updatedCustomer.getAge());
                    assertThat(it.getName()).isNotEqualTo(persistedCustomer.getName());
                    assertThat(it.getEmail()).isNotEqualTo(persistedCustomer.getEmail());
                    assertThat(it.getAge()).isNotEqualTo(persistedCustomer.getAge());
                }
        );
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
