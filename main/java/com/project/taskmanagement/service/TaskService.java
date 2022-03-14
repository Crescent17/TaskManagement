package com.project.taskmanagement.service;

import com.project.taskmanagement.model.Company;
import com.project.taskmanagement.model.Employee;
import com.project.taskmanagement.model.Task;
import com.project.taskmanagement.repository.EmployeeRepository;
import com.project.taskmanagement.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Company> companyOptional = companyService.findByName(companyName);
        if (!companyOptional.isEmpty()) {
            if (companyService.findByUsername(authentication.getName()).get(0).getName().equalsIgnoreCase(companyName)) {
                Company company = companyOptional.get(0);
                Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
                if (employeeOptional.isPresent()) {
                    Employee employee = employeeOptional.get();
                    if (Objects.equals(company.getCompanyId(), employee.getCompany().getCompanyId())) {
                        task.setEmployee(employeeRepository.findById(employeeId).get());
                        employeeService.assignTask(employeeId, task);
                        taskRepository.save(task);
                        return "Saved";
                    }
                    return "Employee with such id doesn't belong to this company!";
                }
                return "Employee with such id doesn't exist!";
            }
            return "You don't have permission to assign tasks to users who are not in you company!";
        }
        return "The company with such name doesn't exist";
    }

    public String deleteTask(String companyName, Long taskId) {
        if (companyValidation(companyName)) {
            if (taskRepository.findById(taskId).isPresent()) {
                taskRepository.deleteByTaskId(taskId);
                return "Deleted!";
            }
            return "No task with such id!";
        }
        return "You cannot delete task for this company!";
    }

    public String updateTask(String companyName, Long taskId, Task task) {
        if (companyValidation(companyName)) {
            Optional<Task> oldTaskOptional = taskRepository.findById(taskId);
            if (oldTaskOptional.isPresent()) {
                taskRepository.updateByTaskId(taskId, task.getExplanation());
                return "Changed!";
            }
            return "The task with such id is not found!";
        }
        return "You cannot update task for this company!";
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
                    return "This user doesn't belong to " + companyName + " company";
                }
                return "Wrong employee id";
            }
            return "Wrong task id";
        }
        return "Wrong company name";
    }

    public boolean companyValidation(String companyName) {
        List<Company> companyOptional = companyService.findByName(companyName);
        if (!companyOptional.isEmpty()) {
            Company company = companyOptional.get(0);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (company.getUsername().equalsIgnoreCase(authentication.getName())) {
                return true;
            }
            return false;
        }
        return false;
    }
}

