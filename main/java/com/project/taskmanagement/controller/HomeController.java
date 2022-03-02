package com.project.taskmanagement.controller;


import com.project.taskmanagement.model.*;
import com.project.taskmanagement.service.CompanyService;
import com.project.taskmanagement.service.EmployeeService;
import com.project.taskmanagement.service.TaskService;
import com.project.taskmanagement.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Component
public class HomeController {
    private final CompanyService companyService;
    private final EmployeeService employeeService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtTokenUtil;
    private final TaskService taskService;

    @Autowired
    public HomeController(EmployeeService employeeService, CompanyService companyService, TaskService taskService,
                          AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.employeeService = employeeService;
        this.companyService = companyService;
        this.taskService = taskService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtUtil;
    }

    @GetMapping("/company/info")
    public List<Employee> company() {
        return companyService.printInfo();
    }

    @GetMapping(value = "/employee")
    public String user() {
        return "The page is available for employees and companies";
    }

    @PostMapping("/company/register")
    public String register(@RequestBody Company company) {
        return companyService.register(company);
    }

    @PostMapping("/employee/register")
    public String register(@RequestBody Employee employee) {
        return employeeService.register(employee);
    }

    @PostMapping("/company/authenticate")
    public ResponseEntity<?> createAuthenticationTokenForCompany(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
        final UserDetails userDetails = companyService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/employee/authenticate")
    public ResponseEntity<?> createAuthenticationTokenForEmployee(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
        final UserDetails userDetails = employeeService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/{companyName}/assign/{employeeId}")
    public String assignTask(@PathVariable String companyName, @PathVariable Long employeeId, @RequestBody Task task) {
        return taskService.assignTask(companyName, employeeId, task);
    }

    @PutMapping("/{companyName}/update/{taskId}")
    public String updateTask(@PathVariable String companyName, @PathVariable Long taskId, @RequestBody Task task) {
        return taskService.updateTask(companyName, taskId, task);
    }

    @Transactional
    @DeleteMapping("/{companyName}/delete/{taskId}")
    public String deleteTask(@PathVariable String companyName, @PathVariable Long taskId) {
        return taskService.deleteTask(companyName, taskId);
    }

    @Modifying
    @Transactional
    @PutMapping("/{companyName}/reassign/{taskId}/{employeeId}")
    public String reassignTask(@PathVariable String companyName, @PathVariable Long taskId, @PathVariable Long employeeId) {
        return taskService.assignToOtherEmployee(companyName, taskId, employeeId);
    }
}