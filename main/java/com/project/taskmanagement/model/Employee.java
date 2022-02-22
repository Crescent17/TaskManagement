package com.project.taskmanagement.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Setter
@JsonIgnoreProperties({"company", "accountNonLocked", "accountNonExpired", "credentialsNonExpired", "authorities", "enabled", "myPasswordEncoder"})
@AllArgsConstructor
//@NoArgsConstructor
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
    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id", referencedColumnName = "companyId")
    private Company company;
    @Column(nullable = false)
    private String companyName;
    private String task;
    @Transient
    private boolean accountNonLocked;
    @Transient
    private boolean accountNonExpired;
    @Transient
    private boolean credentialsNonExpired;
    @Transient
    private Collection<? extends GrantedAuthority> authorities;
    @Transient
    private boolean enabled;
    @Transient
    private PasswordEncoder myPasswordEncoder = new BCryptPasswordEncoder();

    public Employee() {
    }

    public Employee(String name, String lastName, String username, String password, String companyName) {
        this.name = name;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.companyName = companyName;
    }

// TODO Dependency injection of password encoder (doesn't work)
//    @Autowired
//    public Employee(PasswordEncoder bCryptPasswordEncoder) {
//        this.passwordEncoder = bCryptPasswordEncoder;
//    }

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

    public String getPassword() {
        return myPasswordEncoder.encode(password);
    }
}
