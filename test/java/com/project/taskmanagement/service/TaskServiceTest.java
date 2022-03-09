package com.project.taskmanagement.service;


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

@SpringBootTest
@ContextConfiguration
@Transactional
public class TaskServiceTest {

    @Autowired
    private TaskService taskService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Test
    void assignTask() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        String expected = "Saved";
        String actual = taskService.assignTask("Adidas", 2L, new Task("Assigned task"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void assignTaskForEmployeeFromAnotherCompany() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        String expected = "Employee with such id doesn't belong to this company!";
        String actual = taskService.assignTask("Adidas", 1L, new Task("Another company"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void assignTaskForEmployeeWithWrongId() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        String expected = "Employee with such id doesn't exist!";
        String actual = taskService.assignTask("Adidas", 0L, new Task("Wrong ID"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void assignTaskWithoutPermission() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        String expected = "You don't have permission to assign tasks to users who are not in you company!";
        String actual = taskService.assignTask("Nike", 4L, new Task("Without permission"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void assignTaskForWrongCompany() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        Assertions.assertThrows(IllegalStateException.class, () -> {
            taskService.assignTask("Qwer", 4L, new Task("Wrong company"));
        });
    }

    @Test
    void assignTaskWithoutAuthentication() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            taskService.assignTask("Adidas", 2L, new Task("Non authenticated"));
        });
    }

    @Test
    void deleteTask() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        String expected = "Deleted!";
        String actual = taskService.deleteTask("Adidas", 2L);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteNonExistingTask() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        String expected = "No task with such id!";
        String actual = taskService.deleteTask("Adidas", 10L);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void deleteTaskForWrongCompany() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        Assertions.assertThrows(IllegalStateException.class, () -> {
            taskService.deleteTask("Eweqr", 5L);
        });
    }

    @Test
    void deleteTaskWithoutAuthentication() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            taskService.deleteTask("Adidas", 2L);
        });
    }

    @Test
    void updateTask() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        String expected = "Changed!";
        String actual = taskService.updateTask("Adidas", 2L, new Task("Changed task"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateTaskWithWrongId() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        String expected = "The task with such id is not found!";
        String actual = taskService.updateTask("Adidas", 10L, new Task("Wrong id"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void updateTaskForWrongCompany() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        Assertions.assertThrows(IllegalStateException.class, () -> {
            taskService.updateTask("Qwerq", 5L, new Task("Wrong company"));
        });
    }

    @Test
    void updateTaskWithoutAuthentication() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            taskService.updateTask("Adidas", 2L, new Task("Non authenticated"));
        });
    }

    @Test
    void assignToOtherEmployee() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        String expected = "The task was reassigned!";
        String actual = taskService.assignToOtherEmployee("Adidas", 1L, 3L);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void assignToEmployeeFromAnotherCompany() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        String expected = "This user doesn't belong to Adidas company";
        String actual = taskService.assignToOtherEmployee("Adidas", 1L, 1L);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void assignToEmployeeWithWrongId() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        String expected = "Wrong employee id";
        String actual = taskService.assignToOtherEmployee("Adidas", 1L, 10L);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void assignToEmployeeWrongTaskId() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        String expected = "Wrong task id";
        String actual = taskService.assignToOtherEmployee("Adidas", 10L, 3L);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void assignToEmployeeWithWrongCompanyName() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        Assertions.assertThrows(IllegalStateException.class, () -> {
            taskService.assignToOtherEmployee("wqrwrweq", 5L, 2L);
        });
    }

    @Test
    void assignToEmployeeWithoutAuthentication() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            taskService.assignToOtherEmployee("Adidas", 2L, 5L);
        });
    }

    @Test
    void companyValidation() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        boolean expected = true;
        boolean actual = taskService.companyValidation("Adidas");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void companyValidationForWrongCompanyName() {
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken("adidas@gmail.com", "123456");
        Authentication authentication = authenticationManager.authenticate(authReq);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
        boolean expected = false;
        boolean actual = taskService.companyValidation("Nike");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void companyValidationWithoutAuthentication() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            taskService.companyValidation("Adidas");
        });
    }
}
