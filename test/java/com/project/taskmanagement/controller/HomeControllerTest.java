package com.project.taskmanagement.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.taskmanagement.model.AuthenticationRequest;
import com.project.taskmanagement.model.Company;
import com.project.taskmanagement.model.Employee;
import com.project.taskmanagement.model.Task;
import com.project.taskmanagement.repository.CompanyRepository;
import com.project.taskmanagement.repository.EmployeeRepository;
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
    @Autowired
    private EmployeeRepository employeeRepository;
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
        mvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();
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
    void createAuthenticationTokenForCompany() throws Exception {
        String jsonString = objectMapper.writeValueAsString(new AuthenticationRequest("nike@gmail.com", "123456"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/company/authenticate").content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
    }

    @Test
    void createAuthenticationTokenForWrongCompany() throws Exception {
        String jsonString = objectMapper.writeValueAsString(new AuthenticationRequest("qweywey@gmail.com", "123456"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/company/authenticate").content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void createAuthenticationTokenForEmployee() throws Exception {
        String jsonString = objectMapper.writeValueAsString(new AuthenticationRequest("alex@gmail.com", "123456"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/employee/authenticate").content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
    }

    @Test
    void createAuthenticationTokenForWrongEmployee() throws Exception {
        String jsonString = objectMapper.writeValueAsString(new AuthenticationRequest("qweywey@gmail.com", "123456"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/employee/authenticate").content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();
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
        mvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void assignTaskForEmployeeWithWrongId() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        String jsonString = objectMapper.writeValueAsString(new Task("Assigned Task"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/nike/assign/100")
                .header("Authorization", "Bearer " + jwt).content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();
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
        mvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();
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
        mvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();
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
    void reassignTaskWithWrongCompanyPath() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        String jsonString = objectMapper.writeValueAsString(new Task("Assigned Task"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/amazon/reassign/1/3")
                .header("Authorization", "Bearer " + jwt).content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    void reassignTaskForEmployeeFromAnotherCompany() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        String jsonString = objectMapper.writeValueAsString(new Task("Assigned Task"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/nike/reassign/1/3")
                .header("Authorization", "Bearer " + jwt).content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void reassignTaskForNonExistingEmployeeId() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        String jsonString = objectMapper.writeValueAsString(new Task("Assigned Task"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/nike/reassign/1/10")
                .header("Authorization", "Bearer " + jwt).content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void reassignWrongTaskId() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        String jsonString = objectMapper.writeValueAsString(new Task("Assigned Task"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/nike/reassign/10/1")
                .header("Authorization", "Bearer " + jwt).content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void reassignWithoutAuthentication() throws Exception {
        String jsonString = objectMapper.writeValueAsString(new Task("Assigned Task"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/nike/reassign/1/3").content(jsonString)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON);
        mvc.perform(requestBuilder).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    void changeCompanyName() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/company/changeName/Nike/Nike20")
                .header("Authorization", "Bearer " + jwt);
        mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
    }

    @Test
    void changeCompanyNameWithWrongPreviousName() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/company/changeName/wqtqwet/Nike20")
                .header("Authorization", "Bearer " + jwt);
        mvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void changeCompanyNameWithoutAuthentication() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/company/changeName/wqtqwet/Nike20");
        mvc.perform(requestBuilder).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    void changeCompanyUsername() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/company/changeUsername/nike@gmail.com/nike20@gmail.com")
                .header("Authorization", "Bearer " + jwt);
        mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
    }

    @Test
    void changeCompanyUsernameWithWrongPreviousUsername() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/company/changeUsername/qwey@gmail.com/nike20@gmail.com")
                .header("Authorization", "Bearer " + jwt);
        mvc.perform(requestBuilder).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    void changeCompanyUsernameWithExistingUsername() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/company/changeUsername/nike@gmail.com/apple@icloud.com")
                .header("Authorization", "Bearer " + jwt);
        mvc.perform(requestBuilder).andExpect(status().isConflict()).andReturn();
    }

    @Test
    void changeCompanyUsernameWithoutAuthentication() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/company/changeUsername/qwey@gmail.com/nike20@gmail.com");
        mvc.perform(requestBuilder).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    void changeCompanyPassword() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/nike/changePassword/123456/1234567")
                .header("Authorization", "Bearer " + jwt);
        mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
    }

    @Test
    void changeCompanyPasswordWithWrongCompanyName() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/apple/changePassword/123456/1234567")
                .header("Authorization", "Bearer " + jwt);
        mvc.perform(requestBuilder).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    void changeCompanyPasswordWithWrongPreviousPassword() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/nike/changePassword/wqeywq/1234567")
                .header("Authorization", "Bearer " + jwt);
        mvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void changeCompanyPasswordWithoutAuthentication() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/nike/changePassword/123456/1234567");
        mvc.perform(requestBuilder).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    void deleteEmployee() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/nike/deleteEmployee/alex@gmail.com")
                .header("Authorization", "Bearer " + jwt);
        mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
    }

    @Test
    void deleteEmployeeWithWrongCompanyName() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/apple/deleteEmployee/alex@gmail.com")
                .header("Authorization", "Bearer " + jwt);
        mvc.perform(requestBuilder).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    void deleteEmployeeWithWrongUsername() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/nike/deleteEmployee/aewyewqy@gmail.com")
                .header("Authorization", "Bearer " + jwt);
        mvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void deleteEmployeeFromAnotherCompany() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/nike/deleteEmployee/hendersen@gmail.com")
                .header("Authorization", "Bearer " + jwt);
        mvc.perform(requestBuilder).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    void deleteEmployeeWithoutAuthentication() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/nike/deleteEmployee/aewyewqy@gmail.com");
        mvc.perform(requestBuilder).andExpect(status().isForbidden()).andReturn();
    }


    @Test
    void changeEmployeeName() throws Exception {
        String jwt = jwtUtil.generateToken(employeeRepository.findByUsername("alex@gmail.com").get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employee/changeName/Alex/Jeremy")
                .header("Authorization", "Bearer " + jwt);
        mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
    }

    @Test
    void changeEmployeeNameWithWrongPreviousName() throws Exception {
        String jwt = jwtUtil.generateToken(employeeRepository.findByUsername("alex@gmail.com").get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employee/changeName/Qtewq/Jeremy")
                .header("Authorization", "Bearer " + jwt);
        mvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void changeEmployeeNameWithoutAuthentication() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employee/changeName/Alex/Jeremy");
        mvc.perform(requestBuilder).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    void changeEmployeeLastName() throws Exception {
        String jwt = jwtUtil.generateToken(employeeRepository.findByUsername("alex@gmail.com").get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employee/changeLastName/Jackson/Karel")
                .header("Authorization", "Bearer " + jwt);
        mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
    }

    @Test
    void changeEmployeeLastNameWithWrongPreviousLastName() throws Exception {
        String jwt = jwtUtil.generateToken(employeeRepository.findByUsername("alex@gmail.com").get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employee/changeLastName/QWtewqy/Karel")
                .header("Authorization", "Bearer " + jwt);
        mvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void changeEmployeeLastNameWithoutAuthentication() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employee/changeLastName/Jackson/Karel");
        mvc.perform(requestBuilder).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    void changeUsername() throws Exception {
        String jwt = jwtUtil.generateToken(employeeRepository.findByUsername("alex@gmail.com").get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employee/changeUsername/alex@gmail.com/karel@gmail.com")
                .header("Authorization", "Bearer " + jwt);
        mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
    }

    @Test
    void changeUsernameForExistingUsername() throws Exception {
        String jwt = jwtUtil.generateToken(employeeRepository.findByUsername("alex@gmail.com").get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employee/changeUsername/alex@gmail.com/hendersen@gmail.com")
                .header("Authorization", "Bearer " + jwt);
        mvc.perform(requestBuilder).andExpect(status().isConflict()).andReturn();
    }

    @Test
    void changeUsernameWithWrongPreviousUsername() throws Exception {
        String jwt = jwtUtil.generateToken(employeeRepository.findByUsername("alex@gmail.com").get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employee/changeUsername/erqyqery@gmail.com/karel@gmail.com")
                .header("Authorization", "Bearer " + jwt);
        mvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void changeUsernameWithoutAuthentication() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employee/changeUsername/alex@gmail.com/karel@gmail.com");
        mvc.perform(requestBuilder).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    void changeEmployeePassword() throws Exception {
        String jwt = jwtUtil.generateToken(employeeRepository.findByUsername("alex@gmail.com").get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employee/changePassword/123456/1234567")
                .header("Authorization", "Bearer " + jwt);
        mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
    }

    @Test
    void employeePasswordsDontMatch() throws Exception {
        String jwt = jwtUtil.generateToken(employeeRepository.findByUsername("alex@gmail.com").get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employee/changePassword/qweywqet/1234567")
                .header("Authorization", "Bearer " + jwt);
        mvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void changeEmployeePasswordWithoutAuthentication() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employee/changePassword/123456/1234567");
        mvc.perform(requestBuilder).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    void changeCompany() throws Exception {
        String jwt = jwtUtil.generateToken(employeeRepository.findByUsername("alex@gmail.com").get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employee/changeCompany/apple")
                .header("Authorization", "Bearer " + jwt);
        mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
    }

    @Test
    void changeCompanyWithWrongCompanyName() throws Exception {
        String jwt = jwtUtil.generateToken(employeeRepository.findByUsername("alex@gmail.com").get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employee/changeCompany/qwetqwet")
                .header("Authorization", "Bearer " + jwt);
        mvc.perform(requestBuilder).andExpect(status().isBadRequest()).andReturn();
    }

    @Test
    void changeCompanyWithoutAuthentication() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/employee/changeCompany/apple");
        mvc.perform(requestBuilder).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    void printEmployeeInfo() throws Exception {
        String jwt = jwtUtil.generateToken(employeeRepository.findByUsername("alex@gmail.com").get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employee/info")
                .header("Authorization", "Bearer " + jwt);
        mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
    }
    @Test
    void printEmployeeInfoWithoutAuthentication() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/employee/info");
        mvc.perform(requestBuilder).andExpect(status().isForbidden()).andReturn();
    }

    @Test
    void printCompanyInfo() throws Exception {
        String jwt = jwtUtil.generateToken(companyRepository.findByUsername("nike@gmail.com").get(0));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/company/info")
                .header("Authorization", "Bearer " + jwt);
        mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
    }

    @Test
    void printCompanyInfoWithoutAuthentication() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/company/info");
        mvc.perform(requestBuilder).andExpect(status().isForbidden()).andReturn();
    }
}
