package com.project.taskmanagement.controller;


import com.project.taskmanagement.model.Company;
import com.project.taskmanagement.model.Employee;
import com.project.taskmanagement.model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;

@SpringBootTest
@ContextConfiguration
@Transactional
public class HomeControllerTest {

    @Autowired
    private HomeController homeController;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Test
    void companyRegister() {
        String expected = "Successful registration!";
        String actual = homeController.register(new Company("BMW", "bmw@gmail.com", "123456"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void registerExistingCompany() {
        Assertions.assertThrows(EntityExistsException.class, () -> {
            homeController.register(new Company("Nike", "nike@gmail.com", "123456"));
        });
    }

    @Test
    void employeeRegister() {
        String expected = "Successful registration!";
        String actual = homeController.register(new Employee("Jack", "Lesley",
                "lesley541124@gmail.com", "123456", "Adidas"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void registerEmployeeForNonExistingCompany() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            homeController.register(new Employee("Anna", "Davies", "davies14@gmail.com", "123456", "weqtqwet"));
        });
    }

    @Test
    void registerExistingEmployee() {
        Assertions.assertThrows(EntityExistsException.class, () -> {
            homeController.register(new Employee("Anna", "Davies", "alex@gmail.com", "123456", "Adidas"));
        });
    }

    @Test
    void assignTask() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        String expected = "Saved";
        String actual = homeController.assignTask("Adidas", 2L, new Task("Assigned"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void assignTaskForEmployeeFromAnotherCompany() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        String expected = "Employee with such id doesn't belong to this company!";
        String actual = homeController.assignTask("Adidas", 1L, new Task("Wrong Id"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void assignTaskForEmployeeWithWrongId() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        String expected = "Employee with such id doesn't exist!";
        String actual = homeController.assignTask("Adidas", 10L, new Task("Wrong Id"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void assignTaskForAnotherCompany() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        String expected = "You don't have permission to assign tasks to users who are not in you company!";
        String actual = homeController.assignTask("Nike", 2L, new Task("Wrong company"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void assignTaskForNonExistingCompany() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        Assertions.assertThrows(IllegalStateException.class, () -> {
            homeController.assignTask("Qtqwet", 2L, new Task("Wrong company"));
        });
    }

    @Test
    void assignWithoutAuthentication() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            homeController.assignTask("Adidas", 2L, new Task("No authentication"));
        });
    }

    @Test
    void updateTask() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        String expected = "Changed!";
        String actual = homeController.updateTask("Adidas", 2L, new Task("Updated"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateTaskWithWrongId() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        String expected = "The task with such id is not found!";
        String actual = homeController.updateTask("Adidas", 10L, new Task("Wrong task Id"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateTaskForAnotherCompany() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        String expected = "You cannot update task for this company!";
        String actual = homeController.updateTask("Nike", 1L, new Task("Qwrq"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateWithoutAuthentication() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            homeController.updateTask("Adidas", 5L, new Task("No authentication"));
        });
    }

    @Test
    void deleteTask() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        String expected = "Deleted!";
        String actual = homeController.deleteTask("Adidas", 1L);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteTaskWithWrongId() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        String expected = "No task with such id!";
        String actual = homeController.deleteTask("Adidas", 10L);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteTaskForAnotherCompany() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        String expected = "You cannot delete task for this company!";
        String actual = homeController.deleteTask("Nike", 3L);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteWithoutAuthentication() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            homeController.deleteTask("Adidas", 5L);
        });
    }

    @Test
    void reassignTask() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        String expected = "The task was reassigned!";
        String actual = homeController.reassignTask("Adidas", 1L, 3L);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void reassignTaskForEmployeeFromAnotherCompany() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        String expected = "This user doesn't belong to Adidas company";
        String actual = homeController.reassignTask("Adidas", 2L, 1L);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void reassignTaskForWrongEmployeeId() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        String expected = "Wrong employee id";
        String actual = homeController.reassignTask("Adidas", 1L, 10L);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void reassignWrongTaskId() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        String expected = "Wrong task id";
        String actual = homeController.reassignTask("Adidas", 10L, 2L);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void reassignWithWrongCompanyName() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        String expected = "Wrong company name";
        String actual = homeController.reassignTask("Nike", 4L, 2L);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void reassignWithoutAuthentication() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            homeController.reassignTask("Adidas", 2L, 2L);
        });
    }
}
