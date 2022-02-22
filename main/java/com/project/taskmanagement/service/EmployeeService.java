package com.project.taskmanagement.service;

import com.project.taskmanagement.model.AuthenticationRequest;
import com.project.taskmanagement.model.AuthenticationResponse;
import com.project.taskmanagement.model.Company;
import com.project.taskmanagement.model.Employee;
import com.project.taskmanagement.repository.CompanyRepository;
import com.project.taskmanagement.repository.EmployeeRepository;
import com.project.taskmanagement.util.JwtUtil;
import com.project.taskmanagement.util.MyPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtTokenUtil;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, CompanyRepository companyRepository,
                           AuthenticationManager authenticationManager, JwtUtil jwtTokenUtil) {
        this.employeeRepository = employeeRepository;
        this.companyRepository = companyRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Employee> employees = employeeRepository.findByUsername(username);
        if (employees.isEmpty()) {
            throw new UsernameNotFoundException("User " + username + " doesn't exist!");
        }
        return employees.get(0);
    }

    public List<Employee> findByCompanyId(Long id) {
        return employeeRepository.findByCompanyId(id);
    }

    public String register(Employee employee) {
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

    public Optional<Employee> findById(Long id) {
        return employeeRepository.findById(id);
    }

    public ResponseEntity<?> createAuthenticationTokenForEmployee(AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                    authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
        final UserDetails userDetails = loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }


}