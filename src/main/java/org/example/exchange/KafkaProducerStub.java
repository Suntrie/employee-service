package org.example.exchange;

import org.example.domain.dto.EmployeeMVDTO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(value = "spring.kafka.enabled",havingValue = "false")
public class KafkaProducerStub implements KafkaProducerInterface{
    @Override
    public void sendEmployee(EmployeeMVDTO employeeMVDTO) {
        // Stub method
    }
}
