import CompanyAuthentication from "./pages/CompanyAuthentication";
import EmployeeRegistration from "./pages/EmployeeRegistration";
import CompanyRegistration from "./pages/CompanyRegistration";
import CompanyInfo from "./pages/CompanyInfo";
import TaskAssignment from "./pages/TaskAssignment";
import {BrowserRouter, Routes, Route} from "react-router-dom";
import Footer from "./pages/Footer";
import EmployeeCabinet from "./pages/EmployeeCabinet";
import StartingComponent from "./pages/StartingComponent";
import EmployeeAuthentication from "./pages/EmployeeAuthentication";
import ProtectedRoutes from "./pages/ProtectedRoutes";
import CompanyCabinet from "./pages/CompanyCabinet";
import "./css/style.css"

function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<StartingComponent/>}/>
                <Route path="/company/register" element={<CompanyRegistration/>}/>
                <Route path="/employee/register" element={<EmployeeRegistration/>}/>
                <Route path="/company/authentication" element={<CompanyAuthentication/>}/>
                <Route path="/employee/authentication" element={<EmployeeAuthentication/>}/>
                <Route element={<ProtectedRoutes/>}>
                    <Route path="/company/info" element={<CompanyInfo/>}/>
                    <Route path="/employee/cabinet" element={<EmployeeCabinet/>}/>
                    <Route path="/company/assign" element={<TaskAssignment/>}/>
                    <Route path="/company/cabinet" element={<CompanyCabinet/>}/>
                    }/></Route>
            </Routes>
            <Footer/>
        </BrowserRouter>
    );
}

export default App;
