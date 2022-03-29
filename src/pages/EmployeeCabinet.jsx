import {useEffect, useState} from "react";
import axios from "axios";
import PopUp from "../service/PopUp";
import EmployeeService from "../service/EmployeeService";
import Header from "./Header";
import {Box, Button, Container, Table, TableBody, TableCell, TableHead, TableRow} from "@mui/material";
import Typography from "@mui/material/Typography";

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
        <Box>
            <Header/>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell sx={{textAlign: "center", fontSize: "20px"}}>Id</TableCell>
                        <TableCell sx={{textAlign: "center", fontSize: "20px"}}>Name</TableCell>
                        <TableCell sx={{textAlign: "center", fontSize: "20px"}}>Surname</TableCell>
                        <TableCell sx={{textAlign: "center", fontSize: "20px"}}>Username</TableCell>
                        <TableCell sx={{textAlign: "center", fontSize: "20px"}}>Password</TableCell>
                        <TableCell sx={{textAlign: "center", fontSize: "20px"}}>Company</TableCell>
                        <TableCell sx={{textAlign: "center", fontSize: "20px"}}>Tasks</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {employee.map(
                        employee =>
                            <TableRow key={employee.employeeId}>
                                <TableCell sx={{textAlign: "center", fontSize: "20px"}}>{employee.employeeId}</TableCell>
                                <TableCell sx={{textAlign: "center", fontSize: "20px"}}>{employee.name}<br/>
                                    <Button color={"success"} variant={"contained"}
                                            onClick={() => PopUp.openForm(employee.name)}>Change
                                    </Button>
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
                                </TableCell>
                                <TableCell sx={{textAlign: "center", fontSize: "20px"}}>{employee.lastName}<br/>
                                    <Button color={"success"} variant={"contained"}
                                            onClick={() => PopUp.openForm(employee.lastName)}>Change
                                    </Button>
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
                                </TableCell>
                                <TableCell sx={{textAlign: "center", fontSize: "20px"}}>{employee.username}<br/>
                                    <Button color={"success"} variant={"contained"}
                                            onClick={() => PopUp.openForm(employee.username)}>Change
                                    </Button>
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
                                </TableCell>
                                <TableCell sx={{textAlign: "center", fontSize: "20px"}}>{employee.password}<br/>
                                    <Button color={"success"} variant={"contained"}
                                            onClick={() => PopUp.openForm(employee.password)}>Change
                                    </Button>
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
                                </TableCell>
                                <TableCell sx={{textAlign: "center", fontSize: "20px"}}>{employee.companyName}<br/>
                                    <Button color={"success"} variant={"contained"}
                                            onClick={() => PopUp.openForm(employee.companyName)}>Change
                                    </Button>
                                    <div className="form-popup" id={employee.companyName}>
                                        <form className="form-container">
                                            <h1>Edit</h1>
                                            <textarea name="company" required defaultValue={employee.companyName}
                                                      className="edit"
                                                      onChange={e => setEmployeeCompany(e.target.value)}/>
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
                                </TableCell>
                                <td>{employee.task.map(task => <div>{task.explanation}</div>)}</td>
                            </TableRow>
                    )}
                </TableBody>
            </Table>
            <Container><Typography mt="40px" textAlign={"center"} color={"crimson"} fontWeight={"bold"}
                                   fontSize="40px">{message}</Typography></Container>
        </Box>
    )
}

export default EmployeeCabinet