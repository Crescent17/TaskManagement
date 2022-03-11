package com.project.taskmanagement.controller;


import com.project.taskmanagement.model.*;
import com.project.taskmanagement.service.CompanyService;
import com.project.taskmanagement.service.EmployeeService;
import com.project.taskmanagement.service.TaskService;
import com.project.taskmanagement.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;

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
    public ResponseEntity<?> companyInfo() {

        return new ResponseEntity<>(companyService.printInfo(), HttpStatus.OK);
    }

    @PostMapping("/company/register")
    public ResponseEntity<?> register(@RequestBody Company company) {
        try {
            companyService.register(company);
        } catch (EntityExistsException ex) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/employee/register")
    public ResponseEntity<?> register(@RequestBody Employee employee) {
        try {
            employeeService.register(employee);
        } catch (IllegalStateException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (EntityExistsException ex) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
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
    public ResponseEntity<?> assignTask(@PathVariable String companyName, @PathVariable Long employeeId, @RequestBody Task task) {
        try {
            taskService.assignTask(companyName, employeeId, task);
        } catch (NullPointerException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RuntimeException ex) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/{companyName}/update/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable String companyName, @PathVariable Long taskId, @RequestBody Task task) {
        try {
            taskService.updateTask(companyName, taskId, task);
        } catch (AccessDeniedException ex) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (NullPointerException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/{companyName}/delete/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable String companyName, @PathVariable Long taskId) {
        try {
            taskService.deleteTask(companyName, taskId);
        } catch (AccessDeniedException ex) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (NullPointerException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Modifying
    @Transactional
    @PutMapping("/{companyName}/reassign/{taskId}/{employeeId}")
    public ResponseEntity<?> reassignTask(@PathVariable String companyName, @PathVariable Long taskId, @PathVariable Long employeeId) {
        try {
            taskService.assignToOtherEmployee(companyName, taskId, employeeId);
        } catch (IllegalStateException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (NullPointerException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (AccessDeniedException ex) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}