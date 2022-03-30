import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import {Link} from "@mui/material";
import AuthenticationService from "../service/AuthenticationService";

const Header = () => {
    return (
        <AppBar position={"static"}
                sx={{
                    display: "flex",
                    background: "black",
                    height: "60px",
                    textAlign: "center",
                    paddingTop: "15px",
                    fontSize: "20px"
                }}>
            <Link href="/" underline={"none"} position={"absolute"} display={"flex"} marginLeft="1%"
                  color={"white"}>Task Manager</Link>
            {AuthenticationService.isEmployeeLoggedIn() &&
            <Link href="/employee/cabinet" underline={"none"} position={"absolute"} display={"inline-block"}
                  textAlign={"center"}
                  marginLeft="49%" color={"white"}>Home</Link>}
            {AuthenticationService.isCompanyLoggedIn() &&
            <Link href="/company/cabinet" underline={"none"} position={"absolute"} display={"flex"}
                  textAlign={"center"}
                  marginLeft="49%" color={"white"}>Home</Link>}
            {(AuthenticationService.isEmployeeLoggedIn() || AuthenticationService.isCompanyLoggedIn()) &&
            <Link href="/" position={"absolute"} underline={"none"} display={"inline-block"} right="0"
                  marginRight="2%" color={"white"} onClick={() => AuthenticationService.logout()}>Logout</Link>}
        </AppBar>
    );
};
export default Header;