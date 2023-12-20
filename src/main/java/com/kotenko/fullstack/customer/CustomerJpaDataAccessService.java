package com.kotenko.fullstack.customer;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class CustomerJpaDataAccessService implements CustomerDao{

    private final CustomerRepository customerRepository;

    public CustomerJpaDataAccessService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> fetchAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> fetchCustomerById(Integer id) {
        return customerRepository.findById(id);
    }

    @Override
    public Customer persistCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(Integer id) {
        customerRepository.deleteById(id);
    }

    @Override
    public boolean customerWithEmailExists(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public boolean customerWithIdExists(Integer id) {
        return customerRepository.existsById(id);
    }
}
