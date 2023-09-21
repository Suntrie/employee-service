package org.example.domain.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "employee", schema = "employees")
@Data
@SuperBuilder
@NoArgsConstructor
public class Employee extends Person {

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
}
