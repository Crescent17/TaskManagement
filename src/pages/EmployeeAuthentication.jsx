import {useState} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import Header from "./Header";

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
        <div className="loginPage">
            <Header/>
            <div className="login">
                Authorization
                <form onSubmit={authenticate}>
                    <input type="text" className="inputField" placeholder="Username"
                           onChange={e => setLogin(e.target.value)}
                           required={true}/>
                    <input type="password" className="inputField" placeholder="Password"
                           onChange={e => setPassword(e.target.value)} required={true}/>
                    <br/>
                    <button className="loginButton">Log in</button>
                </form>
                <p>{message}</p>
            </div>
        </div>
    )

}

export default EmployeeAuthentication;