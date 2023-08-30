import { Paper } from '@mui/material';
import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link} from "react-router-dom"
import {useNavigate} from "react-router-dom"

export default function Profile(){
  const [userStats,setUserStats] = useState([]);
    const navigate = useNavigate();
    const {t} = useTranslation();
    const handleLogout = ()=> {
      localStorage.clear("user");
      navigate("/WelcomePage");
      window.location.reload(false);
    }
useEffect(()=>{
  fetch("http://localhost:8080/api/authorized/user/getuserdata",{
    method:"GET",
    headers:{
      'Authorization': localStorage.getItem("token")}
    }).then(async(response)=>response.json())
    .then((userStats)=> setUserStats(userStats),
    console.log("userstats"),
    console.log(userStats));
},[])
   


    return( <div>
  {userStats.user ? (
<Paper elevation={3} className='paperProfile'>
        <div>
          Nickname <b> {userStats.user.nickname}</b> <br/>
          Email <b>{userStats.user.email}</b><br/>
          Height <b>{userStats.user.height}</b><br/>
          Weight <b>{userStats.user.weight}</b><br/>
          Date of birth<b>{userStats.user.dateOfBirth}</b><br/>
          BodyType<b>{userStats.userstats.bodytype}</b>
        </div>
      </Paper>
       ) : null}
        <Link to="/WelcomePage">
        <button on onClick={handleLogout}> {t('log-out')} </button>
        </Link>
      </div>)
}



 