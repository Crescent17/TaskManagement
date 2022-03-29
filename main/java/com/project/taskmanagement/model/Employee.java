package com.project.taskmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({"company", "accountNonLocked", "accountNonExpired", "credentialsNonExpired", "authorities", "enabled", "myPasswordEncoder"})
@AllArgsConstructor
@NoArgsConstructor
public class Employee implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;
    @Column(nullable = false)
    private String name;
    private String lastName;
    @Column(unique = true, nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "companyId")
    private Company company;
    @Column(nullable = false)
    private String companyName;
    @OneToMany(mappedBy = "employee", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<Task> task = new ArrayList<>();

    public Employee(String name, String lastName, String username, String password, String companyName) {
        this.name = name;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.companyName = companyName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(name, employee.name) && Objects.equals(lastName, employee.lastName) && Objects.equals(username, employee.username) && Objects.equals(password, employee.password) && Objects.equals(companyName, employee.companyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lastName, username, password, company, companyName, task);
    }
}