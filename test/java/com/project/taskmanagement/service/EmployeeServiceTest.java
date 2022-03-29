package com.project.taskmanagement.service;


import com.project.taskmanagement.exception.*;
import com.project.taskmanagement.model.Employee;
import com.project.taskmanagement.model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@ContextConfiguration
@Transactional
class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @Test
    void register() {
        String expected = "Successful registration!";
        String actual = employeeService.register(new Employee("Pavel", "Markov", "mark21@gmail.com", "123456", "Apple"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void registerForNonExistingCompany() {
        Assertions.assertThrows(WrongCompanyName.class, () -> {
            employeeService.register(new Employee("Jack", "Brown", "jack363@gmail.com", "123456", "NonExisting"));
        });
    }

    @Test
    void registerWithLoginTaken() {
        Assertions.assertThrows(EntityExistsException.class, () -> {
            employeeService.register(new Employee("Kevin", "Lesley", "alex@gmail.com", "123456", "Nike"));
        });
    }

    @Test
    void assignTask() {
        String expected = "Assigned";
        String actual = employeeService.assignTask(1L, new Task("Assign"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void assignTaskForEmployeeWithWrongId() {
        Assertions.assertThrows(WrongEmployeeId.class, () -> {
            employeeService.assignTask(100L, new Task("Assign"));
        });
    }

    @Test
    @WithMockUser(username = "alex@gmail.com", password = "123456")
    void changeEmployeeName() {
        String expected = "Changed!";
        String actual = employeeService.changeEmployeeName("Alex", "Jeremy");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "alex@gmail.com", password = "123456")
    void changeEmployeeNameWithWrongPreviousName() {
        Assertions.assertThrows(WrongEmployeeName.class, () -> {
            employeeService.changeEmployeeName("Mike", "Jeremy");
        });
    }

    @Test
    void changeEmployeeNameWithoutAuthentication() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            employeeService.changeEmployeeName("Alex", "Jeremy");
        });
    }

    @Test
    @WithMockUser(username = "alex@gmail.com", password = "123456")
    void changeEmployeeLastName() {
        String expected = "Changed!";
        String actual = employeeService.changeEmployeeLastName("Jackson", "Karel");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "alex@gmail.com", password = "123456")
    void changeEmployeeLastNameWithWrongPreviousLastName() {
        Assertions.assertThrows(WrongEmployeeLastName.class, () -> {
            employeeService.changeEmployeeLastName("QWt", "Karel");
        });
    }

    @Test
    void changeEmployeeLastNameWithoutAuthentication() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            employeeService.changeEmployeeLastName("Jackson", "Karel");
        });
    }

    @Test
    @WithMockUser(username = "alex@gmail.com", password = "123456")
    void changeEmployeeUsername() {
        String expected = "Changed!";
        String actual = employeeService.changeEmployeeUsername("alex@gmail.com", "jeremy@gmail.com");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "alex@gmail.com", password = "123456")
    void chooseExistingUsername() {
        Assertions.assertThrows(EntityExistsException.class, () -> {
            employeeService.changeEmployeeUsername("alex@gmail.com", "hendersen@gmail.com");
        });
    }

    @Test
    @WithMockUser(username = "alex@gmail.com", password = "123456")
    void wrongPreviousUsername() {
        Assertions.assertThrows(WrongUsername.class, () -> {
            employeeService.changeEmployeeUsername("qwet", "jeremy@gmail.com");
        });
    }

    @Test
    void changeUsernameWithoutAuthentication() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            employeeService.changeEmployeeUsername("alex@gmail.com", "jeremy@gmail.com");
        });
    }

    @Test
    @WithMockUser(username = "alex@gmail.com", password = "123456")
    void changeEmployeePassword() {
        String expected = "Changed!";
        String actual = employeeService.changeEmployeePassword("123456", "1234567");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "alex@gmail.com", password = "123456")
    void passwordsDontMatch() {
        Assertions.assertThrows(PasswordsDontMatch.class, () -> {
            employeeService.changeEmployeePassword("12345", "1234567");
        });
    }

    @Test
    void changePasswordWithoutAuthentication() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            employeeService.changeEmployeePassword("123456", "1234567");
        });
    }

    @Test
    @WithMockUser(username = "alex@gmail.com", password = "123456")
    void changeCompany() {
        String expected = "Changed!";
        String actual = employeeService.changeCompany("Apple");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "alex@gmail.com", password = "123456")
    void changeCompanyWrongName() {
        Assertions.assertThrows(WrongCompanyName.class, () -> {
            employeeService.changeCompany("Qwqr");
        });
    }

    @Test
    void changeCompanyWithoutAuthentication() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            employeeService.changeCompany("Apple");
        });
    }

    @Test
    @WithMockUser(username = "alex@gmail.com", password = "123456")
    void printInfo() {
        List<Employee> expected = Collections.singletonList(new Employee("Alex", "Jackson", "alex@gmail.com", "$2a$12$PNkqw7cxNAzILGl3qTFFpOEmLPXJkJ9eeg0dS5O1Nu9qDpLHdoG3.", "Nike"));
        List<Employee> actual = employeeService.printInfo();
        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    void printInfoWithoutAuthentication() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            employeeService.printInfo();
        });
    }

}