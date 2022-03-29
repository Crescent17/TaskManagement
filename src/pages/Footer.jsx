import 'bootstrap/dist/css/bootstrap.css'
import {BottomNavigation} from "@mui/material";
import Typography from "@mui/material/Typography";

function Footer() {
    return (
        <BottomNavigation sx={{position: "fixed", background: "black", bottom: 0, width: "100%"}}>
            <Typography color={"white"} fontWeight={"bold"} marginTop="15px">Footer</Typography>
        </BottomNavigation>
    )
}

export default Footer