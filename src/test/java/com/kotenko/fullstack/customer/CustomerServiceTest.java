package com.kotenko.fullstack.customer;

import com.kotenko.fullstack.exception.DuplicateResourceException;
import com.kotenko.fullstack.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private CustomerDao customerDao;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomers() {
        underTest.getAllCustomers();
        Mockito.verify(customerDao).fetchAllCustomers();
    }

    @Test
    void canGetCustomer() {
        int id = 1;
        Customer customer = new Customer(id, "Alex", "alex@gmail.com", 19);
        Mockito.when(customerDao.fetchCustomerById(id)).thenReturn(Optional.of(customer));
        Customer actual = underTest.getCustomer(id);
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willThrowWhenGetCustomerReturnsEmptyOptional() {
        int id = 1;
        Mockito.when(customerDao.fetchCustomerById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id: %s is not found".formatted(id));
    }

    @Test
    void persistCustomer() {
        var email = "alex@gmail.com";
        Mockito.when(customerDao.customerWithEmailExists(email)).thenReturn(false);
        CustomerRequest request = new CustomerRequest("alex", email, 19);
        underTest.persistCustomer(request);
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerDao).persistCustomer(customerArgumentCaptor.capture());
        Customer cupturedCustomer = customerArgumentCaptor.getValue();
        assertThat(cupturedCustomer.getId()).isNull();
        assertThat(cupturedCustomer.getName()).isEqualTo(request.getName());
        assertThat(cupturedCustomer.getEmail()).isEqualTo(request.getEmail());
        assertThat(cupturedCustomer.getAge()).isEqualTo(request.getAge());
    }

    @Test
    void willThrowWhenEmailExistsWhilePersistingCustomer() {
        var email = "alex@gmail.com";
        Mockito.when(customerDao.customerWithEmailExists(email)).thenReturn(true);
        CustomerRequest request = new CustomerRequest("alex", email, 19);
        assertThatThrownBy(()->underTest.persistCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("customer with email: %s exists".formatted(request.getEmail()));
        Mockito.verify(customerDao, Mockito.never()).persistCustomer(any());
    }

    @Test
    void updateCustomer() {

    }

    @Test
    void deleteCustomer() {

    }
}