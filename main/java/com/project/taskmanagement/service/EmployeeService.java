package com.project.taskmanagement.service;

import com.project.taskmanagement.exception.*;
import com.project.taskmanagement.model.Company;
import com.project.taskmanagement.model.Employee;
import com.project.taskmanagement.model.Task;
import com.project.taskmanagement.repository.CompanyRepository;
import com.project.taskmanagement.repository.EmployeeRepository;
import com.project.taskmanagement.util.MyPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
            throw new WrongCompanyName();
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
            throw new WrongEmployeeId();
        }
    }

    public String changeEmployeeName(String previousName, String newName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = employeeRepository.findByUsername(authentication.getName()).get(0);
        if (employee.getName().equalsIgnoreCase(previousName)) {
            employee.setName(newName);
            return "Changed!";
        }
        throw new WrongEmployeeName();
    }

    public String changeEmployeeLastName(String lastName, String newLastName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = employeeRepository.findByUsername(authentication.getName()).get(0);
        if (employee.getLastName().equalsIgnoreCase(lastName)) {
            employee.setLastName(newLastName);
            return "Changed!";
        }
        throw new WrongEmployeeLastName();
    }

    public String changeEmployeeUsername(String username, String newUsername) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = employeeRepository.findByUsername(authentication.getName()).get(0);
        if (employee.getUsername().equalsIgnoreCase(username)) {
            if (employeeRepository.findByUsername(newUsername).isEmpty()) {
                employee.setUsername(newUsername);
                return "Changed!";
            }
            throw new EntityExistsException();
        }
        throw new WrongUsername();
    }

    public String changeEmployeePassword(String password, String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Employee> employeeOptional = employeeRepository.findByUsername(authentication.getName());
        if (!employeeOptional.isEmpty()) {
            Employee employee = employeeOptional.get(0);
            if (myPasswordEncoder.getPasswordEncoder().matches(password, employee.getPassword())) {
                employee.setPassword(myPasswordEncoder.getPasswordEncoder().encode(newPassword));
                return "Changed!";
            }
            throw new PasswordsDontMatch();
        }
        throw new AuthorizationException();
    }

    public String changeCompany(String companyName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Employee employee = employeeRepository.findByUsername(authentication.getName()).get(0);
        if (!companyRepository.findByNameIgnoreCase(companyName).isEmpty()) {
            employee.setCompany(companyRepository.findByNameIgnoreCase(companyName).get(0));
            employee.setCompanyName(companyRepository.findByNameIgnoreCase(companyName).get(0).getName());
            return "Changed!";
        }
        throw new WrongCompanyName();
    }
}