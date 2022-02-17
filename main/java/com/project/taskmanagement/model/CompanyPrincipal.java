package com.project.taskmanagement.model;

import com.project.taskmanagement.util.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CompanyPrincipal implements UserDetails {
    private final Company company;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CompanyPrincipal(Company company, PasswordEncoder passwordEncoder) {
        this.company = company;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return passwordEncoder.getPasswordEncoder().encode(company.getPassword());
    }

    @Override
    public String getUsername() {
        return company.getUsername();
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
}
