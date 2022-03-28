import {useState} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import Header from "./Header";

function CompanyAuthentication() {
    const [login, setLogin] = useState()
    const [password, setPassword] = useState()
    const [message, setMessage] = useState()
    const navigate = useNavigate()

    function companyAuthenticate(event) {
        event.preventDefault()
        axios.post("http://localhost:8080/company/authenticate", {
            username: login,
            password: password
        })
            .then(response => {
                localStorage.setItem("companyToken", response.data.jwt)
                navigate('/company/cabinet')
            })
            .catch(error => setMessage(error.response.data))
    }

    return (
        <div className="loginPage">
            <Header/>
            <div className="login">
                Authorization
                <form onSubmit={companyAuthenticate}>
                    <input type="text" className="inputField" placeholder="Username"
                           onChange={e => setLogin(e.target.value)}
                           required={true}/>
                    <input type="password" className="inputField" placeholder="Password"
                           onChange={e => setPassword(e.target.value)} required={true}/>
                    <br/>
                    <button className="loginButton">Log in</button>
                </form>
                <p className="message">{message}</p>
            </div>
        </div>
    )

}

export default CompanyAuthentication;