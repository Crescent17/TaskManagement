import {useEffect, useState} from "react";
import Footer from "./Footer";
import {useNavigate} from "react-router-dom";
import PopUp from "../service/PopUp";
import axios from "axios";
import CompanyService from "../service/CompanyService";
import Header from "./Header";
import {Box, Button, Container, Table, TableBody, TableCell, TableHead, TableRow} from "@mui/material";
import Typography from "@mui/material/Typography";

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
    return <Box>
        <Header/>
        <Table>
            <TableHead>
                <TableRow>
                    <TableCell sx={{textAlign: "center", fontSize: "20px"}}>Id</TableCell>
                    <TableCell sx={{textAlign: "center", fontSize: "20px"}}>Name</TableCell>
                    <TableCell sx={{textAlign: "center", fontSize: "20px"}}>Username</TableCell>
                    <TableCell sx={{textAlign: "center", fontSize: "20px"}}>Password</TableCell>
                </TableRow>
            </TableHead>
            <TableBody>
                {company.map(
                    company =>
                        <TableRow key={company.companyId}>
                            <TableCell sx={{textAlign: "center", fontSize: "20px"}}>{company.companyId}</TableCell>
                            <TableCell sx={{textAlign: "center", fontSize: "20px"}}>{company.name}<br/>
                                <Button color={"success"} variant={"contained"}
                                        onClick={() => PopUp.openForm(company.name)}>Change
                                </Button>
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
                            </TableCell>
                            <TableCell sx={{textAlign: "center", fontSize: "20px"}}>{company.username}<br/>
                                <Button color={"success"} variant={"contained"}
                                        onClick={() => PopUp.openForm(company.username)}>Change
                                </Button>
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
                            </TableCell>
                            <TableCell sx={{textAlign: "center", fontSize: "20px"}}>{company.password}<br/>
                                <Button color={"success"} variant={"contained"}
                                        onClick={() => PopUp.openForm(company.password)}>Change
                                </Button>
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
                            </TableCell>
                        </TableRow>
                )}
            </TableBody>
        </Table>
        <Container sx={{textAlign: "center", mt: "2%"}}><Button color={"warning"} variant={"contained"}
                                                                sx={{textAlign: "center"}}
                                                                onClick={() => navigation("/company/info")}>Show
            employees</Button></Container>
        <Container><Typography mt="40px" textAlign={"center"} color={"crimson"} fontWeight={"bold"}
                               fontSize="40px">{message}</Typography></Container>
        <Footer/>
    </Box>
}

export default CompanyCabinet