package com.project.taskmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;
    private String explanation;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id", referencedColumnName = "employeeId")
    @JsonIgnore
    private Employee employee;

    public Task(String explanation) {
        this.explanation = explanation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(taskId, task.taskId) && Objects.equals(explanation, task.explanation) && Objects.equals(employee, task.employee);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, explanation, employee);
    }
}