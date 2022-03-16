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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


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
        return new ResponseEntity<>(companyService.register(company), HttpStatus.OK);
    }

    @PostMapping("/employee/register")
    public ResponseEntity<?> register(@RequestBody Employee employee) {
        return new ResponseEntity<>(employeeService.register(employee), HttpStatus.OK);
    }

    @PostMapping("/company/authenticate")
    public ResponseEntity<?> createAuthenticationTokenForCompany(@RequestBody AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                authenticationRequest.getPassword()));
        final UserDetails userDetails = companyService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/employee/authenticate")
    public ResponseEntity<?> createAuthenticationTokenForEmployee(@RequestBody AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                authenticationRequest.getPassword()));
        final UserDetails userDetails = employeeService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }

    @PostMapping("/{companyName}/assign/{employeeId}")
    public ResponseEntity<?> assignTask(@PathVariable String companyName, @PathVariable Long employeeId, @RequestBody Task task) {
        return new ResponseEntity<>(taskService.assignTask(companyName, employeeId, task), HttpStatus.OK);
    }

    @PutMapping("/{companyName}/update/{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable String companyName, @PathVariable Long taskId, @RequestBody Task task) {
        return new ResponseEntity<>(taskService.updateTask(companyName, taskId, task), HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/{companyName}/delete/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable String companyName, @PathVariable Long taskId) {
        return new ResponseEntity<>(taskService.deleteTask(companyName, taskId), HttpStatus.OK);
    }

    @Modifying
    @Transactional
    @PutMapping("/{companyName}/reassign/{taskId}/{employeeId}")
    public ResponseEntity<?> reassignTask(@PathVariable String companyName, @PathVariable Long taskId, @PathVariable Long employeeId) {
        return new ResponseEntity<>(taskService.assignToOtherEmployee(companyName, taskId, employeeId), HttpStatus.OK);
    }

    @Modifying
    @Transactional
    @PutMapping("/company/changeName/{previousName}/{newName}")
    public ResponseEntity<?> changeCompanyName(@PathVariable String previousName, @PathVariable String newName) {
        return new ResponseEntity<>(companyService.changeCompanyName(previousName, newName), HttpStatus.OK);
    }

    @Modifying
    @Transactional
    @PutMapping("/company/changeUsername/{previousUsername}/{newUsername}")
    public ResponseEntity<?> changeCompanyUsername(@PathVariable String previousUsername, @PathVariable String newUsername) {
        return new ResponseEntity<>(companyService.changeCompanyUsername(previousUsername, newUsername), HttpStatus.OK);
    }

    @Modifying
    @Transactional
    @PutMapping("/{companyName}/changePassword/{previousPassword}/{newPassword}")
    public ResponseEntity<?> changeCompanyPassword(@PathVariable String companyName, @PathVariable String previousPassword,
                                                   @PathVariable String newPassword) {
        return new ResponseEntity<>(companyService.changeCompanyPassword(companyName, previousPassword, newPassword), HttpStatus.OK);
    }

    @Modifying
    @Transactional
    @PutMapping("/employee/changeName/{previousName}/{newName}")
    public ResponseEntity<?> changeEmployeeName(@PathVariable String previousName, @PathVariable String newName) {
        return new ResponseEntity<>(employeeService.changeEmployeeName(previousName, newName), HttpStatus.OK);
    }

    @Modifying
    @Transactional
    @PutMapping("/employee/changeLastName/{previousLastName}/{newLastName}")
    public ResponseEntity<?> changeEmployeeLastName(@PathVariable String previousLastName, @PathVariable String newLastName) {
        return new ResponseEntity<>(employeeService.changeEmployeeLastName(previousLastName, newLastName), HttpStatus.OK);
    }

    @Modifying
    @Transactional
    @PutMapping("/employee/changeUsername/{previousUsername}/{newUsername}")
    public ResponseEntity<?> changeEmployeeUsername(@PathVariable String previousUsername, @PathVariable String newUsername) {
        return new ResponseEntity<>(employeeService.changeEmployeeUsername(previousUsername, newUsername), HttpStatus.OK);
    }

    @Modifying
    @Transactional
    @PutMapping("/employee/changePassword/{previousPassword}/{newPassword}")
    public ResponseEntity<?> changeEmployeePassword(@PathVariable String previousPassword, @PathVariable String newPassword) {
        return new ResponseEntity<>(employeeService.changeEmployeePassword(previousPassword, newPassword), HttpStatus.OK);
    }

    @Modifying
    @DeleteMapping("/{companyName}/deleteEmployee/{employeeUsername}")
    public ResponseEntity<?> deleteEmployee(@PathVariable String companyName, @PathVariable String employeeUsername) {
        return new ResponseEntity<>(companyService.deleteEmployee(companyName, employeeUsername), HttpStatus.OK);
    }

    @Modifying
    @Transactional
    @PutMapping("/employee/changeCompany/{companyName}")
    public ResponseEntity<?> changeCompany(@PathVariable String companyName) {
        return new ResponseEntity<>(employeeService.changeCompany(companyName), HttpStatus.OK);
    }
}