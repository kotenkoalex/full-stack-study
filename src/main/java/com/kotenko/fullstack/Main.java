package com.kotenko.fullstack;

import com.kotenko.fullstack.customer.Customer;
import com.kotenko.fullstack.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {
        List<Customer> customers = List.of(
                new Customer(1, "Alex", "alex@gmail.com", 21),
                new Customer(2, "Jamila", "jamila@gmail.com", 18));
        return args -> customerRepository.saveAll(customers);
    }
}
