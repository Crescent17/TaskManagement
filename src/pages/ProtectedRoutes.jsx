import {Navigate, Outlet} from "react-router-dom"

const useAuth = () => {
    const user = {loggedIn: false}
    if (localStorage.getItem('companyToken')) {
        user.loggedIn = true
    }
    if (localStorage.getItem('employeeToken')) {
        user.loggedIn = true
    }
    return user && user.loggedIn
}

const ProtectedRoutes = () => {
    const isAuth = useAuth()
    return isAuth ? <Outlet/> : <Navigate to="/"/>
}

export default ProtectedRoutes