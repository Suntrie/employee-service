package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.dto.*;
import org.example.domain.model.Employee;
import org.example.exchange.KafkaProducerInterface;
import org.example.mappers.EmployeeMapper;
import org.example.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final KafkaProducerInterface kafkaProducer;

    @Transactional
    public EmployeeVDTO createEmployee(EmployeeCDTO employeeCDTO) {
        Optional<Employee> employeeOptional = employeeRepository.findByEmail(employeeCDTO.getEmail());

        if (employeeOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee already exists");
        }
        Employee employee = employeeRepository.save(employeeMapper.toEntity(employeeCDTO));
        sendModifiedEmployeeToKafka(employee, "create");
        return employeeMapper.toVDTO(employee);
    }

    private void sendModifiedEmployeeToKafka(Employee employee, String method) {
        EmployeeMVDTO employeeMVDTO = employeeMapper.toMVDTO(employee);
        employeeMVDTO.setMethod(method);
        kafkaProducer.sendEmployee(employeeMVDTO);
    }

    @Transactional
    public void deleteEmployee(UUID employeeId) {
        Employee employee = getByIdOrThrow(employeeId);
        EmployeeMVDTO employeeMVDTO = employeeMapper.toMVDTO(employee);
        employeeMVDTO.setMethod("delete");
        employeeRepository.delete(employee);
        kafkaProducer.sendEmployee(employeeMVDTO);
    }

    @Transactional
    public EmployeeVDTO updateEmployee(UUID employeeId, EmployeeUDTO employeeUDTO) {
        Employee employee = getByIdOrThrow(employeeId);
        Optional<Employee> employeeDuplicateOpt =
                employeeRepository.findByEmailAndIdNot(employeeUDTO.getEmail(), employeeId);

        if (employeeDuplicateOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee duplicate (email field must be unique)");
        }

        employee = employeeRepository.save(employeeMapper.updateEntity(employee, employeeUDTO));
        sendModifiedEmployeeToKafka(employee, "update");
        return employeeMapper.toVDTO(employee);
    }

    private Employee getByIdOrThrow(UUID employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee doesn't exist"));
    }

    @Transactional(readOnly = true)
    public EmployeeVDTO getEmployee(UUID employeeId) {
        Employee employee = getByIdOrThrow(employeeId);
        return employeeMapper.toVDTO(employee);
    }

    @Transactional(readOnly = true)
    public List<EmployeeLDTO> getEmployees() {
        return employeeRepository.findAllByOrderByLastNameAsc().stream().map(employeeMapper::toLDTO).collect(Collectors.toList());
    }
}
