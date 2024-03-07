import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom";
import { callAPI } from './CallAPI';

export default function Exercise() {
    const navigate = useNavigate()
    const { exerciseID } = useParams();
    const [exerciseData, setExerciseData] = useState([]);
    useEffect(() => {
        callAPI("GET", "exercise/getExercise/" + exerciseID, null, null)
            .then(async (response) => {
                setExerciseData(await response.text())
            }).then(console.log(exerciseData))
            .catch((error) => {
                if(error.response && error.response.data === "Token expired"){
                    navigate("/tokenExpired")
               }
            }
            )
    }, [])
    return null
}
