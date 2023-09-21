package org.example.exchange;

import lombok.RequiredArgsConstructor;
import org.example.domain.dto.EmployeeVDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class KafkaProducer {
    @Value("${spring.kafka.topic.on-employee-change}")
    private String onEmployeeChange;

    private final KafkaTemplate<UUID, EmployeeVDTO> employeesKafkaTemplate;

    public void sendEmployee(EmployeeVDTO employeeVDTO) {
        employeesKafkaTemplate.send(onEmployeeChange, employeeVDTO);
    }
}
