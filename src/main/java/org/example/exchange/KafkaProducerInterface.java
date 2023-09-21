package org.example.exchange;

import org.example.domain.dto.EmployeeMVDTO;

public interface KafkaProducerInterface {
    void sendEmployee(EmployeeMVDTO employeeVDTO);
}
