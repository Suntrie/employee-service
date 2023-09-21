package org.example.unit;

import org.example.repository.EmployeeRepository;
import org.example.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@DisplayName("EmployeeService is working when")
@ExtendWith(MockitoExtension.class)
class ServiceTest {

    @InjectMocks
    EmployeeService service;

    @Mock
    EmployeeRepository employeeRepository;

    @Test
    void when_delete_non_existing_user_expect_500() {
        UUID uuid = UUID.randomUUID();
        when(employeeRepository.findById(uuid)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> {
            service.deleteEmployee(uuid);
        });
    }
}