import { useState, useEffect } from "react";
import { Paper } from "@mui/material";
import {Container} from "@mui/material";
import {useNavigate} from "react-router-dom";
import { useTranslation } from 'react-i18next';
import  {Button} from "@mui/material";

export default function AdminTools(){

    const [isAdmin, setIsAdmin] = useState(false);
    const navigate = useNavigate();
    const {t} = useTranslation();
   

    const handleAddExercise = ()=> {
        navigate("/AddExercise");
        window.location.reload(false);
      }
      
        useEffect(() => {
            fetch("https://localhost:8443/api/authorized/user/getuserdata", {
                method: "GET",
                headers: {
                    'Authorization': localStorage.getItem("token")
                }
            })
            .then(async (response) => {
                const userData = await response.json();
                setIsAdmin(userData.user.adminPrivileges);
            })
            .catch(error => {
                console.error(error);
            });
        }, []);

        return (
            <div>
              {isAdmin !== undefined ? ( 
                isAdmin ? (
                    <Container>
                    <Paper elevation={3} className='paper'>
                        <h1>Admin tools </h1>
                        <br/>
                        <Button color="dark" variant="contained" onClick={handleAddExercise}> Add Exercise </Button>
                   </Paper>
                 </Container>
                ) : (
                  <p>Admin only</p>
                )
              ) : (
                <p>Loading...</p>
              )}
            </div>
          );

         
    
}