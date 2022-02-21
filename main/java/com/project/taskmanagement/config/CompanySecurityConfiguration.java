package com.project.taskmanagement.config;

import com.project.taskmanagement.filter.JwtRequestFilter;
import com.project.taskmanagement.service.CompanyService;
import com.project.taskmanagement.service.EmployeeService;
import com.project.taskmanagement.util.MyPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class CompanySecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final CompanyService companyService;
    private final EmployeeService employeeService;
    private final MyPasswordEncoder myPasswordEncoder;
    private final JwtRequestFilter jwtRequestFilter;

    @Autowired
    public CompanySecurityConfiguration(CompanyService companyService, MyPasswordEncoder myPasswordEncoder, EmployeeService employeeService,
                                        JwtRequestFilter jwtRequestFilter) {
        this.companyService = companyService;
        this.myPasswordEncoder = myPasswordEncoder;
        this.employeeService = employeeService;
        this.jwtRequestFilter = jwtRequestFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(companyService).passwordEncoder(myPasswordEncoder.getPasswordEncoder())
                .and().userDetailsService(employeeService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/company/register").permitAll()
                .antMatchers("/employee/register").permitAll()
                .antMatchers("/company/authenticate").permitAll()
                .antMatchers("/employee/authenticate").permitAll()
                .antMatchers("/company/**").hasRole("COMPANY")
                .antMatchers("/employee/**").hasAnyRole("COMPANY", "EMPLOYEE")
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}