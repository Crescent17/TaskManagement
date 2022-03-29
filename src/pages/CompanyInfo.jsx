import {useEffect, useState} from "react";
import axios from "axios";
import TaskService from "../service/TaskService";
import {useNavigate} from "react-router-dom";
import PopUp from "../service/PopUp";
import CompanyService from "../service/CompanyService";
import Header from "./Header";
import {Box, Button, Container, Table, TableBody, TableCell, TableHead, TableRow} from "@mui/material";
import Typography from "@mui/material/Typography";

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
        <Box>
            <Header/>
            <Typography fontWeight={"bold"} fontSize="40px" textAlign={"center"}>{companyName}</Typography>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell sx={{textAlign: "center", fontSize: "20px"}}>Id</TableCell>
                        <TableCell sx={{textAlign: "center", fontSize: "20px"}}>Name</TableCell>
                        <TableCell sx={{textAlign: "center", fontSize: "20px"}}>Surname</TableCell>
                        <TableCell sx={{textAlign: "center", fontSize: "20px"}}>Company</TableCell>
                        <TableCell sx={{textAlign: "center", fontSize: "20px"}}>Tasks</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {employees.map(
                        employee =>
                            <TableRow key={employee.employeeId}>
                                <TableCell
                                    sx={{textAlign: "center", fontSize: "20px"}}>{employee.employeeId}</TableCell>
                                <TableCell sx={{textAlign: "center", fontSize: "20px"}}>{employee.name}
                                    <Button color={"error"} variant={"contained"} sx={{float: "right"}}
                                            onClick={(event) => {
                                                event.preventDefault()
                                                CompanyService.deleteEmployee(employee.companyName, employee.username)
                                                    .then(response => setMessage(response.data))
                                                    .catch(error => setMessage(error.response.data))
                                            }}>Delete
                                    </Button>
                                </TableCell>
                                <TableCell sx={{textAlign: "center", fontSize: "20px"}}>{employee.lastName}</TableCell>
                                <TableCell
                                    sx={{textAlign: "center", fontSize: "20px"}}>{employee.companyName}</TableCell>
                                <TableCell sx={{fontSize: "20px"}}>{employee.task.map(task =>
                                    <Container>
                                        <Typography mb="25px">{task.explanation}
                                            <Button color={"error"}
                                                    variant={"contained"}
                                                    sx={{float: "right"}} onClick={(event) => {
                                                event.preventDefault()
                                                TaskService.deleteTask(companyName, task.taskId)
                                                    .then(response => setMessage(response.data))
                                                    .catch(error => setMessage(error.response.data))
                                            }}>Delete</Button>
                                            <Button color={"success"}
                                                    variant={"contained"}
                                                    sx={{float: "right"}}>Edit</Button>
                                        </Typography>
                                    </Container>)}
                                    <Container sx={{textAlign: "center"}}>
                                        <Button color={"success"} variant={"contained"}
                                                sx={{alignContent: "center"}}
                                                onClick={() =>
                                                    navigate(`../company/assign/?id=${employee.employeeId}&company=${employee.companyName}`)}>
                                            Assign</Button>
                                    </Container>
                                </TableCell>
                            </TableRow>
                    )}
                </TableBody>
            </Table>
            <Container><Typography mt="40px" textAlign={"center"} color={"crimson"} fontWeight={"bold"}
                                   fontSize="40px">{message}</Typography></Container>
        </Box>
    )
}


export default CompanyInfo