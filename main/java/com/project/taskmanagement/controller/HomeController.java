package com.project.taskmanagement.controller;

import com.project.taskmanagement.model.AuthenticationRequest;
import com.project.taskmanagement.model.AuthenticationResponse;
import com.project.taskmanagement.model.Company;
import com.project.taskmanagement.model.Employee;
import com.project.taskmanagement.service.CompanyService;
import com.project.taskmanagement.service.EmployeeService;
import com.project.taskmanagement.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController()
public class HomeController {
    private final AuthenticationManager authenticationManager;
    private final CompanyService companyService;
    private final EmployeeService employeeService;
    private final JwtUtil jwtTokenUtil;

    @Autowired
    public HomeController(AuthenticationManager authenticationManager, EmployeeService employeeService,
                          CompanyService companyService, JwtUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.employeeService = employeeService;
        this.companyService = companyService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("/company/info")
    public List<Employee> company() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Company company = (Company) companyService.loadUserByUsername(username);
        return employeeService.findByCompanyId(company.getCompanyId());
    }

    @GetMapping(value = "/employee")
    public String user() {
        return "The page is available for employees and companies";
    }

    @PostMapping("/company/register")
    public String register(@RequestBody Company company) {
        companyService.register(company);
        return "Successful registration!";
    }

    @PostMapping("/employee/register")
    public String register(@RequestBody Employee employee) {
        employeeService.register(employee);
        return "Successful registration!";
    }

    @Modifying
    @Transactional
    @PutMapping("/company/assign/{employeeId}")
    public String assignTask(@ModelAttribute(value = "taskExplanation", name = "taskExplanation") String taskExplanation, @PathVariable Long employeeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<Company> company = companyService.findByUsername(username);
        Optional<Employee> employeeById = employeeService.findById(employeeId);
        if (!company.isEmpty() && employeeById.get().getCompany().getName().equalsIgnoreCase(company.get(0).getName())) {
            employeeById.orElseThrow(() -> new NullPointerException("Username with this id is not found")).setTask(taskExplanation);
        } else {
            return "Error!";
        }
        return "The task was assigned!";
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
}