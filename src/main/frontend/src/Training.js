import { Paper, Button } from "@mui/material";
import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { callAPI, callAPINoAuth } from "./CallAPI";

export default function Training() {
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
        })
    }, [])

    function generateTraining(trainingType) {
        navigate(`/generateTraining/${trainingType}`);
    }

    return (
        pageContent
    );

}