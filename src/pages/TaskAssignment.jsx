import React, {useState} from "react";
import {useSearchParams} from "react-router-dom";
import TaskService from "../service/TaskService";
import Header from "./Header";

function TaskAssignment() {
    const [searchParams, setSearchParams] = useSearchParams()
    const [explanation, setExplanation] = useState()
    const [message, setMessage] = useState()
    let company = searchParams.get("company")
    let id = searchParams.get("id")


    return (
        <div>
            <Header/>
            <div className="taskAssignment">
                Assign Task
                <div>
                    <form onSubmit={(event) => {
                        event.preventDefault()
                        TaskService.addTask(company, id, explanation)
                            .then(response => setMessage(response.data))
                            .catch(error => setMessage(error.response.data))
                    }}>
                        <p><textarea className="comment" onChange={e => setExplanation(e.target.value)}
                                     required={true}/>
                        </p>
                        <p>
                            <button className="assignTaskButton">Assign
                            </button>
                        </p>
                    </form>
                </div>
                <div className="message-wrapper"><p className="message">{message}</p></div>
            </div>
        </div>
    )
}

export default TaskAssignment