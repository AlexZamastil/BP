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

export default function Food() {
    const navigate = useNavigate()
    const { foodID } = useParams();
    const [foodData, setExerciseData] = useState([]);
    const { t } = useTranslation();
    const [picture, setPicture] = useState(undefined)
    const [isPictureLoading, setIsPictureLoading] = useState(false)

    //getting food information from server
    useEffect(() => {
        callAPI("GET", "food/getFood/" + foodID, null, null)
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
    //getting the food picture from server 
    useEffect(() => {
        setIsPictureLoading(true)
        callAPI("GET", "food/getFood/picture/" + foodID, null, null)
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
    //rendering food data
    function renderFood(localization) {

        if (localization === "cs") {
            return (
                <div className="exerciseFoodDisplay">
                    {t("name_e")}{foodData.name}<br />
                    {t("description_e")} {foodData.description}<br />
                    {t("calories_gain_f")}{foodData.calories}<br />
                    {foodData.tagsJSON}
                </div>
            )



        } else {

            return (
                <div className="exerciseFoodDisplay">
                    {t("name_e")}{foodData.name_eng}<br />
                    {t("description_e")}  {foodData.description_eng}<br />
                    {t("calories_gain_f")}{foodData.calories}<br />

                    {foodData.tagsJSON}
                    </div>
            )

        }
    }

    //file returns the food detail
    return (
        <>
            <div className="exerciseFlex">


                <Paper elevation={3} style={{ maxWidth: '100%' }} className='paperExercise'>
                    <div>
                        {foodData && (

                            renderFood(localStorage.getItem("Localization"))

                        )}
                    </div>
                </Paper>
                <div style={{ height: "250px", marginTop: "20px", display: "flex" }}>
                    {isPictureLoading ?
                        <CircularProgress />
                        :
                        <img src={`data:image/png;base64,${picture}`}
                            style={{
                                borderRadius: '10px',
                                maxWidth: '100%',
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
