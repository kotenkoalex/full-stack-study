package com.kotenko.fullstack.customer;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(CustomerDao customers) {
        this.customerDao = customers;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Integer id) {
        return customerDao.selectCustomerById(id)
                .orElseThrow(() -> new IllegalArgumentException("customer with id: %s is not found".formatted(id)));
    }
}
