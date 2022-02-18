package com.project.taskmanagement.controller;

import com.project.taskmanagement.model.Company;
import com.project.taskmanagement.model.Employee;
import com.project.taskmanagement.repository.CompanyRepository;
import com.project.taskmanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;

@RestController("/hello")
public class HomeController {
    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public HomeController(CompanyRepository companyRepository, EmployeeRepository employeeRepository) {
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/company/info")
    public List<Employee> company() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Company company = companyRepository.findByUsername(username).get(0);
        return employeeRepository.findByCompanyId(company.getCompanyId());
    }

    @GetMapping(value = "/employee")
    public String user() {
        return "The page is available for employees and companies";
    }

    @PostMapping("/company/register")
    public String register(@RequestBody Company company) {
        if (!companyRepository.findByUsername(company.getUsername()).isEmpty()) {
            throw new EntityExistsException("Company with this login already exists!");
        }
        companyRepository.save(company);
        return "Successful registration!";
    }

    @PostMapping("/employee/register")
    public String register(@RequestBody Employee employee) {
        if (companyRepository.findByNameIgnoreCase(employee.getCompanyName()).isEmpty()) {
            throw new IllegalStateException("There is no such company registered!");
        } else if (!employeeRepository.findByUsername(employee.getUsername()).isEmpty()) {
            throw new EntityExistsException("Employee with this login already exists!");
        }
        Company company = companyRepository.findByNameIgnoreCase(employee.getCompanyName()).get(0);
        employee.setCompany(company);
        employeeRepository.save(employee);
        return "Successful registration!";
    }

    @Modifying
    @Transactional
    @PutMapping("/company/assign/{employeeId}")
    public String assignTask(@ModelAttribute(value = "taskExplanation", name = "taskExplanation") String taskExplanation, @PathVariable Long employeeId) {
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
}
