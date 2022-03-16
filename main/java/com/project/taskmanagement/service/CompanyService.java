package com.project.taskmanagement.service;

import com.project.taskmanagement.exception.*;
import com.project.taskmanagement.model.Company;
import com.project.taskmanagement.model.Employee;
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

@Service
public class CompanyService implements UserDetailsService {
    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;
    private final MyPasswordEncoder myPasswordEncoder;


    @Autowired
    public CompanyService(CompanyRepository companyRepository, EmployeeRepository employeeRepository,
                          TaskRepository taskRepository, MyPasswordEncoder myPasswordEncoder,
                          EmployeeService employeeService) {
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
        this.myPasswordEncoder = myPasswordEncoder;
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
            throw new WrongCompanyName();
        }
    }

    public List<Employee> printInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Company company = companyRepository.findByUsername(username).get(0);
        return employeeRepository.findByCompanyId(company.getCompanyId());
    }

    public String changeCompanyName(String previousName, String newName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Company company = companyRepository.findByUsername(authentication.getName()).get(0);
        if (company.getName().equalsIgnoreCase(previousName)) {
            company.setName(newName);
            return "Changed!";
        }
        throw new WrongCompanyName();
    }

    public String changeCompanyUsername(String previousUsername, String newUsername) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Company company = companyRepository.findByUsername(authentication.getName()).get(0);
        if (company.getUsername().equalsIgnoreCase(previousUsername)) {
            if (companyRepository.findByUsername(newUsername).isEmpty()) {
                company.setUsername(newUsername);
                return "Changed!";
            }
            throw new EntityExistsException();
        }
        throw new NoPermission();
    }

    public String changeCompanyPassword(String companyName, String previousPassword, String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Company> companyOptional = companyRepository.findByUsername(authentication.getName());
        if (!companyOptional.isEmpty()) {
            Company company = companyOptional.get(0);
            if (company.getName().equalsIgnoreCase(companyName)) {
                if (myPasswordEncoder.getPasswordEncoder().matches(previousPassword, company.getPassword())) {
                    company.setPassword(myPasswordEncoder.getPasswordEncoder().encode(newPassword));
                    return "Changed!";
                }
                throw new PasswordsDontMatch();
            }
            throw new NoPermission();
        }
        throw new AuthorizationException();
    }

    public String deleteEmployee(String companyName, String employeeUsername) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Company> companyOptional = companyRepository.findByUsername(authentication.getName());
        if (!companyOptional.isEmpty()) {
            Company company = companyOptional.get(0);
            if (company.getName().equalsIgnoreCase(companyName)) {
                List<Employee> employeeOptional = employeeRepository.findByUsername(employeeUsername);
                if (!employeeOptional.isEmpty()) {
                    Employee employee = employeeOptional.get(0);
                    if (employee.getCompanyName().equalsIgnoreCase(companyName)) {
                        employeeRepository.delete(employee);
                        return "Deleted!";
                    }
                    throw new NoPermission();
                }
                throw new WrongUsername();
            }
            throw new NoPermission();
        }
        throw new AuthorizationException();
    }
}
