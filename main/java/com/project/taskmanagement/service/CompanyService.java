package com.project.taskmanagement.service;

import com.project.taskmanagement.model.Company;
import com.project.taskmanagement.model.Employee;
import com.project.taskmanagement.model.Task;
import com.project.taskmanagement.repository.CompanyRepository;
import com.project.taskmanagement.repository.EmployeeRepository;
import com.project.taskmanagement.repository.TaskRepository;
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
import java.util.Objects;
import java.util.Optional;

@Service
public class CompanyService implements UserDetailsService {
    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;
    private final MyPasswordEncoder myPasswordEncoder;
    private final TaskRepository taskRepository;
    private final EmployeeService employeeService;


    @Autowired
    public CompanyService(CompanyRepository companyRepository, EmployeeRepository employeeRepository,
                          TaskRepository taskRepository, MyPasswordEncoder myPasswordEncoder,
                          EmployeeService employeeService) {
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
        this.taskRepository = taskRepository;
        this.myPasswordEncoder = myPasswordEncoder;
        this.employeeService = employeeService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Company> companies = companyRepository.findByUsername(username);
        if (companies.isEmpty()) {
            throw new UsernameNotFoundException("User " + username + " doesn't exist!");
        }
        return companies.get(0);
    }

    public String register(Company company) {
        if (!companyRepository.findByUsername(company.getUsername()).isEmpty()) {
            throw new EntityExistsException("Company with this login already exists!");
        }
        company.setPassword(myPasswordEncoder.getPasswordEncoder().encode(company.getPassword()));
        companyRepository.save(company);
        return "Successful registration!";
    }

    public List<Company> findByUsername(String username) {
        return companyRepository.findByUsername(username);
    }

    public List<Company> findByName(String name) {
        List<Company> company = companyRepository.findByNameIgnoreCase(name);
        if (!company.isEmpty()) {
            return company;
        } else {
            throw new IllegalStateException("Wrong company name");
        }
    }

    public List<Employee> printInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Company company = companyRepository.findByUsername(username).get(0);
        return employeeRepository.findByCompanyId(company.getCompanyId());
    }
}
