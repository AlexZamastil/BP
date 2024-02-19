import { useEffect, useState } from "react"
import { useParams } from "react-router-dom";
import { callAPI } from './CallAPI';

export default function Exercise() {
    const { exerciseID } = useParams();
    const [exerciseData, setExerciseData] = useState([]);
    useEffect(() => {
        callAPI("GET", "exercise/getExercise/" + exerciseID, null, null)
            .then(async (response) => {
                setExerciseData(await response.text())
            }).then(console.log(exerciseData))
    }, [])
    return null
}
