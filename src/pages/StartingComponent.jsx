import React from "react";
import {useNavigate} from "react-router-dom";
import AuthenticationService from "../service/AuthenticationService";
import Header from "./Header";
import {Box, Button, Container} from "@mui/material";
import Typography from "@mui/material/Typography";

function StartingComponent() {
    let navigate = useNavigate()
    return (
        <Box>
            <Header/>
            {(AuthenticationService.isCompanyLoggedIn() || AuthenticationService.isEmployeeLoggedIn()) && <Container>
                <Typography textAlign={"center"} fontWeight={"bold"} fontSize="40px" marginTop="28%">Thank you for using
                    our application!</Typography>
            </Container>}
            {(!AuthenticationService.isCompanyLoggedIn() && !AuthenticationService.isEmployeeLoggedIn()) && <Container>
                <Container sx={{marginTop: "29%", textAlign: "center"}}>
                    <Button onClick={() => navigate("../employee/authentication")} variant={"contained"}
                            color={"primary"} sx={{width: "210px"}}>Login as employee</Button>
                    <Button onClick={() => navigate("../company/authentication")} variant={"contained"}
                            color={"primary"} sx={{width: "210px", marginLeft: "20px"}}>Login as
                        company</Button></Container>
                <Container sx={{marginTop: "30px", textAlign: "center"}}>
                    <Button onClick={() => navigate("../employee/register")} variant={"contained"} color={"primary"}>Employee
                        registration</Button>
                    <Button onClick={() => navigate("../company/register")} variant={"contained"} color={"primary"}
                            sx={{marginLeft: "20px"}}>Company
                        registration</Button>
                </Container>
            </Container>}
        </Box>
    )
}

export default StartingComponent