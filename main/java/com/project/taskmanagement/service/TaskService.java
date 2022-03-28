package com.project.taskmanagement.service;

import com.project.taskmanagement.exception.*;
import com.project.taskmanagement.model.Company;
import com.project.taskmanagement.model.Employee;
import com.project.taskmanagement.model.Task;
import com.project.taskmanagement.repository.EmployeeRepository;
import com.project.taskmanagement.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final CompanyService companyService;
    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;

    @Autowired
    public TaskService(TaskRepository taskRepository, CompanyService companyService, EmployeeRepository employeeRepository,
                       EmployeeService employeeService) {
        this.taskRepository = taskRepository;
        this.companyService = companyService;
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
    }

    public String assignTask(String companyName, Long employeeId, Task task) {
        if (companyValidation(companyName)) {
            Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
            if (employeeOptional.isPresent()) {
                Employee employee = employeeOptional.get();
                Company company = companyService.findByName(companyName).get(0);
                if (Objects.equals(company.getCompanyId(), employee.getCompany().getCompanyId())) {
                    task.setEmployee(employeeRepository.findById(employeeId).get());
                    employeeService.assignTask(employeeId, task);
                    taskRepository.save(task);
                    return "Saved";
                }
                throw new EmployeeFromAnotherCompany();
            }
            throw new WrongEmployeeId();
        }
        return null;
    }

    public String deleteTask(String companyName, Long taskId) {
        if (companyValidation(companyName)) {
            Optional<Task> taskByIdOptional = taskRepository.findById(taskId);
            if (taskByIdOptional.isPresent()) {
                Task task = taskByIdOptional.get();
                if (task.getEmployee().getCompanyName().equalsIgnoreCase(companyName)) {
                    taskRepository.deleteById(taskId);
                    return "Deleted!";
                }
                throw new AccessDeniedException("Access denied!");
            }
            throw new WrongTaskId();
        }
        throw new AccessDeniedException("Access denied!");
    }

    public String updateTask(String companyName, Long taskId, Task task) {
        if (companyValidation(companyName)) {
            Optional<Task> oldTaskOptional = taskRepository.findById(taskId);
            if (oldTaskOptional.isPresent()) {
                Task oldTask = oldTaskOptional.get();
                if (oldTask.getEmployee().getCompanyName().equalsIgnoreCase(companyName)) {
                    taskRepository.updateByTaskId(taskId, task.getExplanation());
                    return "Changed!";
                }
                throw new WrongTaskId();
            }
            throw new NonExistingTaskId();
        }
        throw new AccessDeniedException("Access denied!");
    }

    public String assignToOtherEmployee(String companyName, Long taskId, Long employeeId) {
        if (companyValidation(companyName)) {
            Optional<Task> taskOptional = taskRepository.findById(taskId);
            if (taskOptional.isPresent()) {
                Task task = taskOptional.get();
                Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
                if (employeeOptional.isPresent()) {
                    Employee employee = employeeOptional.get();
                    if (employee.getCompany().getName().equalsIgnoreCase(companyName)) {
                        task.setEmployee(employeeRepository.findById(employeeId).get());
                        return "The task was reassigned!";
                    }
                    throw new WrongCompanyName();
                }
                throw new WrongEmployeeId();
            }
            throw new WrongTaskId();
        }
        throw new NoPermission();
    }

    public boolean companyValidation(String companyName) {
        List<Company> companyOptional = companyService.findByName(companyName);
        if (!companyOptional.isEmpty()) {
            Company company = companyOptional.get(0);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (company.getUsername().equalsIgnoreCase(authentication.getName())) {
                return true;
            }
            throw new NoPermission();
        }
        throw new WrongCompanyName();
    }
}

