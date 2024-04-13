import { Paper, Button, Container } from "@mui/material";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { callAPI, callAPINoAuth } from "./CallAPI";
import { useTranslation } from 'react-i18next';

/**
 * Author: Alex Zamastil
 * If user has active training, this file renders information about current training. Otherwhise it renders an offer to start the training.
 */

export default function Training() {
    const navigate = useNavigate();
    const { t } = useTranslation();
    const [trainingID,setTrainingID] = useState(null);
    const paperRun = {
        backgroundColor: `rgb(${125}, ${175}, ${255})`,
        padding: '10px',
        border: '2px solid blue',
        margin: "10px",
        maxWidth: "300px"
    };

    const paperOCR = {
        backgroundColor: `rgb(${255}, ${132}, ${132})`,
        padding: '10px',
        border: '2px solid red',
        margin: "10px",
        maxWidth: "300px"
    };

    const [pageContent, setPageContent] = useState(null);
    //getting user and training data from server and setting the page content based on result
    useEffect(() => {
        callAPI("GET", "user/getUserData", null, null)
            .then(response => {
                let id = response.data.user.id;
                callAPINoAuth("GET", "training/hasActiveTraining/" + id, null, null)
                    .then(response => {
                        console.log("response: " + response.data)
                        if (response.data !== "no") {
                            
                            setTrainingID(response.data);
                            callAPINoAuth("GET", "training/getActiveTraining/" + id, null, null)
                                .then(response => {
                                    console.log(response.data)
                                    
                                    setPageContent(displayTraining(response.data));
                                    
                                })

                        } else
                            setPageContent(
                                <div className="trainingDiv">
                                    <Paper elevation={3} style={paperRun}>
                                        <h1> <b>{t("run")} </b> </h1> <br /> {t("run_text")} <br /><br />
                                        <Button variant="contained" onClick={() => generateTraining("RUN")}>{t("submit")}</Button>
                                    </Paper>
                                    <Paper elevation={3} style={paperOCR}>
                                        <h1> <b>{t("gladiator_race")}</b> </h1> <br /> {t("gladiator_text")} <br /> <br />
                                        <Button variant="contained" onClick={() => generateTraining("OCR")}>{t("submit")}</Button>
                                    </Paper>
                                </div>
                            );
                    })
            })
            .catch((error) => {
                if (error.response && error.response.data === "Token expired") {
                    navigate("/tokenExpired")
                }
            })
    }, [t, trainingID])
    //function that navigates to generate training form
    function generateTraining(trainingType) {
        navigate(`/generateTraining/${trainingType}`);
    }
    //function that processes training data and renders them
    function displayTraining(data) {
        console.log(data)
        let today = new Date();
        today.setDate(today.getDate() + 1)
        console.log(today)
        let todayTraining = null;
        let tomorrowTraining = null;

        for (let i = 0; i < data.length; i++) {
            let trainingDate = new Date(data[i].date);
            if (trainingDate.toDateString() === today.toDateString()) {
                todayTraining = data[i];
                tomorrowTraining = data[i + 1];
                break;
            }
        }

        console.log(todayTraining)
        return (<>
            <Container >

            <Paper className="paper">
                today training <br />

                today:{todayTraining.date}<br />
                {todayTraining.caloriesburned}<br />
                {todayTraining.exercises}<br />
                {todayTraining.menu}<br />
            </Paper>

            <Paper className="paper">
                tomorrow training <br />
                tomorrow: {tomorrowTraining.date}<br />
                {tomorrowTraining.caloriesburned}<br />
                {tomorrowTraining.exercises}<br />
                {tomorrowTraining.menu}<br />
             

            </Paper>

            <Paper className="paper">
                overview & progress
            </Paper>

            <Button style={{ margin: '10px' }} color="secondary" variant="contained" onClick={() => navigate("/deleteTraining/"+trainingID)}>{t("delete_training_button")}</Button>
            </Container>

           
        </>)
    }

    return (
        pageContent
    );

}