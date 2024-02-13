package com.kotenko.fullstack.customer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer", uniqueConstraints = {
        @UniqueConstraint(
                name = "customer_unique_email",
                columnNames = "email"
        )
})
public class Customer {
    @Id
    @SequenceGenerator(
            name = "customer_id_seq",
            sequenceName = "customer_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_id_seq"
    )
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false, unique = true)
    private Integer age;

    public Customer(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }
}
