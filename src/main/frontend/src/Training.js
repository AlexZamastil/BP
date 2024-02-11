import { Paper, Button } from "@mui/material";
import React, { useEffect, useState } from "react";
import {useNavigate} from "react-router-dom"

export default function Training() {
    const [userStats, setUserStats] = useState([]);
    const [boolean, setBoolean] = useState(null);
    const navigate = useNavigate();

    const paperRun = {
        backgroundColor: `rgb(${255}, ${132}, ${132})`,
        padding: '20px', 
        border: '2px solid red',
        margin: "20px"
    };

    const paperOCR = {
        backgroundColor: `rgb(${125}, ${175}, ${255})`,
        padding: '20px',
        border: '2px solid blue',
        margin: "20px"
    };

    const [pageContent, setPageContent] = useState(null);


    useEffect(() => {
        fetch(process.env.REACT_APP_BACKEND_API_URL+"/authorized/user/getUserData", {
            method: "GET",
            headers: {
                'Authorization': localStorage.getItem("token")
            }
        })
        .then(async (response) => {
            const userStatsData = await response.json();
            setUserStats(userStatsData);
            return fetch(process.env.REACT_APP_BACKEND_API_URL+"/authorized/hasActiveTraining/${userStatsData.user.id}", {
                method: "GET",
                headers: {
                    'Authorization': localStorage.getItem("token")
                }
            });
        })
        .then(async (response) => {
            const booleanData = await response.json();
            setBoolean(booleanData);
            console.log(booleanData);
            if (boolean === true) {
              setPageContent(<div>User has active training // TEMP</div>);
          } else 
              setPageContent(
                  <div className="trainingDiv">
                      <Paper elevation={3} style={paperRun}>
              <h1> <b>Run</b> </h1> <br /> Suitable for beginners<br /> Get in shape <br /> <br />
              <Button variant="contained" onClick={() => generateTraining("RUN")}>Submit</Button>
            </Paper>
            <Paper elevation={3} style={paperOCR}>
              <h1> <b>Gladiator race</b> </h1> <br /> Suitable for experienced runner <br /> try a new challenge <br /> <br />
              <Button variant="contained" onClick={() => generateTraining("OCR")}>Submit</Button>
            </Paper>
                  </div>
              );
          
        })
    }, []);


    function generateTraining(trainingType) {
        navigate(`/generateTraining/${trainingType}`);
    }

    return (
       pageContent
      );
      
}