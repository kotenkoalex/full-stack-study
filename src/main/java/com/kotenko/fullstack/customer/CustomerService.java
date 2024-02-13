package com.kotenko.fullstack.customer;

import com.kotenko.fullstack.exception.RequestValidationException;
import com.kotenko.fullstack.exception.DuplicateResourceException;
import com.kotenko.fullstack.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customers) {
        this.customerDao = customers;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.fetchAllCustomers();
    }

    public Customer getCustomer(Integer id) {
        return customerDao.fetchCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException("customer with id: %s is not found".formatted(id)));
    }

    public Customer persistCustomer(CustomerRequest request) {
        if (customerDao.customerWithEmailExists(request.getEmail())) {
            throw new DuplicateResourceException("customer with email: %s exists".formatted(request.getEmail()));
        }
        return customerDao.persistCustomer(new Customer(
                request.getName(),
                request.getEmail(),
                request.getAge()
        ));
    }

    public Customer updateCustomer(Integer id, CustomerRequest request) {
        Customer customer = getCustomer(id);
        boolean update = false;
        String updatedName = request.getName();
        String updatedEmail = request.getEmail();
        Integer updatedAge = request.getAge();
        if (updatedName != null && !updatedName.equals(customer.getName())) {
            customer.setName(updatedName);
            update = true;
        }
        if (updatedEmail != null && !updatedEmail.equals(customer.getEmail())) {
            if(customerDao.customerWithEmailExists(updatedEmail)){
                throw new DuplicateResourceException("Email already taken");
            }
            customer.setEmail(updatedEmail);
            update = true;
        }
        if (updatedAge != null && !updatedAge.equals(customer.getAge())) {
            customer.setAge(updatedAge);
            update = true;
        }
        if (!update) {
            throw new RequestValidationException("no data changes found");
        }
        return customerDao.updateCustomer(customer);
    }

    public void deleteCustomer(Integer id) {
        if (!customerDao.customerWithIdExists(id)) {
            throw new ResourceNotFoundException("customer with id: %s is not found".formatted(id));
        }
        customerDao.deleteCustomer(id);
    }
}
