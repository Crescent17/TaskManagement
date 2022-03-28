import {useEffect, useState} from "react";
import axios from "axios";
import PopUp from "../service/PopUp";
import EmployeeService from "../service/EmployeeService";
import Header from "./Header";

function EmployeeCabinet() {
    const [employee, setEmployee] = useState([])
    const [employeeName, setEmployeeName] = useState()
    const [employeeLastName, setLastName] = useState()
    const [employeeUsername, setUsername] = useState()
    const [employeeCompany, setEmployeeCompany] = useState()
    const [oldPassword, setOldPassword] = useState()
    const [newPassword, setNewPassword] = useState()
    const [message, setMessage] = useState("")
    useEffect(() => {
        axios.get('http://localhost:8080/employee/info',
            {headers: {"authorization": "Bearer " + localStorage.getItem("employeeToken")}})
            .then((response) => {
                setEmployee(response.data)
            })
    }, [])
    return (
        <div className="info">
            <Header/>
            <table className="custom-table">
                <thead className="names">
                <tr>
                    <th>Id</th>
                    <th>Name</th>
                    <th>Surname</th>
                    <th>Username</th>
                    <th>Password</th>
                    <th>Company</th>
                    <th>Tasks</th>
                </tr>
                </thead>
                <tbody>
                {employee.map(
                    employee =>
                        <tr key={employee.employeeId}>
                            <td>{employee.employeeId}</td>
                            <td>{employee.name}<br/>
                                <button className="employee-edit-button"
                                        onClick={() => PopUp.openForm(employee.name)}>Change
                                </button>
                                <div className="form-popup" id={employee.name}>
                                    <form className="form-container">
                                        <h1>Edit</h1>
                                        <textarea name="name" required defaultValue={employee.name}
                                                  className="edit" onChange={e => setEmployeeName(e.target.value)}/>
                                        <button type="submit" className="btn"
                                                onClick={(event) => {
                                                    event.preventDefault()
                                                    EmployeeService
                                                        .editEmployeeName(employee.name, employeeName)
                                                        .then(response => setMessage(response.data))
                                                        .catch(error => setMessage(error.response.data))
                                                }}>Save
                                        </button>
                                        <button type="button" className="btn cancel"
                                                onClick={() => PopUp.closeForm(employee.name)}>Close
                                        </button>
                                    </form>
                                </div>
                            </td>
                            <td>{employee.lastName}<br/>
                                <button className="employee-edit-button"
                                        onClick={() => PopUp.openForm(employee.lastName)}>Change
                                </button>
                                <div className="form-popup" id={employee.lastName}>
                                    <form className="form-container">
                                        <h1>Edit</h1>
                                        <textarea name="surname" required defaultValue={employee.lastName}
                                                  className="edit" onChange={e => setLastName(e.target.value)}/>
                                        <button type="submit" className="btn"
                                                onClick={(event) => {
                                                    event.preventDefault()
                                                    EmployeeService
                                                        .editEmployeeLastName(employee.lastName, employeeLastName)
                                                        .then(response => setMessage(response.data))
                                                        .catch(error => setMessage(error.response.data))
                                                }}>Save
                                        </button>
                                        <button type="button" className="btn cancel"
                                                onClick={() => PopUp.closeForm(employee.lastName)}>Close
                                        </button>
                                    </form>
                                </div>
                            </td>
                            <td className="username">{employee.username}<br/>
                                <button className="employee-edit-button"
                                        onClick={() => PopUp.openForm(employee.username)}>Change
                                </button>
                                <div className="form-popup" id={employee.username}>
                                    <form className="form-container">
                                        <h1>Edit</h1>
                                        <h6>You will have to re-login!</h6>
                                        <textarea name="username" required defaultValue={employee.username}
                                                  className="edit" onChange={e => setUsername(e.target.value)}/>
                                        <button type="submit" className="btn"
                                                onClick={(event) => {
                                                    event.preventDefault()
                                                    EmployeeService
                                                        .editEmployeeUsername(employee.username, employeeUsername)
                                                        .then(response => setMessage(response.data + ' Please re-login'))
                                                        .catch(error => setMessage(error.response.data))
                                                }}>Save
                                        </button>
                                        <button type="button" className="btn cancel"
                                                onClick={() => PopUp.closeForm(employee.username)}>Close
                                        </button>
                                    </form>
                                </div>
                            </td>
                            <td className="password">{employee.password}<br/>
                                <button className="employee-edit-button"
                                        onClick={() => PopUp.openForm(employee.password)}>Change
                                </button>
                                <div className="form-popup" id={employee.password}>
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
                                                    EmployeeService
                                                        .editEmployeePassword(oldPassword, newPassword)
                                                        .then(response => setMessage(response.data))
                                                        .catch(error => setMessage(error.response.data))
                                                }}>Save
                                        </button>
                                        <button type="button" className="btn cancel"
                                                onClick={() => PopUp.closeForm(employee.password)}>Close
                                        </button>
                                    </form>
                                </div>
                            </td>
                            <td>{employee.companyName}<br/>
                                <button className="employee-edit-button"
                                        onClick={() => PopUp.openForm(employee.companyName)}>Change
                                </button>
                                <div className="form-popup" id={employee.companyName}>
                                    <form className="form-container">
                                        <h1>Edit</h1>
                                        <textarea name="company" required defaultValue={employee.companyName}
                                                  className="edit" onChange={e => setEmployeeCompany(e.target.value)}/>
                                        <button type="submit" className="btn"
                                                onClick={(event) => {
                                                    event.preventDefault()
                                                    EmployeeService
                                                        .editEmployeeCompany(employeeCompany)
                                                        .then(response => setMessage(response.data))
                                                        .catch(error => setMessage(error.response.data))
                                                }}>Save
                                        </button>
                                        <button type="button" className="btn cancel"
                                                onClick={() => PopUp.closeForm(employee.companyName)}>Close
                                        </button>
                                    </form>
                                </div>
                            </td>
                            <td>{employee.task.map(task => <div>{task.explanation}</div>)}</td>
                        </tr>
                )}
                </tbody>
            </table>
            <div className="message-wrapper"><p className="message">{message}</p></div>
        </div>
    )
}

export default EmployeeCabinet