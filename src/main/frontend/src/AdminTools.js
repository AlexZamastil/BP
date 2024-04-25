import { useState, useEffect } from "react";
import { Paper } from "@mui/material";
import { Container } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { useTranslation } from 'react-i18next';
import { Button } from "@mui/material";
import { callAPI } from './CallAPI';


/**
 * Author: Alex Zamastil
 * This file displays availible tools for administrator.
 */

export default function AdminTools() {

  const [isAdmin, setIsAdmin] = useState(false);
  const navigate = useNavigate();
  const { t } = useTranslation();

  useEffect(()=>{
    console.log(isAdmin);
  },[isAdmin])

  //getting user data from server
  useEffect(() => {
    callAPI("GET", "user/getUserData", null, null)
      .then(response => {
        console.log(response.data)
        setIsAdmin((response.data.user.role === "ROLE_ADMIN"));
      })
      .catch(error => {
        console.error('Error fetching data:', error);
        if (error.response && error.response.data === "Token expired") {
          navigate("/tokenExpired")
        }
      });
  }, []);

//returns admin tools
  return (
    <div>
      {isAdmin !== undefined ? (
        isAdmin ? (
          <Container style={{ padding: "20px" }}>
            <Paper elevation={3} className='paper'>
              <h1>{t("admin_tools")} </h1>
              <br />
              <Button style={{margin: "5px"}} color="dark" variant="contained" onClick={()=>navigate("/AddExercise")}> Add Exercise </Button>
              <Button style={{margin: "5px"}} color="dark" variant="contained" onClick={()=>navigate("/AddFood")}> Add Food </Button>
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