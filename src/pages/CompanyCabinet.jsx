import {useEffect, useState} from "react";
import Footer from "./Footer";
import {useNavigate} from "react-router-dom";
import PopUp from "../service/PopUp";
import axios from "axios";
import CompanyService from "../service/CompanyService";
import Header from "./Header";

function CompanyCabinet() {
    const [company, setCompany] = useState([])
    const [companyName, setCompanyName] = useState()
    const [companyUsername, setCompanyUsername] = useState()
    const [oldPassword, setOldPassword] = useState()
    const [newPassword, setNewPassword] = useState()
    const [message, setMessage] = useState()
    const navigation = useNavigate()
    useEffect(() => {
        axios.get("http://localhost:8080/company/data",
            {headers: {Authorization: "Bearer " + localStorage.getItem("companyToken")}})
            .then(response => setCompany(response.data))
    }, [])
    return <div className="info">
        <Header/>
        <table className="custom-table">
            <thead className="names">
            <tr>
                <th>Id</th>
                <th>Name</th>
                <th>Username</th>
                <th>Password</th>
            </tr>
            </thead>
            <tbody>
            {company.map(
                company =>
                    <tr key={company.companyId}>
                        <td>{company.companyId}</td>
                        <td>{company.name}<br/>
                            <button className="employee-edit-button"
                                    onClick={() => PopUp.openForm(company.name)}>Change
                            </button>
                            <div className="form-popup" id={company.name}>
                                <form className="form-container">
                                    <h1>Edit</h1>
                                    <textarea name="name" required defaultValue={company.name}
                                              className="edit" onChange={e => setCompanyName(e.target.value)}/>
                                    <button type="submit" className="btn"
                                            onClick={(event) => {
                                                event.preventDefault()
                                                CompanyService
                                                    .editCompanyName(company.name, companyName)
                                                    .then(response => setMessage(response.data))
                                                    .catch(error => setMessage(error.response.data))
                                            }}>Save
                                    </button>
                                    <button type="button" className="btn cancel"
                                            onClick={() => PopUp.closeForm(company.name)}>Close
                                    </button>
                                </form>
                            </div>
                        </td>
                        <td>{company.username}<br/>
                            <button className="employee-edit-button"
                                    onClick={() => PopUp.openForm(company.username)}>Change
                            </button>
                            <div className="form-popup" id={company.username}>
                                <form className="form-container">
                                    <h1>Edit</h1>
                                    <h6>You will have to re-login!</h6>
                                    <textarea name="name" required defaultValue={company.username}
                                              className="edit" onChange={e => setCompanyUsername(e.target.value)}/>
                                    <button type="submit" className="btn"
                                            onClick={(event) => {
                                                event.preventDefault()
                                                CompanyService
                                                    .editCompanyUsername(company.username, companyUsername)
                                                    .then(response => setMessage(response.data + ' Please re-login'))
                                                    .catch(error => setMessage(error.response.data))
                                            }}>Save
                                    </button>
                                    <button type="button" className="btn cancel"
                                            onClick={() => PopUp.closeForm(company.username)}>Close
                                    </button>
                                </form>
                            </div>
                        </td>
                        <td>{company.password}<br/>
                            <button className="employee-edit-button"
                                    onClick={() => PopUp.openForm(company.password)}>Change
                            </button>
                            <div className="form-popup" id={company.password}>
                                <form className="form-container">
                                    <h1>Edit</h1>
                                    <input type="password" required className="password-field"
                                           placeholder="Enter old password"
                                           onChange={e => setOldPassword(e.target.value)}/>
                                    <input type="password" required className="password-field"
                                           placeholder="Enter new password"
                                           onChange={e => setNewPassword(e.target.value)}/>
                                    <br/>
                                    <button type="submit" className="btn"
                                            onClick={(event) => {
                                                event.preventDefault()
                                                CompanyService
                                                    .editCompanyPassword(company.name, oldPassword, newPassword)
                                                    .then(response => setMessage(response.data))
                                                    .catch(error => setMessage(error.response.data))
                                            }}>Save
                                    </button>
                                    <button type="button" className="btn cancel"
                                            onClick={() => PopUp.closeForm(company.password)}>Close
                                    </button>
                                </form>
                            </div>
                        </td>
                    </tr>
            )}
            </tbody>
        </table>
        <button onClick={() => navigation("/company/info")}>Show employees</button>
        <div className="message-wrapper"><p className="message">{message}</p></div>
        <Footer/>
    </div>
}

export default CompanyCabinet