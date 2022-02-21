package com.project.taskmanagement.service;

import com.project.taskmanagement.model.Company;
import com.project.taskmanagement.model.Employee;
import com.project.taskmanagement.repository.CompanyRepository;
import com.project.taskmanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, CompanyRepository companyRepository) {
        this.employeeRepository = employeeRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Employee> employees = employeeRepository.findByUsername(username);
        if (employees.isEmpty()) {
            throw new UsernameNotFoundException("User " + username + " doesn't exist!");
        }
        return employees.get(0);
    }

    public List<Employee> findByCompanyId(Long id) {
        return employeeRepository.findByCompanyId(id);
    }

    public void register(Employee employee) {
        if (companyRepository.findByNameIgnoreCase(employee.getCompanyName()).isEmpty()) {
            throw new IllegalStateException("There is no such company registered!");
        } else if (!employeeRepository.findByUsername(employee.getUsername()).isEmpty()) {
            throw new EntityExistsException("Employee with this login already exists!");
        }
        Company company = companyRepository.findByNameIgnoreCase(employee.getCompanyName()).get(0);
        employee.setCompany(company);
        employeeRepository.save(employee);
    }

    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }


}