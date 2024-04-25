import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom";
import { callAPI } from './CallAPI';
import { Paper } from "@mui/material";
import { useTranslation } from "react-i18next";
import CircularProgress from "@mui/material/CircularProgress";
/**
 * Author: Alex Zamastil
 * File displays information about specific exercise, based on ID from URL parameter.
 */

export default function Exercise() {
    const navigate = useNavigate()
    const { exerciseID } = useParams();
    const [exerciseData, setExerciseData] = useState([]);
    const { t } = useTranslation();
    const [picture, setPicture] = useState(undefined)
    const [isPictureLoading, setIsPictureLoading] = useState(false)

    //getting exercise information from server
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
    //getting the exercise picture from server 
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
    //rendering exercise data
    function renderExercise(localization) {

        if (localization === "cs") {
            if (exerciseData.type === "RUN") {
                return (

                    <div className="exerciseFoodDisplay">
                        {t("name_e")}{exerciseData.name}<br />
                        {t("description_e")} {exerciseData.description}<br />
                        {t("type_e")}{exerciseData.type}<br />
                        {t("category_e")} {exerciseData.category}<br />
                        {t("length_e")}{exerciseData.length}<br />
                        {exerciseData.tagsJSON}
                    </div>

                )
            } else if (exerciseData.type === "GYM") {
                return (
                    <div className="exerciseFoodDisplay">
                        {t("name_e")}{exerciseData.name}<br />
                        {t("description_e")}  {exerciseData.description}<br />
                        {t("type_e")} {exerciseData.type}<br />
                        {t("repetitions_e")} {exerciseData.repetitions}<br />
                        {t("series_e")} {exerciseData.series}<br />
                        {exerciseData.tagsJSON}
                    </div>
                )
            } else {
                return (
                    <div className="exerciseFoodDisplay">
                        {t("name_e")}{exerciseData.name}<br />
                        {t("description_e")}  {exerciseData.description}<br />
                        {t("type_e")} {exerciseData.type}<br />
                        {t("style_e")}{exerciseData.style}<br />
                        {t("length_e")} {exerciseData.length}<br />
                        {exerciseData.tagsJSON}

                    </div>
                )
            }


        } else {
            if (exerciseData.type === "RUN") {
                return (
                    <div className="exerciseFoodDisplay">
                        {t("name_e")}{exerciseData.name_eng}<br />
                        {t("description_e")}  {exerciseData.description_eng}<br />
                        {t("type_e")}{exerciseData.type}<br />
                        {t("category_e")} {exerciseData.category}<br />
                        {t("length_e")} {exerciseData.length}<br />
                        {exerciseData.tagsJSON}
                    </div>
                )
            } else if (exerciseData.type === "GYM") {
                return (
                    <div className="exerciseFoodDisplay">
                        {t("name_e")}{exerciseData.name_eng}<br />
                        {t("description_e")}  {exerciseData.description_eng}<br />
                        {t("type_e")}{exerciseData.type}<br />
                        {t("repetitions_e")}{exerciseData.repetitions}<br />
                        {t("series_e")} {exerciseData.series}<br />
                        {exerciseData.tagsJSON}
                    </div>
                )
            } else {
                return (
                    <div className="exerciseFoodDisplay">
                        {t("name_e")}{exerciseData.name_eng}<br />
                        {t("description_e")} {exerciseData.description_eng}<br />
                        {t("type_e")} {exerciseData.type}<br />
                        {t("style_e")} {exerciseData.style}<br />
                        {t("length_e")} {exerciseData.length}<br />
                        {exerciseData.tagsJSON}

                    </div>
                )
            }
        }
    }
    //file returns an exercise
    return (
        <>
            <div className="exerciseFlex">


                <Paper elevation={3} className='paperExercise'>
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
                            style={{
                                borderRadius: '30px',
                                maxWidth: '100%',
                                height: 'auto',
                                objectFit: 'cover',
                                margin: "20px"
                            }}
                        />
                    }
                </div>
            </div>
        </>
    );
}
