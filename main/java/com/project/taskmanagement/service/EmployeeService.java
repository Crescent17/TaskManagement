package com.project.taskmanagement.service;

import com.project.taskmanagement.model.Company;
import com.project.taskmanagement.model.Employee;
import com.project.taskmanagement.model.Task;
import com.project.taskmanagement.repository.CompanyRepository;
import com.project.taskmanagement.repository.EmployeeRepository;
import com.project.taskmanagement.util.MyPasswordEncoder;
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
    private final MyPasswordEncoder myPasswordEncoder;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, CompanyRepository companyRepository, MyPasswordEncoder myPasswordEncoder) {
        this.employeeRepository = employeeRepository;
        this.companyRepository = companyRepository;
        this.myPasswordEncoder = myPasswordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Employee> employees = employeeRepository.findByUsername(username);
        if (employees.isEmpty()) {
            throw new UsernameNotFoundException("User " + username + " doesn't exist!");
        }
        return employees.get(0);
    }

    public String register(Employee employee) {
        if (companyRepository.findByNameIgnoreCase(employee.getCompanyName()).isEmpty()) {
            throw new IllegalStateException("There is no such company registered!");
        } else if (!employeeRepository.findByUsername(employee.getUsername()).isEmpty()) {
            throw new EntityExistsException("Employee with this login already exists!");
        }
        Company company = companyRepository.findByNameIgnoreCase(employee.getCompanyName()).get(0);
        employee.setCompany(company);
        employee.setPassword(myPasswordEncoder.getPasswordEncoder().encode(employee.getPassword()));
        employeeRepository.save(employee);
        return "Successful registration!";
    }

    public String assignTask(Long employeeId, Task task) {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            employee.getTask().add(task);
            return "Assigned";
        } else {
            throw new IllegalStateException("Wrong id");
        }
    }
}