import React from "react";
import {useNavigate} from "react-router-dom";
import AuthenticationService from "../service/AuthenticationService";
import Header from "./Header";

function StartingComponent() {
    let navigate = useNavigate()
    return (
        <div>
            <Header/>
            {!AuthenticationService.isCompanyLoggedIn() && !AuthenticationService.isEmployeeLoggedIn() &&
            <div className="info">
                <button className="employeeLogin" onClick={() => navigate("/employee/authentication")}>Login as
                    employee
                </button>
                <button className="companyLogin" onClick={() => navigate("/company/authentication")}>Login as company
                </button>
                <br/>
                <button className="employeeRegister" onClick={() => navigate("/employee/register")}>Register as
                    employee
                </button>
                <button className="companyRegister" onClick={() => navigate("/company/register")}>Register as company
                </button>
            </div>}
            {(AuthenticationService.isCompanyLoggedIn() || AuthenticationService.isEmployeeLoggedIn()) &&
            <div className="info"><p className="thanks">Thank you for using our application!</p></div>}</div>
    )
}

export default StartingComponent