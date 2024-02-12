package com.kotenko.fullstack;

import com.github.javafaker.Faker;
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
        Faker faker = new Faker();
        List<Customer> customers = List.of(
                new Customer(
                        faker.name().firstName(),
                        faker.internet().emailAddress(),
                        faker.number().numberBetween(15, 75)),
                new Customer(
                        faker.name().firstName(),
                        faker.internet().emailAddress(),
                        faker.number().numberBetween(15, 75)));
        return args -> customerRepository.saveAll(customers);
    }
}
