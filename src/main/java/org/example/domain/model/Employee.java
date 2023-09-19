package org.example.domain.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "employee", schema = "employees")
@Data
//TODO: validation
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "birthday")
    private Date birthday;

    @ElementCollection
    @CollectionTable(
            schema = "employees",
            name = "hobby",
            joinColumns = @JoinColumn(name = "employee_id")
    )
    @Column(name = "name")
    private List<String> hobbies;

    @Version
    private Long version;
}
