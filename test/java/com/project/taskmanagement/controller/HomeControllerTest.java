package com.project.taskmanagement.controller;




import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.taskmanagement.model.Company;
import com.project.taskmanagement.model.Employee;
import com.project.taskmanagement.model.Task;
import com.project.taskmanagement.repository.CompanyRepository;
import com.project.taskmanagement.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class HomeControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CompanyRepository companyRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void companyRegister() throws Exception {
        String jsonString = objectMapper.writeValueAsString(new Company("Adidas", "adidas@gmail.com", "123456"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/company/register").content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
    }

    @Test
    void registerExistingCompany() throws Exception {
        String jsonString = objectMapper.writeValueAsString(new Company("Nike", "nike@gmail.com", "123456"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/company/register").content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isConflict()).andReturn();
    }

    @Test
    void employeeRegister() throws Exception {
        String jsonString = objectMapper.writeValueAsString(new Employee("Alexey", "Berezin", "berezin@gmail.com",
                "123456", "Amazon"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/employee/register").content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
    }


    @Test
    void registerEmployeeForNonExistingCompany() throws Exception {
        String jsonString = objectMapper.writeValueAsString(new Employee("Alexey", "Berezin", "berezin@gmail.com",
                "123456", "Qtewqt"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/employee/register").content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isNotFound()).andReturn();
    }

    @Test
    void registerExistingEmployee() throws Exception {
        String jsonString = objectMapper.writeValueAsString(new Employee("Alex", "Jackson", "alex@gmail.com",
                "123456", "Nike"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/employee/register").content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isConflict()).andReturn();
    }

    @Test
    void assignTask() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        String jsonString = objectMapper.writeValueAsString(new Task("Assigned Task"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/nike/assign/1")
                .header("Authorization", "Bearer " + jwt).content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
    }

    @Test
    void assignTaskForEmployeeFromAnotherCompany() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        String jsonString = objectMapper.writeValueAsString(new Task("Assigned Task"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/nike/assign/2")
                .header("Authorization", "Bearer " + jwt).content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    void assignTaskForEmployeeWithWrongId() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        String jsonString = objectMapper.writeValueAsString(new Task("Assigned Task"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/nike/assign/100")
                .header("Authorization", "Bearer " + jwt).content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isNotFound()).andReturn();
    }

    @Test
    void assignTaskForAnotherCompany() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        String jsonString = objectMapper.writeValueAsString(new Task("Assigned Task"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/amazon/assign/1")
                .header("Authorization", "Bearer " + jwt).content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    void assignWithoutAuthentication() throws Exception {
        String jsonString = objectMapper.writeValueAsString(new Task("Assigned Task"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/nike/assign/1").content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    void updateTask() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        String jsonString = objectMapper.writeValueAsString(new Task("Assigned Task"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/nike/update/1")
                .header("Authorization", "Bearer " + jwt).content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
    }

    @Test
    void updateTaskWithWrongId() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        String jsonString = objectMapper.writeValueAsString(new Task("Assigned Task"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/nike/assign/10")
                .header("Authorization", "Bearer " + jwt).content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isNotFound()).andReturn();
    }

    @Test
    void updateTaskForAnotherCompany() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        String jsonString = objectMapper.writeValueAsString(new Task("Assigned Task"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/amazon/assign/1")
                .header("Authorization", "Bearer " + jwt).content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    void updateWithoutAuthentication() throws Exception {
        String jsonString = objectMapper.writeValueAsString(new Task("Assigned Task"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/nike/assign/1").content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    void deleteTask() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/nike/delete/1")
                .header("Authorization", "Bearer " + jwt);
        mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
    }

    @Test
    void deleteTaskWithWrongId() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/nike/delete/10")
                .header("Authorization", "Bearer " + jwt);
        mvc.perform(requestBuilder).andExpect(status().isNotFound()).andReturn();
    }

    @Test
    void deleteTaskForAnotherCompany() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/amazon/delete/2")
                .header("Authorization", "Bearer " + jwt);
        mvc.perform(requestBuilder).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    void deleteWithoutAuthentication() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/nike/delete/1");
        mvc.perform(requestBuilder).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    void reassignTask() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        String jsonString = objectMapper.writeValueAsString(new Task("Assigned Task"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/nike/reassign/1/4")
                .header("Authorization", "Bearer " + jwt).content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
    }

    @Test
    void reassignTaskForEmployeeFromAnotherCompany() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        String jsonString = objectMapper.writeValueAsString(new Task("Assigned Task"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/amazon/reassign/1/3")
                .header("Authorization", "Bearer " + jwt).content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    void reassignTaskForWrongEmployeeId() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        String jsonString = objectMapper.writeValueAsString(new Task("Assigned Task"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/nike/reassign/1/10")
                .header("Authorization", "Bearer " + jwt).content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isNotFound()).andReturn();
    }

    @Test
    void reassignWrongTaskId() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        String jsonString = objectMapper.writeValueAsString(new Task("Assigned Task"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/nike/reassign/10/1")
                .header("Authorization", "Bearer " + jwt).content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isNotFound()).andReturn();
    }

    @Test
    void reassignWithoutAuthentication() throws Exception {
        String jsonString = objectMapper.writeValueAsString(new Task("Assigned Task"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/nike/reassign/1/3").content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isForbidden()).andReturn();
    }
}
