package org.example.exchange;

import lombok.RequiredArgsConstructor;
import org.example.domain.dto.EmployeeVDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty("spring.kafka.enabled")
public class KafkaProducer implements KafkaProducerInterface{
    @Value("${spring.kafka.topic.on-employee-change}")
    private String onEmployeeChange;

    private final KafkaTemplate<UUID, EmployeeVDTO> employeesKafkaTemplate;

    public void sendEmployee(EmployeeVDTO employeeVDTO) {
        employeesKafkaTemplate.send(onEmployeeChange, employeeVDTO);
    }
}
