package com.project.taskmanagement;

import com.project.taskmanagement.model.Company;
import com.project.taskmanagement.model.Employee;
import com.project.taskmanagement.model.Task;
import com.project.taskmanagement.service.CompanyService;
import com.project.taskmanagement.service.EmployeeService;
import com.project.taskmanagement.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration
public class Initialization {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Test
    void initialize() {
        companyService.register(new Company("Adidas", "adidas@gmail.com", "123456"));
        companyService.register(new Company("Puma", "puma@gmail.com", "123456"));
        employeeService.register(new Employee("Alexey", "Berezin", "berezin141@gmail.com", "123456", "Adidas"));
        employeeService.register(new Employee("Vladimir", "Minenko", "minenko21@gmail.com", "123456", "Adidas"));
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        taskService.assignTask("Adidas", 2L, new Task("ID1"));
        taskService.assignTask("Adidas", 3L, new Task("ID2"));
    }
}
