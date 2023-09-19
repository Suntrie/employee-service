package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.dto.EmployeeCDTO;
import org.example.domain.dto.EmployeeLDTO;
import org.example.domain.dto.EmployeeUDTO;
import org.example.domain.dto.EmployeeVDTO;
import org.example.domain.model.Employee;
import org.example.mappers.EmployeeMapper;
import org.example.repository.EmployeeRepository;
import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.domain.Page;
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

    @Transactional
    public EmployeeVDTO createEmployee(EmployeeCDTO employeeCDTO){
        Optional<Employee> employeeOptional = employeeRepository.findByEmail(employeeCDTO.getEmail());

        if (employeeOptional.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee already exists");
        }
        Employee employee = employeeMapper.toEntity(employeeCDTO);
        return employeeMapper.toVDTO(employeeRepository.save(employee));
    }

    @Transactional
    public void deleteEmployee(UUID employeeId){
        Employee employee = getByIdOrThrow(employeeId);
        employeeRepository.delete(employee);
    }

    //TODO: osiv?

    @Transactional
    public EmployeeVDTO updateEmployee(UUID employeeId, EmployeeUDTO employeeUDTO) throws InterruptedException {
        Employee employee = getByIdOrThrow(employeeId);
        Optional<Employee> employeeDuplicateOpt =
                employeeRepository.findByEmailAndIdNot(employeeUDTO.getEmail(), employeeId);

        if (employeeDuplicateOpt.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee duplicate (email field must be unique)");
        }

        employee = employeeMapper.updateEntity(employee, employeeUDTO);

        Thread.sleep(5000);

        return employeeMapper.toVDTO(employeeRepository.save(employee));
    }

    private Employee getByIdOrThrow(UUID employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee doesn't exist"));
    }

    public EmployeeVDTO getEmployee(UUID employeeId) {
        Employee employee = getByIdOrThrow(employeeId);
        return employeeMapper.toVDTO(employee);
    }

    public List<EmployeeLDTO> getEmployees() {
        return employeeRepository.findAllByOrderByLastNameAsc().stream().map(employeeMapper::toLDTO).collect(Collectors.toList());
    }
}