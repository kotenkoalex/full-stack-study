package com.kotenko.fullstack.customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    List<Customer> fetchAllCustomers();

    Optional<Customer> fetchCustomerById(Integer id);

    Customer persistCustomer(Customer customer);

    Customer updateCustomer(Customer customer);

    void deleteCustomer(Integer id);

    boolean customerWithEmailExists(String email);

    boolean customerWithIdExists(Integer id);
}
