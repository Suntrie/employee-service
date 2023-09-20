package org.example.domain.dto;

import lombok.Data;
import org.example.domain.model.UserRole;

import java.util.List;

@Data
public class UserVDTO {
    private String firstName;
    private String lastName;
    private String email;
    private List<UserRole> userRoles;
}
