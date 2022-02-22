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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService implements UserDetailsService {
    private final CompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtTokenUtil;

    @Autowired
    public CompanyService(CompanyRepository companyRepository, EmployeeRepository employeeRepository,
                          AuthenticationManager authenticationManager, JwtUtil jwtTokenUtil) {
        this.companyRepository = companyRepository;
        this.employeeRepository = employeeRepository;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
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

    public ResponseEntity<?> createAuthenticationTokenForCompany(AuthenticationRequest authenticationRequest) throws Exception {
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
