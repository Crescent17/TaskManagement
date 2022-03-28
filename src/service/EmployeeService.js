import axios from "axios";

class EmployeeService {
    editEmployeeName(previousName, newName) {
        return axios.put(`http://localhost:8080/employee/changeName/${previousName}/${newName}`, {},
            {headers: {Authorization: 'Bearer ' + localStorage.getItem('employeeToken')}})
    }

    editEmployeeLastName(previousLastName, newLastName) {
        return axios.put(`http://localhost:8080/employee/changeLastName/${previousLastName}/${newLastName}`,{},
            {headers: {"Authorization": "Bearer " + localStorage.getItem("employeeToken")}})
    }

    editEmployeeUsername(previousUsername, newUsername) {
        return axios.put(`http://localhost:8080/employee/changeUsername/${previousUsername}/${newUsername}`,{},
            {headers: {"Authorization": "Bearer " + localStorage.getItem("employeeToken")}})
    }

    editEmployeePassword(previousPassword, newPassword) {
        return axios.put(`http://localhost:8080/employee/changePassword/${previousPassword}/${newPassword}`,{},
            {headers: {"Authorization": "Bearer " + localStorage.getItem("employeeToken")}})
    }


    editEmployeeCompany(company) {
        return axios.put(`http://localhost:8080/employee/changeCompany/${company}`, {},
            {headers: {"Authorization": "Bearer " + localStorage.getItem("employeeToken")}})
    }

}

export default new EmployeeService