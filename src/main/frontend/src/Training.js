import { Paper, Button } from "@mui/material";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { callAPI, callAPINoAuth } from "./CallAPI";
import { useTranslation } from 'react-i18next';
export default function Training() {
    const navigate = useNavigate();
    const {t} = useTranslation();
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

    useEffect(()=> {
        callAPI("GET","user/getUserData",null,null)
        .then(response =>{
            callAPINoAuth("GET","training/hasActiveTraining/"+response.data.user.id,null,null)
            .then(response=>{
                if (response.data === true) {
                    setPageContent(<div>User has active training // TEMP</div>);
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
        .catch((error)=>{
            if(error.response && error.response.data === "Token expired"){
                navigate("/tokenExpired")
           }
        })
    }, [])

    function generateTraining(trainingType) {
        navigate(`/generateTraining/${trainingType}`);
    }

    return (
        pageContent
    );

}