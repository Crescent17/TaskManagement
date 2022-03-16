package com.project.taskmanagement.service;


import com.project.taskmanagement.exception.*;
import com.project.taskmanagement.model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
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
        Assertions.assertThrows(NoPermission.class, () -> {
            taskService.assignTask("amazon", 1L, new Task("Qwe"));
        });
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void assignTaskForEmployeeWithWrongId() {
        Assertions.assertThrows(WrongEmployeeId.class, () -> {
            taskService.assignTask("nike", 10L, new Task("Qwe"));
        });
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void assignTaskWithoutPermission() {
        Assertions.assertThrows(NoPermission.class, () -> {
            taskService.assignTask("amazon", 5L, new Task("Qwe"));
        });
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void assignTaskForWrongCompany() {
        Assertions.assertThrows(WrongCompanyName.class, () -> {
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
        Assertions.assertThrows(WrongTaskId.class, () -> {
            taskService.deleteTask("nike", 10L);
        });
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void deleteTaskForWrongCompany() {
        Assertions.assertThrows(WrongCompanyName.class, () -> {
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
        Assertions.assertThrows(WrongTaskId.class, () -> {
            taskService.updateTask("nike", 6L, new Task("Qwe"));
        });
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void updateTaskWithNonExistingId() {
        Assertions.assertThrows(NonExistingTaskId.class, () -> {
            taskService.updateTask("nike", 15L, new Task("Qwe"));
        });
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void updateTaskForWrongCompany() {
        Assertions.assertThrows(WrongCompanyName.class, () -> {
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
        Assertions.assertThrows(NoPermission.class, () -> {
            taskService.assignToOtherEmployee("amazon", 1L, 2L);
        });
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void assignToEmployeeWithWrongId() {
        Assertions.assertThrows(WrongEmployeeId.class, () -> {
            taskService.assignToOtherEmployee("nike", 1L, 10L);
        });
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void assignToEmployeeWrongTaskId() {
        Assertions.assertThrows(WrongTaskId.class, () -> {
            taskService.assignToOtherEmployee("nike", 10L, 1L);
        });
    }

    @Test
    @WithMockUser(username = "nike@gmail.com", password = "123456")
    void assignToEmployeeWithWrongCompanyName() {
        Assertions.assertThrows(WrongCompanyName.class, () -> {
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
        Assertions.assertThrows(WrongCompanyName.class, () -> {
            taskService.companyValidation("Qwqr");
        });
    }

    @Test
    void companyValidationWithoutAuthentication() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            taskService.companyValidation("Nike");
        });
    }
}
