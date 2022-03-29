import 'bootstrap/dist/css/bootstrap.css'
import {Link} from "react-router-dom";
import AuthenticationService from "../service/AuthenticationService";


function Header() {

    return <header>
        <nav className="navbar navbar-expand-md navbar-dark bg-dark">
            <div className="Title"><a className="title" href="/">Task Manager</a></div>
            <ul className="navbar-nav">
                {AuthenticationService.isEmployeeLoggedIn() &&
                <li><Link className="nav-link home" to="/employee/cabinet">Home</Link></li>}
                {AuthenticationService.isCompanyLoggedIn() &&
                <li><Link className="nav-link home" to="/company/cabinet">Home</Link></li>}
            </ul>
            <ul className="navbar-nav navbar-collapse justify-content-end">
                {AuthenticationService.isEmployeeLoggedIn() &&
                <li><Link className="nav-link logout" to="/" onClick={AuthenticationService.logout}>Logout</Link></li>}
                {AuthenticationService.isCompanyLoggedIn() &&
                <li><Link className="nav-link logout" to="/" onClick={AuthenticationService.logout}>Logout</Link></li>}
            </ul>
        </nav>
    </header>
}

export default Header