package com.project.taskmanagement.service;


import com.project.taskmanagement.model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ContextConfiguration
@Transactional
public class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void assignTask() {
        String expected = "Saved";
        String actual = taskService.assignTask("Nike", 1L, new Task("Assigned task"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void assignTaskForEmployeeFromAnotherCompany() {
        String expected = "Employee with such id doesn't belong to this company!";
        String actual = taskService.assignTask("Nike", 2L, new Task("Another company"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void assignTaskForEmployeeWithWrongId() {
        String expected = "Employee with such id doesn't exist!";
        String actual = taskService.assignTask("Nike", 10L, new Task("Wrong ID"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void assignTaskWithoutPermission() {
        String expected = "You don't have permission to assign tasks to users who are not in you company!";
        String actual = taskService.assignTask("Amazon", 2L, new Task("Without permission"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void assignTaskForWrongCompany() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            taskService.assignTask("Qwer", 4L, new Task("Wrong company"));
        });
    }

    @Test
    void assignTaskWithoutAuthentication() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            taskService.assignTask("Nike", 2L, new Task("Non authenticated"));
        });
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void deleteTask() {
        String expected = "Deleted!";
        String actual = taskService.deleteTask("Nike", 1L);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void deleteNonExistingTask() {
        String expected = "No task with such id!";
        String actual = taskService.deleteTask("Nike", 10L);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void deleteTaskForWrongCompany() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            taskService.deleteTask("Eweqr", 5L);
        });
    }

    @Test
    void deleteTaskWithoutAuthentication() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            taskService.deleteTask("Nike", 2L);
        });
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void updateTask() {
        String expected = "Changed!";
        String actual = taskService.updateTask("Nike", 1L, new Task("Changed task"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void updateTaskWithWrongId() {
        String expected = "The task with such id is not found!";
        String actual = taskService.updateTask("Nike", 10L, new Task("Wrong id"));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void updateTaskForWrongCompany() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            taskService.updateTask("Qwerq", 5L, new Task("Wrong company"));
        });
    }

    @Test
    void updateTaskWithoutAuthentication() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            taskService.updateTask("Nike", 2L, new Task("Non authenticated"));
        });
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void assignToOtherEmployee() {
        String expected = "The task was reassigned!";
        String actual = taskService.assignToOtherEmployee("Nike", 1L, 4L);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void assignToEmployeeFromAnotherCompany() {
        String expected = "This user doesn't belong to Nike company";
        String actual = taskService.assignToOtherEmployee("Nike", 1L, 2L);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void assignToEmployeeWithWrongId() {
        String expected = "Wrong employee id";
        String actual = taskService.assignToOtherEmployee("Nike", 1L, 10L);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void assignToEmployeeWrongTaskId() {
        String expected = "Wrong task id";
        String actual = taskService.assignToOtherEmployee("Nike", 10L, 1L);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void assignToEmployeeWithWrongCompanyName() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            taskService.assignToOtherEmployee("wqrwrweq", 5L, 2L);
        });
    }

    @Test
    void assignToEmployeeWithoutAuthentication() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            taskService.assignToOtherEmployee("Nike", 2L, 5L);
        });
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void companyValidation() {
        boolean expected = true;
        boolean actual = taskService.companyValidation("Nike");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void companyValidationForWrongCompanyName() {
        boolean expected = false;
        boolean actual = taskService.companyValidation("Amazon");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void companyValidationWithoutAuthentication() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            taskService.companyValidation("Nike");
        });
    }
}
