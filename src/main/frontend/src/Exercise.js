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
                console.log("Get excercise picture response", response)
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
            
    )}, [])

    return (
        <>
        <div className="exerciseFlex">
       <div style={{height: "150px", marginTop: "20px", display: "flex"}}>
                    {isPictureLoading ?
                        <CircularProgress/>
                        :
                          <img src={`data:image/png;base64,${picture}`}
                             alt="Exercise Picture"
                        />
                    }
                </div>
           
            <Paper elevation={3} className='paperExercise'>
                <div>
                    {exerciseData && (
                        <>
                        <h2>{t('exercise_name')}</h2>
                            <div>Name: {exerciseData.name}</div>
                            <div>Name (English): {exerciseData.name_eng}</div>
                            <div>Description: {exerciseData.description}</div>
                            <div>Description (English): {exerciseData.description_eng}</div>
                            <div>Type: {exerciseData.type}</div>
                            <div>Category: {exerciseData.category}</div>
                            <div>Style: {exerciseData.style}</div>
                            <div>Length: {exerciseData.length}</div>
                            <div>Repetitions: {exerciseData.repetitions}</div>
                            <div>Series: {exerciseData.series}</div>
                            <div>Tags: {exerciseData.tagsJSON}</div>
                            
                        </>
                    )}
                </div>
            </Paper>
            </div>
        </>
    );
}
