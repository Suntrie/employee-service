package org.example.repository;

import org.example.domain.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
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
