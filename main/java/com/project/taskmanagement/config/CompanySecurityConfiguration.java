package com.project.taskmanagement.config;

import com.project.taskmanagement.service.CompanyDetailsService;
import com.project.taskmanagement.service.EmployeeDetailsService;
import com.project.taskmanagement.util.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class CompanySecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final CompanyDetailsService companyDetailsService;
    private final EmployeeDetailsService employeeDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CompanySecurityConfiguration(CompanyDetailsService companyDetailsService, PasswordEncoder passwordEncoder, EmployeeDetailsService employeeDetailsService) {
        this.companyDetailsService = companyDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.employeeDetailsService = employeeDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(companyDetailsService).passwordEncoder(passwordEncoder.getPasswordEncoder())
                .and().userDetailsService(employeeDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/company/register").permitAll()
                .antMatchers("/company/**").hasRole("COMPANY")
                .antMatchers("/employee").hasAnyRole("COMPANY", "EMPLOYEE")
                .and().httpBasic().and().formLogin();
    }
}