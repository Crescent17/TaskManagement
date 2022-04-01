import React, {useState} from "react";
import {useSearchParams} from "react-router-dom";
import TaskService from "../service/TaskService";
import Header from "./Header";
import {Box, Button, Container, TextField} from "@mui/material";
import Typography from "@mui/material/Typography";

function TaskAssignment() {
    const [searchParams] = useSearchParams()
    const [explanation, setExplanation] = useState()
    const [message, setMessage] = useState()
    let company = searchParams.get("company")
    let id = searchParams.get("id")


    return (
        <Box>
            <Header/>
            <Container sx={{textAlign: "center", marginTop: "10%"}}>
                <Typography fontWeight={"bold"} fontSize="40px">Assign task</Typography>
                <Container>
                    <form onSubmit={(event) => {
                        event.preventDefault()
                        TaskService.addTask(company, id, explanation)
                            .then(response => setMessage(response.data))
                            .catch(error => setMessage(error.response.data))
                    }}>
                        <TextField inputProps={{maxLength: 70}} multiline maxRows={3}
                                   sx={{width: "500px", background: "white"}}
                                   onChange={e => setExplanation(e.target.value)}
                                   required={true}/>
                        <Typography>
                            <Button type={"submit"} color="success" variant={"contained"} sx={{mt: "5%"}}>Assign
                            </Button>
                        </Typography>
                    </form>
                </Container>
                <Container className="message-wrapper"><Typography fontWeight={"bold"} fontSize="40px"
                                                                   color={"crimson"}>
                    {message}</Typography></Container>
            </Container>
        </Box>
    )
}

export default TaskAssignment