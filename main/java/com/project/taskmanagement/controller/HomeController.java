package com.project.taskmanagement.controller;

import com.project.taskmanagement.model.Company;
import com.project.taskmanagement.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/hello")
public class HomeController {
    private final CompanyRepository companyRepository;

    @Autowired
    public HomeController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @GetMapping("/admin")
    public String admin() {
        return "The page is available only for admins";
    }

    @GetMapping(value = "/user")
    public String user() {
        return "The page is available for users and admins";
    }

    @PostMapping("/register")
    public String register(@RequestBody Company company) {
        if (!companyRepository.findByUsername(company.getUsername()).isEmpty()) {
            return "This username is already taken!";
        }
        companyRepository.save(company);
        return "Successful registration!";
    }
}
