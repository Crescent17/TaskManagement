class AuthenticationService {

    logout() {
        localStorage.removeItem("employeeToken")
        localStorage.removeItem("companyToken")
    }

    isEmployeeLoggedIn() {
        let employee = localStorage.getItem('employeeToken')
        return employee !== null
    }

    isCompanyLoggedIn() {
        let company = localStorage.getItem('companyToken')
        return company !== null
    }

}

export default new AuthenticationService