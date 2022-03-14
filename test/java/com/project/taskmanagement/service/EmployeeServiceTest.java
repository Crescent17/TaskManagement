package com.project.taskmanagement.service;


import com.project.taskmanagement.model.Employee;
import com.project.taskmanagement.model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;

@SpringBootTest
@ContextConfiguration
@Transactional
class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @Test
    void register() {
        String expected = "Successful registration!";
        String actual = employeeService.register(new Employee("Pavel", "Markov", "mark21@gmail.com", "123456", "Puma"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void registerForNonExistingCompany() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
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
        Assertions.assertThrows(IllegalStateException.class, () -> {
            employeeService.assignTask(5L, new Task("Assign"));
        });
    }
}