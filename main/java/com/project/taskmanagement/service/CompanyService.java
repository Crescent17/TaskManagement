package com.project.taskmanagement.service;

import com.project.taskmanagement.model.Company;
import com.project.taskmanagement.model.Employee;
import com.project.taskmanagement.repository.CompanyRepository;
import com.project.taskmanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
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
public class CompanyService implements UserDetailsService {
    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;


    @Autowired
    public CompanyService(CompanyRepository companyRepository, EmployeeRepository employeeRepository) {
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
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
        companyRepository.save(company);
        return "Successful registration!";
    }

    public List<Company> findByUsername(String username) {
        return companyRepository.findByUsername(username);
    }

    public String assignTask(Long employeeId, String taskExplanation) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<Company> company = companyRepository.findByUsername(username);
        Optional<Employee> employeeById = employeeRepository.findById(employeeId);
        if (!company.isEmpty() && employeeById.get().getCompany().getName().equalsIgnoreCase(company.get(0).getName())) {
            employeeById.orElseThrow(() -> new NullPointerException("Username with this id is not found")).setTask(taskExplanation);
        } else {
            return "Error!";
        }
        return "The task was assigned!";
    }

    public List<Employee> printInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Company company = (Company) companyRepository.findByUsername(username);
        return employeeRepository.findByCompanyId(company.getCompanyId());
    }
}
