package org.example.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(
        name = "user",
        schema = "employees"
)
public class User extends Person {

    @Column(name = "password", length = 100)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER, targetClass = UserRole.class)
    @CollectionTable(
            schema = "employees",
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    List<UserRole> userRoles;
}
