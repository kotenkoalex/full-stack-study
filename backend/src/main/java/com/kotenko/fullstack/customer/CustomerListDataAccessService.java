package com.kotenko.fullstack.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao {
    private static final List<Customer> customers;

    static {
        customers = new ArrayList<>();
        Customer alex = new Customer(
                1,
                "Alex",
                "alex@gmail.com",
                21
        );
        Customer jamila = new Customer(
                2,
                "Jamila",
                "jamila@gmail.com",
                18
        );
        customers.add(alex);
        customers.add(jamila);
    }

    @Override
    public List<Customer> fetchAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> fetchCustomerById(Integer id) {
        return customers.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }

    @Override
    public Customer persistCustomer(Customer customer) {
        customers.add(customer);
        return customer;
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        deleteCustomer(customer.getId());
        customers.add(customer);
        return customer;
    }

    @Override
    public void deleteCustomer(Integer id) {
        customers.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst()
                .ifPresent(customers::remove);
    }

    @Override
    public boolean customerWithEmailExists(String email) {
        return customers.stream()
                .anyMatch(it -> it.getEmail().equals(email));
    }

    @Override
    public boolean customerWithIdExists(Integer id) {
        return customers.stream()
                .anyMatch(it -> it.getId().equals(id));
    }
}
