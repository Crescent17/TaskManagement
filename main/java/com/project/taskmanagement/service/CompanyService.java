package com.project.taskmanagement.service;

import com.project.taskmanagement.model.Company;
import com.project.taskmanagement.repository.CompanyRepository;
import com.project.taskmanagement.util.MyPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.List;

@Service
public class CompanyService implements UserDetailsService {
    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<Company> companies = companyRepository.findByUsername(username);
        if (companies.isEmpty()) {
            throw new UsernameNotFoundException("User " + username + " doesn't exist!");
        }
        return companies.get(0);
    }

    public void register(Company company) {
        if (!companyRepository.findByUsername(company.getUsername()).isEmpty()) {
            throw new EntityExistsException("Company with this login already exists!");
        }
        companyRepository.save(company);
    }

    public List<Company> findByUsername(String username) {
        return companyRepository.findByUsername(username);
    }
}
