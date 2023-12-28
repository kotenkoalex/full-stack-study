package com.kotenko.fullstack.customer;

import com.kotenko.fullstack.exception.DuplicateResourceException;
import com.kotenko.fullstack.exception.RequestValidationException;
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
        assertThatThrownBy(() -> underTest.persistCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("customer with email: %s exists".formatted(request.getEmail()));
        Mockito.verify(customerDao, Mockito.never()).persistCustomer(any());
    }

    @Test
    void canUpdateAllCustomerProperties() {
        int id = 1;
        Customer customer = new Customer(id, "Alex", "alex@gmail.com", 19);
        Mockito.when(customerDao.fetchCustomerById(id)).thenReturn(Optional.of(customer));
        var newEmail = "alexandr@gmail.com";
        CustomerRequest customerRequest = new CustomerRequest("Alexandr", newEmail, 23);
        Mockito.when(customerDao.customerWithEmailExists(newEmail)).thenReturn(false);
        underTest.updateCustomer(id, customerRequest);
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer cupturedCustomer = customerArgumentCaptor.getValue();
        assertThat(cupturedCustomer.getAge()).isEqualTo(customerRequest.getAge());
        assertThat(cupturedCustomer.getName()).isEqualTo(customerRequest.getName());
        assertThat(cupturedCustomer.getEmail()).isEqualTo(customerRequest.getEmail());
    }

    @Test
    void canUpdateOnlyCustomerName() {
        int id = 1;
        Customer customer = new Customer(id, "Alex", "alex@gmail.com", 19);
        Mockito.when(customerDao.fetchCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerRequest customerRequest = new CustomerRequest("Alexandr", null, null);
        underTest.updateCustomer(id, customerRequest);
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer cupturedCustomer = customerArgumentCaptor.getValue();
        assertThat(cupturedCustomer.getName()).isEqualTo(customerRequest.getName());
        assertThat(cupturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(cupturedCustomer.getEmail()).isEqualTo(customer.getEmail());
    }

    @Test
    void canUpdateOnlyCustomerEmail() {
        int id = 1;
        Customer customer = new Customer(id, "Alex", "alex@gmail.com", 19);
        Mockito.when(customerDao.fetchCustomerById(id)).thenReturn(Optional.of(customer));
        var newEmail = "alexandr@gmail.com";
        CustomerRequest customerRequest = new CustomerRequest(null, newEmail, null);
        Mockito.when(customerDao.customerWithEmailExists(newEmail)).thenReturn(false);
        underTest.updateCustomer(id, customerRequest);
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer cupturedCustomer = customerArgumentCaptor.getValue();
        assertThat(cupturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(cupturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(cupturedCustomer.getEmail()).isEqualTo(customerRequest.getEmail());
    }

    @Test
    void canUpdateOnlyCustomerAge() {
        int id = 1;
        Customer customer = new Customer(id, "Alex", "alex@gmail.com", 19);
        Mockito.when(customerDao.fetchCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerRequest customerRequest = new CustomerRequest(null, null, 20);
        underTest.updateCustomer(id, customerRequest);
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer cupturedCustomer = customerArgumentCaptor.getValue();
        assertThat(cupturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(cupturedCustomer.getAge()).isEqualTo(customerRequest.getAge());
        assertThat(cupturedCustomer.getEmail()).isEqualTo(customer.getEmail());
    }

    @Test
    void willThrowWhenTryToUpdateCustomerEmailWhenAlreadyTaken() {
        int id = 1;
        Customer customer = new Customer(id, "Alex", "alex@gmail.com", 19);
        Mockito.when(customerDao.fetchCustomerById(id)).thenReturn(Optional.of(customer));
        var newEmail = "alexandr@gmail.com";
        CustomerRequest customerRequest = new CustomerRequest(null, newEmail, null);
        Mockito.when(customerDao.customerWithEmailExists(newEmail)).thenReturn(true);
        assertThatThrownBy(() -> underTest.updateCustomer(id, customerRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");
        Mockito.verify(customerDao, Mockito.never()).updateCustomer(any());
    }

    @Test
    void willThrowWhenCustomerUpdateHasNoChanges() {
        int id = 1;
        Customer customer = new Customer(id, "Alex", "alex@gmail.com", 19);
        Mockito.when(customerDao.fetchCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerRequest customerRequest = new CustomerRequest(customer.getName(), customer.getEmail(), customer.getAge());
        assertThatThrownBy(() -> underTest.updateCustomer(id, customerRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");
        Mockito.verify(customerDao, Mockito.never()).updateCustomer(any());
    }

    @Test
    void deleteCustomer() {
        var id = 1;
        Mockito.when(customerDao.customerWithIdExists(id)).thenReturn(true);
        underTest.deleteCustomer(id);
        Mockito.verify(customerDao).deleteCustomer(id);
    }

    @Test
    void willThrowWhenDeleteCustomerNotExists() {
        var id = 1;
        Mockito.when(customerDao.customerWithIdExists(id)).thenReturn(false);
        assertThatThrownBy(() -> underTest.deleteCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id: %s is not found".formatted(id));
        Mockito.verify(customerDao, Mockito.never()).deleteCustomer(id);
    }
}