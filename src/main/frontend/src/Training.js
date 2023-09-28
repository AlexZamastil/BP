import { useEffect, useState } from "react";

export default function Training() {
    const [userStats, setUserStats] = useState([]);
    const [boolean, setBoolean] = useState(null);

    useEffect(() => {
        fetch("http://localhost:8080/api/authorized/user/getuserdata", {
            method: "GET",
            headers: {
                'Authorization': localStorage.getItem("token")
            }
        })
        .then(async (response) => {
            const userStatsData = await response.json();
            setUserStats(userStatsData);
            console.log("userstats", userStatsData);

            
            return fetch(`http://localhost:8080/api/authorized/hasActiveTraining/${userStatsData.user.id}`, {
                method: "GET",
                headers: {
                    'Authorization': localStorage.getItem("token")
                }
            });
        })
        .then(async (response) => {
            const booleanData = await response.json();
            setBoolean(booleanData);
            console.log(booleanData);
        });
    }, []);


    return (<div>
        
        {boolean == true ? (
              <>
              User has active training // TEMP
              </>
              
        ) : (<>
          User doesnt have active training //TEMP
          </>
        )
        }
    </div>)
}