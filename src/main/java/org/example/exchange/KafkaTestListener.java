package org.example.exchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.dto.EmployeeVDTO;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty("spring.kafka.enabled")
public class KafkaTestListener {
    @KafkaListener(topics = "${spring.kafka.topic.on-employee-change}")
    public void listenEmployeeUpdateEvents(@Payload EmployeeVDTO employeeVDTO) {
        log.info("New update from Employee: {}", employeeVDTO);
    }

}
