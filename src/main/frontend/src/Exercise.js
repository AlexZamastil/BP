import { useEffect, useState } from "react"
import { useParams } from "react-router-dom";

export default function Exercise(){
    const { exerciseID } = useParams();
    const [exerciseData, setExerciseData] = useState([]);
    useEffect(()=> {
        fetch("http://localhost:8080/api/nonauthorized/getExercise/"+exerciseID, {
            method: "GET"
        }).then(async(response)=> {
            setExerciseData(await response.text())
        }).then(console.log(exerciseData))
    },[])
    return null
}
