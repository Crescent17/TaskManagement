package com.project.taskmanagement.service;


import com.project.taskmanagement.model.Company;
import com.project.taskmanagement.model.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@ContextConfiguration
@Transactional
class CompanyServiceTest {

    @Autowired
    private CompanyService companyService;

    @Test
    void loadUserByUsername() {
        UserDetails expected = new Company(3L, "Nike", "nike@gmail.com", "123456");
        UserDetails actual = companyService.loadUserByUsername("nike@gmail.com");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void loadUserByUsernameFail() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            companyService.loadUserByUsername("audi@gmail.com");
        });
    }

    @Test
    void register() {
        String expected = "Successful registration!";
        String actual = companyService.register(new Company("Mercedess", "mercedess@gmail.com", "123456"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void registerExisting() {
        Assertions.assertThrows(EntityExistsException.class, () -> {
            companyService.register(new Company("Apple", "apple@icloud.com", "123456"));
        });
    }

    @Test
    void findByUsername() {
        List<Company> expected = Collections.singletonList(new Company(1L, "Apple", "apple@icloud.com", "123456"));
        List<Company> actual = companyService.findByUsername("apple@icloud.com");
        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    void findByNonExistingUsername() {
        List<Company> expected = Collections.emptyList();
        List<Company> actual = companyService.findByUsername("nonexist@gmail.com");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void findByName() {
        List<Company> expected = Collections.singletonList(new Company(1L, "Apple", "apple@icloud.com",
                "123456"));
        List<Company> actual = companyService.findByName("Apple");
        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    void findByNonExistingName() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            companyService.findByName("nonexist");
        });
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void printInfo() {
        List<Employee> expected = Collections.singletonList(new Employee(1L, "Alex", "Jackson",
                "alex@gmail.com", "$2a$12$PNkqw7cxNAzILGl3qTFFpOEmLPXJkJ9eeg0dS5O1Nu9qDpLHdoG3.",
                new Company(3L, "Nike", "nike@gmail.com",
                        "$2a$12$Jr4fixJKqrIuArmyieSZT.1UJBthIjUS.3XHCbWkpTbRpAIdEXkKu"), "Nike",
                Collections.emptyList()));
        List<Employee> actual = companyService.printInfo();
        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    void printInfoWithoutAuthentication() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            companyService.printInfo();
        });
    }

    @Test
    @WithMockUser(username = "weqt@gmail.com", password = "123456")
    void printInfoForNonExistingCompany() {
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> {
            companyService.printInfo();
        });
    }
}