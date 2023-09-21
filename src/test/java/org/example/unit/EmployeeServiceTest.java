package org.example.unit;

import org.example.domain.dto.EmployeeUDTO;
import org.example.domain.model.Employee;
import org.example.repository.EmployeeRepository;
import org.example.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@DisplayName("EmployeeService is working when")
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @InjectMocks
    EmployeeService employeeService;

    @Mock
    EmployeeRepository employeeRepository;

    @Test
    void when_delete_non_existing_user_expect_500() {
        UUID uuid = UUID.randomUUID();
        when(employeeRepository.findById(uuid)).thenReturn(Optional.empty());
        Throwable throwable = assertThrows(ResponseStatusException.class, () ->
            employeeService.deleteEmployee(uuid)
        );
        assertTrue(((ResponseStatusException)throwable).getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND));
    }

    @Test
    void when_update_existing_user_duplicate_email_expect_400(){
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();

        String email1="a@a";
        String email2="b@b";

        Employee baseEmployee1 = Employee.builder()
                .id(uuid1)
                .email(email1).firstName("A").build();
        Employee baseEmployee2 = Employee.builder()
                .id(uuid2)
                .email(email2).firstName("B").build();
        EmployeeUDTO employeeUDTO = EmployeeUDTO.builder()
                .email(email1)
                .birthday(new Date())
                .firstName("f")
                .lastName("l")
                .hobbies(List.of())
                .build();

        when(employeeRepository.findByEmailAndIdNot(email1, uuid2)).thenReturn(Optional.of(baseEmployee1));
        when(employeeRepository.findById(uuid2)).thenReturn(Optional.of(baseEmployee2));

        Throwable throwable = assertThrows(ResponseStatusException.class, () ->
            employeeService.updateEmployee(uuid2,employeeUDTO)
        );

        assertTrue(((ResponseStatusException)throwable).getStatusCode().isSameCodeAs(HttpStatus.BAD_REQUEST));
    }
}