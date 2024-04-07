import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom";
import { callAPI } from './CallAPI';
import { Paper } from "@mui/material";
import { useTranslation } from "react-i18next";
import CircularProgress from "@mui/material/CircularProgress";

export default function Exercise() {
    const navigate = useNavigate()
    const { exerciseID } = useParams();
    const [exerciseData, setExerciseData] = useState([]);

    const [picture, setPicture] = useState(undefined)
    const [isPictureLoading, setIsPictureLoading] = useState(false)

    const { t } = useTranslation();

    useEffect(() => {
        callAPI("GET", "exercise/getExercise/" + exerciseID, null, null)
            .then((response) => {
                setExerciseData(response.data)
                console.log(response.data)
            })
            .catch((error) => {
                if (error.response && error.response.data === "Token expired") {
                    navigate("/tokenExpired")
                }
            }
            )
    }, [])

    useEffect(() => {
        setIsPictureLoading(true)
        callAPI("GET", "exercise/getExercise/picture/" + exerciseID, null, null)
            .then(response => {
                setPicture(response.data)
                setIsPictureLoading(false)
            })
            .catch(error => {
                console.error("An error occurred while loading the image.", error)
                setIsPictureLoading(false)
                if (error.response && error.response.data === "Token expired") {
                    navigate("/tokenExpired")
                }
            }

            )
    }, [])

    function renderExercise(localization){
            
            if(localization === "cs"){
                if(exerciseData.type === "RUN"){
                    return(
                        <>
                        Name: {exerciseData.name}<br/>
                        Description: {exerciseData.description}<br/>
                        Type: {exerciseData.type}<br/>
                        Category: {exerciseData.category}<br/>
                        Length: {exerciseData.length}<br/>
                        {exerciseData.tagsJSON}
                    </>
                    )
                } else if (exerciseData.type === "GYM"){
                    return(
                        <>
                        Name: {exerciseData.name}<br/>
                        Description: {exerciseData.description}<br/>
                        Type: {exerciseData.type}<br/>
                        Repetitions: {exerciseData.repetitions}<br/>
                        Series: {exerciseData.series}<br/>
                        {exerciseData.tagsJSON}
                    </>
                    )
                } else {
                    return(
                        <>
                        Name: {exerciseData.name}<br/>
                        Description: {exerciseData.description}<br/>
                        Type: {exerciseData.type}<br/>
                        Style: {exerciseData.style}<br/>
                        Length: {exerciseData.length}<br/>
                        {exerciseData.tagsJSON}

                    </>
                    )
                }

                   
            } else {
                if(exerciseData.type === "RUN"){
                    return(
                        <>
                        Name: {exerciseData.name_eng}<br/>
                        Description: {exerciseData.description_eng}<br/>
                        Type: {exerciseData.type}<br/>
                        Category: {exerciseData.category}<br/>
                        Length: {exerciseData.length}<br/>
                        {exerciseData.tagsJSON}
                    </>
                    )
                } else if (exerciseData.type == "GYM"){
                    return(
                        <>
                        Name: {exerciseData.name_eng}<br/>
                        Description: {exerciseData.description_eng}<br/>
                        Type: {exerciseData.type}<br/>
                        Repetitions: {exerciseData.repetitions}<br/>
                        Series: {exerciseData.series}<br/>
                        {exerciseData.tagsJSON}
                    </>
                    )
                } else {
                    return(
                        <>
                        Name: {exerciseData.name_eng}<br/>
                        Description: {exerciseData.description_eng}<br/>
                        Type: {exerciseData.type}<br/>
                        Style: {exerciseData.style}<br/>
                        Length: {exerciseData.length}<br/>
                        {exerciseData.tagsJSON}

                    </>
                    )
                }
            }
    }

    return (
        <>
            <div className="exerciseFlex">


                <Paper elevation={3} style={{ maxWidth: '100%' }} className='paperExercise'>
                    <div>
                        {exerciseData && (

                            renderExercise(localStorage.getItem("Localization"))
                          
                        )}
                    </div>
                </Paper>
                <div style={{ height: "250px", marginTop: "20px", display: "flex" }}>
                    {isPictureLoading ?
                        <CircularProgress />
                        :
                        <img src={`data:image/png;base64,${picture}`}
                            alt="Exercise Picture"
                        />
                    }
                </div>
            </div>
        </>
    );
}
