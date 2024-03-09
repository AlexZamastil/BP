import { useEffect, useState, lazy } from "react"
import { useNavigate, useParams } from "react-router-dom";
import { callAPI } from './CallAPI';
import { Paper } from "@mui/material";
import { useTranslation } from "react-i18next";

export default function Exercise() {
    const navigate = useNavigate()
    const { exerciseID } = useParams();
    const [exerciseData, setExerciseData] = useState([]);

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
    console.log(exerciseData)
    return (
        <>
        <div className="exerciseFlex">
        <img src={`data:image/png;base64,${exerciseData.pictureData}`} alt="Exercise Picture"  style={{ height: "150px", marginTop : "20px", display: "flex"}}/>
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
