package org.example.exchange;

import org.example.domain.dto.EmployeeVDTO;

public interface KafkaProducerInterface {
    void sendEmployee(EmployeeVDTO employeeVDTO);
}
