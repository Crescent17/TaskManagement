import axios from "axios";

class TaskService {
    addTask(company, id, explanation) {
        return axios.post(`http://localhost:8080/${company}/assign/${id}`, {
            explanation: explanation
        }, {headers: {"Authorization": "Bearer " + localStorage.getItem("companyToken")}})
    }

    deleteTask(company, id) {
        return axios.delete(`http://localhost:8080/${company}/delete/${id}`,
            {headers: {"Authorization": "Bearer " + localStorage.getItem("companyToken")}})
    }

    updateTask(company, id, explanation) {
        return axios.put(`http://localhost:8080/${company}/update/${id}`, {
            explanation: explanation
        }, {headers: {"Authorization": "Bearer " + localStorage.getItem("companyToken")}})
    }
}

export default new TaskService()