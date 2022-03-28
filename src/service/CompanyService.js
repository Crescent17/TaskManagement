import axios from "axios";

class CompanyService {
    editCompanyName(previousName, newName) {
        return axios.put(`http://localhost:8080/company/changeName/${previousName}/${newName}`,
            {}, {headers: {Authorization: "Bearer " + localStorage.getItem("companyToken")}})
    }

    editCompanyUsername(previousUsername, newUsername) {
        return axios.put(`http://localhost:8080/company/changeUsername/${previousUsername}/${newUsername}`,
            {}, {headers: {Authorization: "Bearer " + localStorage.getItem("companyToken")}})
    }

    editCompanyPassword(companyName, previousPassword, newPassword) {
        return axios.put(`http://localhost:8080/${companyName}/changePassword/${previousPassword}/${newPassword}`,
            {}, {headers: {Authorization: "Bearer " + localStorage.getItem("companyToken")}})
    }

    deleteEmployee(companyName, employeeUsername) {
        return axios.delete(`http://localhost:8080/${companyName}/deleteEmployee/${employeeUsername}`,
            {headers: {Authorization: 'Bearer ' + localStorage.getItem('companyToken')}})
    }
}

export default new CompanyService