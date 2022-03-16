package com.project.taskmanagement.service;


import com.project.taskmanagement.exception.NoPermission;
import com.project.taskmanagement.exception.PasswordsDontMatch;
import com.project.taskmanagement.exception.WrongCompanyName;
import com.project.taskmanagement.exception.WrongUsername;
import com.project.taskmanagement.model.Company;
import com.project.taskmanagement.model.Employee;
import com.project.taskmanagement.model.Task;
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
        UserDetails expected = new Company(3L, "Nike", "nike@gmail.com",
                "$2a$12$Jr4fixJKqrIuArmyieSZT.1UJBthIjUS.3XHCbWkpTbRpAIdEXkKu");
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
        List<Company> expected = Collections.singletonList(new Company(1L, "Apple", "apple@icloud.com",
                "$2a$12$L/x4bR.qxkyT6g66bwOtau/hFV2LfuboAIitJ4r.7iNJP0R8bA/lO"));
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
                "$2a$12$L/x4bR.qxkyT6g66bwOtau/hFV2LfuboAIitJ4r.7iNJP0R8bA/lO"));
        List<Company> actual = companyService.findByName("Apple");
        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    void findByNonExistingName() {
        Assertions.assertThrows(WrongCompanyName.class, () -> {
            companyService.findByName("nonexist");
        });
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void printInfo() {
        List<Employee> expected = List.of(new Employee(1L, "Alex", "Jackson",
                        "alex@gmail.com", "$2a$12$PNkqw7cxNAzILGl3qTFFpOEmLPXJkJ9eeg0dS5O1Nu9qDpLHdoG3.",
                        new Company(3L, "Nike", "nike@gmail.com",
                                "$2a$12$Jr4fixJKqrIuArmyieSZT.1UJBthIjUS.3XHCbWkpTbRpAIdEXkKu"), "Nike",
                        Collections.singletonList(new Task("Task ID 1"))),
                new Employee(4L, "Jake", "Brown",
                        "brown@gmail.com", "$2a$12$.Wv5o5hocrOygonHqfjKSeVt2FZmiGo9WQD9YruNju.peemiORe/i",
                        new Company(3L, "Nike", "nike@gmail.com",
                                "$2a$12$Jr4fixJKqrIuArmyieSZT.1UJBthIjUS.3XHCbWkpTbRpAIdEXkKu"), "Nike",
                        Collections.singletonList(new Task("Task ID 4"))));
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

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void changeCompanyName() {
        String expected = "Changed!";
        String actual = companyService.changeCompanyName("nike", "nike10");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void changeWrongCompanyName() {
        Assertions.assertThrows(WrongCompanyName.class, () -> {
            companyService.changeCompanyName("qwer", "weqt");
        });
    }

    @Test
    void changeCompanyNameWithoutAuthentication() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            companyService.changeCompanyName("nike", "nike20");
        });
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void changeCompanyUsername() {
        String expected = "Changed!";
        String actual = companyService.changeCompanyUsername("nike@gmail.com", "nike20@gmail.com");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void chooseExistingUsername() {
        Assertions.assertThrows(EntityExistsException.class, () -> {
            companyService.changeCompanyUsername("nike@gmail.com", "apple@icloud.com");
        });
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void wrongPreviousUsername() {
        Assertions.assertThrows(NoPermission.class, () -> {
            companyService.changeCompanyUsername("qwer", "tqew");
        });
    }

    @Test
    void changeUsernameWithoutAuthentication() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            companyService.changeCompanyUsername("nike@gmail.com", "nike20@gmail.com");
        });
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void changeCompanyPassword() {
        String expected = "Changed!";
        String actual = companyService.changeCompanyPassword("Nike", "123456", "1234567");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void wrongPreviousPassword() {
        Assertions.assertThrows(PasswordsDontMatch.class, () -> {
            companyService.changeCompanyPassword("Nike", "123", "125213");
        });
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void wrongCompanyName() {
        Assertions.assertThrows(NoPermission.class, () -> {
            companyService.changeCompanyPassword("Qwrq", "123456", "1234567");
        });
    }

    @Test
    void changePasswordWithoutAuthentication() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            companyService.changeCompanyPassword("Nike", "123456", "1234567");
        });
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void deleteEmployee() {
        String expected = "Deleted!";
        String actual = companyService.deleteEmployee("Nike", "alex@gmail.com");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void deleteEmployeeFromAnotherCompany() {
        Assertions.assertThrows(NoPermission.class, () -> {
            companyService.deleteEmployee("Apple", "hendersen@gmail.com");
        });
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void deleteEmployeeFromAnotherCompanySecond() {
        Assertions.assertThrows(NoPermission.class, () -> {
            companyService.deleteEmployee("Nike", "hendersen@gmail.com");
        });
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void deleteEmployeeWithWrongUsername() {
        Assertions.assertThrows(WrongUsername.class, () -> {
            companyService.deleteEmployee("Nike", "wqetqewt@gmail.com");
        });
    }

    @Test
    void deleteEmployeeWithoutAuthentication() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            companyService.deleteEmployee("Nike", "alex@gmail.com");
        });
    }
}