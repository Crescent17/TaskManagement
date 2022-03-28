import {useState} from "react";
import axios from "axios";
import Header from "./Header";

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
        <div>
            <Header/>
            <div className="registration">
                <p className="headline">Employee registration</p>
                <form onSubmit={register}>
                    <input className="inputField" placeholder="Name" required={true}
                           onChange={e => setName(e.target.value)}/>
                    <br/>
                    <input className="inputField" placeholder="Surname" required={true}
                           onChange={e => setSurname(e.target.value)}/>
                    <br/>
                    <input className="inputField" placeholder="Email" required={true}
                           onChange={e => setEmail(e.target.value)}/>
                    <br/>
                    <input className="inputField" placeholder="Password" type="password" required={true}
                           onChange={e => setPassword(e.target.value)}/>
                    <br/>
                    <input className="inputField" placeholder="Company" required={true}
                           onChange={e => setCompany(e.target.value)}/>
                    <br/>
                    <button className="registerButton">Register</button>
                    <p className="error">{error}</p>
                </form>
            </div>
        </div>
    )
}

export default EmployeeRegistration