package com.project.taskmanagement.repository;

import com.project.taskmanagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByUsername(String username);

    @Query("select e from Employee e where e.company.companyId = ?1")
    List<Employee> findByCompanyId(Long companyId);
}
