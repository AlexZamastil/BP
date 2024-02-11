import { useState, useEffect } from "react";
import { Paper } from "@mui/material";
import {Container} from "@mui/material";
import {useNavigate} from "react-router-dom";
import { useTranslation } from 'react-i18next';
import  {Button} from "@mui/material";
import axios from 'axios';

export default function AdminTools(){

    const [isAdmin, setIsAdmin] = useState(false);
    const navigate = useNavigate();
    const {t} = useTranslation();
   

    const handleAddExercise = ()=> {
        navigate("/AddExercise");
        window.location.reload(false);
      }
      
      useEffect(() => {
        axios.get(process.env.REACT_APP_BACKEND_API_URL+"/authorized/user/getUserData", {
            headers: {
                'Authorization': localStorage.getItem("token")
            }
        })
        .then(response => {
            setIsAdmin((response.data.user.role="ADMIN"));
        })
        .catch(error => {
            console.error('Error fetching data:', error);
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