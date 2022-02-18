package com.project.taskmanagement.repository;

import com.project.taskmanagement.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    List<Company> findByUsername(String username);
    List<Company> findByNameIgnoreCase(String name);
}
