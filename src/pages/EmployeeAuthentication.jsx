import {useState} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import Header from "./Header";
import {Box, Button, Container, TextField} from "@mui/material";
import Typography from "@mui/material/Typography";

function EmployeeAuthentication() {
    const [login, setLogin] = useState()
    const [password, setPassword] = useState()
    const [message, setMessage] = useState()
    const navigate = useNavigate()

    function authenticate(event) {
        event.preventDefault()
        axios.post("http://localhost:8080/employee/authenticate", {
            username: login,
            password: password
        })
            .then(response => {
                localStorage.setItem("employeeToken", response.data.jwt)
                navigate("../employee/cabinet")
            })
            .catch(error => setMessage(error.response.data))
    }

    return (
        <Box>
            <Header/>
            <Container sx={{textAlign: "center", marginTop: "13%"}}>
                <Typography fontWeight={"bold"} fontSize="30px">Employee Authentication</Typography>
                <Box>
                    <form>
                        <TextField required label="Login" variant={"outlined"}
                                   sx={{mt: "5%", background: "ghostwhite", width: "30%"}}
                                   onChange={e => setLogin(e.target.value)}/>
                        <TextField required label="Password" variant={"outlined"} type={"password"}
                                   sx={{ml: "2%", mt: "5%", background: "ghostwhite", width: "30%"}}
                                   onChange={e => setPassword(e.target.value)}/>
                        <br/>
                        <Button type={"submit"} sx={{
                            mt: "5%",
                            width: "30%",
                            border: "ghostwhite solid",
                            color: "white",
                            background: "black",
                            display: "inline-block",
                        }} onClick={authenticate}>Login</Button>
                    </form>
                </Box>
                <Typography mt="1%" fontWeight={"bold"} color={"crimson"} fontSize="30px">{message}</Typography>
            </Container>
        </Box>
    )

}

export default EmployeeAuthentication;