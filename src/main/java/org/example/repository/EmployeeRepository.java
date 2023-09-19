package org.example.repository;

import jakarta.persistence.LockModeType;
import org.example.domain.model.Employee;
import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

     Optional<Employee> findByEmailAndIdNot(String email, UUID id);
     Optional<Employee> findByEmail(String email);
     List<Employee> findAllByOrderByLastNameAsc();
}
