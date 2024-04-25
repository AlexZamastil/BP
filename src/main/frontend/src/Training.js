import { Paper, Button, LinearProgress } from "@mui/material";
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
    const [trainingID, setTrainingID] = useState(null);
    const [day, setDay] = useState(null);
    const [trainingInfo, setTrainingInfo] = useState(null);
    const [waterIntake, setWaterIntake] = useState(null);
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
                setWaterIntake(response.data.waterintake);

                callAPINoAuth("GET", "training/hasActiveTraining/" + id, null, null)
                    .then(response => {
                        if (response.data !== "no") {

                            setTrainingID(response.data);

                            callAPINoAuth("GET", "training/getActiveTraining/" + id, null, null)
                                .then(response => {
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

    const handleExerciseClick = (id) => {
        navigate(`/exercise/${id}`);
    };

    const handleFoodClick = (id) => {
        navigate(`/food/${id}`);
    };
    //function for rendering exercises as links to exercise detail
    function displayExercises(exercises, localization) {
        if (localization === "en") {
            return (
                <div>
                    {exercises.map((exercise, id) => (
                        <div key={id} onClick={() => handleExerciseClick(exercise.id)} style={{ cursor: 'pointer', color: "#61e248" }}>

                            {exercise.name_eng}
                        </div>
                    ))}
                </div>
            );
        } else return (
            <div>
                {exercises.map((exercise, id) => (
                    <div key={id} onClick={() => handleExerciseClick(exercise.id)} style={{ cursor: 'pointer', color: "#61e248" }}>

                        {exercise.name}
                    </div>
                ))}
            </div>
        );

    }
    //function for rendering menu items as links to food detail
    function displayMenu(menu, localization) {
        if (localization === "en") {
            return (
                <div>
                    {menu.map((food, id) => (
                        <div key={id} onClick={() => handleFoodClick(food.id)} style={{ cursor: 'pointer', color: "#61e248", padding: "10px" }}>
                            {food.name_eng} 
                        </div>
                    ))}
                </div>
            );
        } else return (
            <div>
                {menu.map((food, id) => (
                    <div key={id} onClick={() => handleFoodClick(food.id)} style={{ cursor: 'pointer', color: "#61e248", padding: "10px" }}>
                        {food.name} 
                    </div>
                ))}
            </div>
        );
    }


    //function that processes training data and renders them
    function displayTraining(dayData) {
        let today = new Date();
        today.setDate(today.getDate())
        let todayTraining = null;
        let tomorrowTraining = null;
        let progress = null;
        let localization = localStorage.getItem("Localization")
        console.log(dayData);
        console.log(waterIntake);
        progress = Math.floor(dayData.daysSoFar+10/dayData.daysTotal*100);

        for (let i = 0; i < dayData.trainingDays.length; i++) {
            let trainingDate = new Date(dayData.trainingDays[i].date);
            console.log(trainingDate)
            if (trainingDate.toDateString() === today.toDateString()) {
                setDay(i);
                todayTraining = dayData.trainingDays[i];
                tomorrowTraining = dayData.trainingDays[i + 1];
                break;
            }
        }


        return (<>

            <div className="displayTraining">

                <Paper className="displayTodayTraining">
                    <h2>{t("today_traning")}</h2> <br />

                    {t("date")}{todayTraining.date}<br />
                    {t("calories")}{todayTraining.caloriesburned}<br />
                    {t("exercises")} {displayExercises(todayTraining.exercises, localization)}<br />
                    {t("calories_gain")}<p> {todayTraining.caloriesgained}</p>  <br />
                    {t("menu")}  {displayMenu(todayTraining.menu, localization)}<br />
                </Paper>

                <Paper className="displayTomorrowTraining">
                    <h2>{t("tomorrow_training")}</h2><br />
                    {t("date")} {tomorrowTraining.date}<br />
                    {t("calories")}{tomorrowTraining.caloriesburned}<br />
                    {t("exercises")}{displayExercises(tomorrowTraining.exercises, localization)}<br />
                    {t("calories_gain")} <p>{tomorrowTraining.caloriesgained} </p> <br />
                    {t("menu")} {displayMenu(tomorrowTraining.menu, localization)}<br />


                </Paper>

                <Paper className="displayProgress">
                    <h2>{t("progress")}</h2>
                    {t("progress_stat")} {day} <br />
                    {t("progress_stat2")} {progress}%
                    <LinearProgress variant="determinate" value={progress} style={{
                        width: '200px',
                        margin: 'auto',
                        marginTop: '20px',
                        borderRadius: '10px',
                        backgroundColor: '#e0e0e0',
                        height: '10px',
                    }} />
                    <br/>
                    {t("raceDate")}   <br/>
                    {dayData.trainingInfo.raceDay}
                </Paper>

            </div>
            <Paper style={{ alignContent: "center" }}>

            </Paper>
            <Button style={{ margin: '10px' }} color="secondary" variant="contained" onClick={() => navigate("/deleteTraining/" + trainingID)}>{t("delete_training_button")}</Button>

        </>)
    }
    return (
        pageContent
    );

}