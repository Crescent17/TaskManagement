import {useState} from "react";
import axios from "axios";
import Header from "./Header";
import {Box, Button, Container, FormControl, TextField} from "@mui/material";
import Typography from "@mui/material/Typography";
import Footer from "./Footer";

function EmployeeRegistration() {

    const [name, setName] = useState()
    const [surname, setSurname] = useState()
    const [email, setEmail] = useState()
    const [password, setPassword] = useState()
    const [company, setCompany] = useState()
    const [error, setError] = useState()

    function register(e) {
        e.preventDefault()
        axios.post("http://localhost:8080/employee/register", {
            name: name,
            lastName: surname,
            username: email,
            password: password,
            companyName: company
        }).then(response => setError(response.data)).catch(error => setError(error.response.data))
    }

    return (
        <Box>
            <Header/>
            <Container sx={{textAlign: "center", marginTop: "3%"}}>
                <Typography fontWeight={"bold"} fontSize="30px">Employee Registration</Typography>
                <Box>
                    <form>
                        <TextField required label="Name" variant={"outlined"}
                                   sx={{mt: "5%", background: "ghostwhite", width: "30%"}}
                                   onChange={e => setName(e.target.value)}/>
                        <br/>
                        <TextField required label="Surname" variant={"outlined"}
                                   sx={{mt: "5%", background: "ghostwhite", width: "30%"}}
                                   onChange={e => setSurname(e.target.value)}/>
                        <br/>
                        <TextField required label="Email" variant={"outlined"}
                                   sx={{mt: "5%", background: "ghostwhite", width: "30%"}}
                                   onChange={e => setEmail(e.target.value)}/>
                        <br/>
                        <TextField required label="Password" variant={"outlined"} type={"password"}
                                   sx={{mt: "5%", background: "ghostwhite", width: "30%"}}
                                   onChange={e => setPassword(e.target.value)}/>
                        <br/>
                        <TextField required label="Company" variant={"outlined"}
                                   sx={{mt: "5%", background: "ghostwhite", width: "30%"}}
                                   onChange={e => setCompany(e.target.value)}/>
                        <br/>
                        <Button type={"submit"} sx={{
                            mt: "5%",
                            width: "30%",
                            border: "ghostwhite solid",
                            color: "white",
                            background: "black",
                            display: "inline-block",
                        }} onClick={register}>Register</Button>
                    </form>
                </Box>
                <Typography mt="1%" fontWeight={"bold"} color={"crimson"} fontSize="30px">{error}</Typography>
            </Container>
        </Box>
    )
}

export default EmployeeRegistration