package com.kotenko.fullstack.customer;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CustomerJdbcDataAccessService implements CustomerDao {
    @Override
    public List<Customer> fetchAllCustomers() {
        return null;
    }

    @Override
    public Optional<Customer> fetchCustomerById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Customer persistCustomer(Customer customer) {
        return null;
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        return null;
    }

    @Override
    public void deleteCustomer(Integer id) {

    }

    @Override
    public boolean customerWithEmailExists(String email) {
        return false;
    }

    @Override
    public boolean customerWithIdExists(Integer id) {
        return false;
    }
}
