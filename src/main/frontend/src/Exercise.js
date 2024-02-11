import { useEffect, useState } from "react"
import { useParams } from "react-router-dom";

export default function Exercise(){
    const { exerciseID } = useParams();
    const [exerciseData, setExerciseData] = useState([]);
    useEffect(()=> {
        fetch(process.env.REACT_APP_BACKEND_API_URL+"/unauthorized/getExercise/"+exerciseID, {
            method: "GET"
        }).then(async(response)=> {
            setExerciseData(await response.text())
        }).then(console.log(exerciseData))
    },[])
    return null
}
