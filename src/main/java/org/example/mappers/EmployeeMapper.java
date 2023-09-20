package org.example.mappers;

import org.example.domain.dto.EmployeeCDTO;
import org.example.domain.dto.EmployeeLDTO;
import org.example.domain.dto.EmployeeUDTO;
import org.example.domain.dto.EmployeeVDTO;
import org.example.domain.model.Employee;
import org.mapstruct.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {

    @Mapping(target = "birthday", expression = "java(formatDate(employeeCDTO.getBirthday()))")
    Employee toEntity(EmployeeCDTO employeeCDTO);

    EmployeeVDTO toVDTO(Employee employee);

    Employee updateEntity(@MappingTarget Employee employee, EmployeeUDTO employeeUDTO);

    EmployeeLDTO toLDTO(Employee employee);

    default Date formatDate(Date date) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return formatter.parse(formatter.format(date));
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unexpected date format");
        }
    }
}
