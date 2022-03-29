import {useEffect, useState} from "react";
import axios from "axios";
import TaskService from "../service/TaskService";
import {useNavigate} from "react-router-dom";
import PopUp from "../service/PopUp";
import CompanyService from "../service/CompanyService";
import Header from "./Header";

function CompanyInfo() {
    const [employees, setEmployees] = useState([])
    const [companyName, setCompanyName] = useState()
    const [explanation, setExplanation] = useState()
    const [message, setMessage] = useState()
    let navigate = useNavigate()
    useEffect(() => {
        axios.get('http://localhost:8080/company/info', {headers: {"authorization": "Bearer " + localStorage.getItem("companyToken")}}
        ).then((response) => {
            setEmployees(response.data)
        })
    }, [])
    useEffect(() => {
        axios.get('http://localhost:8080/company/data',
            {headers: {Authorization: "Bearer " + localStorage.getItem("companyToken")}})
            .then((response) => {
                setCompanyName(response.data[0].name)
            })
    })


    return (
        <div className="info">
            <Header/>
            <p className="companyName">{companyName}</p>
            <table className="custom-table">
                <thead className="names">
                <tr>
                    <th>Id</th>
                    <th className="w-25">Name</th>
                    <th className="w-25">Surname</th>
                    <th className="w-25">Company</th>
                    <th className="w-100">Tasks</th>
                </tr>
                </thead>
                <tbody>
                {employees.map(
                    employee =>
                        <tr key={employee.employeeId}>
                            <td>{employee.employeeId}</td>
                            <td>{employee.name}
                                <button className="deleteButton"
                                        onClick={(event) => {
                                            event.preventDefault()
                                            CompanyService.deleteEmployee(employee.companyName, employee.username)
                                                .then(response => setMessage(response.data))
                                                .catch(error => setMessage(error.response.data))
                                        }}>Delete
                                </button>
                            </td>
                            <td>{employee.lastName}</td>
                            <td>{employee.companyName}</td>
                            <td>{employee.task.map(task =>
                                <div>{task.explanation}
                                    <button className="deleteButton"
                                            onClick={(event) => {
                                                event.preventDefault()
                                                TaskService.deleteTask(companyName, task.taskId)
                                                    .then(response => setMessage(response.data))
                                                    .catch(error => setMessage(error.response.data))
                                            }}>Delete
                                    </button>
                                    <button className="editButton" onClick={() => PopUp.openForm(task.taskId)}>Edit
                                    </button>
                                    <div className="form-popup" id={task.taskId}>
                                        <form className="form-container">
                                            <h1>Edit</h1>
                                            <textarea name="task" required defaultValue={task.explanation}
                                                      className="edit" onChange={e => setExplanation(e.target.value)}/>

                                            <button type="submit" className="btn"
                                                    onClick={(event) => {
                                                        event.preventDefault()
                                                        TaskService.updateTask(employee.companyName, task.taskId, explanation)
                                                            .then(response => setMessage(response.data))
                                                            .catch(error => setMessage(error.response.data))
                                                    }}>Save
                                            </button>
                                            <button type="button" className="btn cancel"
                                                    onClick={() => PopUp.closeForm(task.taskId)}>Close
                                            </button>
                                        </form>
                                    </div>
                                </div>
                            )}
                                <button className="assign"
                                        onClick={() => {
                                            navigate(`/company/assign/?id=${employee.employeeId}&company=${employee.companyName}`)
                                        }}>Assign
                                </button>
                            </td>
                        </tr>
                )}
                </tbody>
            </table>
            <div className="message-wrapper"><p className="message">{message}</p></div>
        </div>
    )
}


export default CompanyInfo