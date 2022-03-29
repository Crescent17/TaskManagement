class PopUp {

    openForm(id) {
        document.getElementById(id).style.display = "block";
    }

    closeForm(id) {
        document.getElementById(id).style.display = "none";
    }
}

export default new PopUp