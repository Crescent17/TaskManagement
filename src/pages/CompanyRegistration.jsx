import {useState} from "react";
import axios from "axios";
import Header from "./Header";

function CompanyRegistration() {
    const [name, setName] = useState()
    const [email, setEmail] = useState()
    const [password, setPassword] = useState()
    const [error, setError] = useState()

    function register(e) {
        e.preventDefault()
        axios.post("http://localhost:8080/company/register", {
            name: name,
            username: email,
            password: password
        }).then(response => setError(response.data)).catch(error => {
            setError(error.response.data)
        })
    }

    return (
        <div>
            <Header/>
            <div className="registration">
                <p className="headline">Company registration</p>
                <form onSubmit={register}>
                    <input className="inputField" placeholder="Name" required={true}
                           onChange={e => setName(e.target.value)}/>
                    <br/>
                    <input className="inputField" placeholder="Username" required={true}
                           onChange={e => setEmail(e.target.value)}/>
                    <br/>
                    <input className="inputField" type="password" placeholder="Password" required={true}
                           onChange={e => setPassword(e.target.value)}/>
                    <br/>
                    <button className="registerButton">Register</button>
                    <p className="error">{error}</p>
                </form>
            </div>
        </div>
    )
}

export default CompanyRegistration