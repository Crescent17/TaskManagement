package com.project.taskmanagement.service;

import com.project.taskmanagement.model.Company;
import com.project.taskmanagement.model.CompanyPrincipal;
import com.project.taskmanagement.repository.CompanyRepository;
import com.project.taskmanagement.util.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyDetailsService implements UserDetailsService {
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CompanyDetailsService(CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Company> companies = companyRepository.findByUsername(username);
        if (companies.isEmpty()) {
            throw new UsernameNotFoundException(username + " not found");
        }
        return new CompanyPrincipal(companies.get(0), passwordEncoder);
    }
}
