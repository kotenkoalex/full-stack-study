package com.kotenko.fullstack.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {
    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return service.getAllCustomers();
    }

    @GetMapping("{id}")
    public Customer getCustomer(@PathVariable Integer id) {
        return service.getCustomer(id);
    }

    @PostMapping
    public Customer persistCustomer(@RequestBody CustomerRequest request) {
        return service.persistCustomer(request);
    }

    @PutMapping("{id}")
    public Customer updateCustomer(@PathVariable Integer id, @RequestBody CustomerRequest request) {
        return service.updateCustomer(id, request);
    }

    @DeleteMapping("{id}")
    public void deleteCustomer(@PathVariable Integer id) {
        service.deleteCustomer(id);
    }
}
