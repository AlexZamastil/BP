import { Paper, Button } from '@mui/material';
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

    const handleChange = ()=> {
      navigate("/UpdateData");
      window.location.reload(false);
    }

    const handleAdminTools = ()=> {
      navigate("/AdminTools");
      window.location.reload(false);
    }


    const [adminTools,setAdminTools] = useState(null);
    
useEffect(()=>{
  fetch("https://localhost:8443/api/authorized/user/getuserdata",{
    method:"GET",
    headers:{
      'Authorization': localStorage.getItem("token")}
    }).then(async(response)=>response.json())
    .then((userStats)=> {
      setUserStats(userStats);
        if (userStats.user && userStats.user.adminPrivileges) {
          setAdminTools(
            <Button color="dark" variant="contained" onClick={handleAdminTools}>
              {t('admin_button')}
            </Button>
          );
        }
      })},[])

    return( <div>
  {userStats.user ? (
<Paper elevation={3} className='paperProfile'>
        <div>
          
          Nickname <b> {userStats.user.nickname}</b> <br/>
          Email <b>{userStats.user.email}</b><br/>
          Height <b>{userStats.user.height}</b><br/>
          Weight <b>{userStats.user.weight}</b><br/>
          Date of birth<b>{userStats.user.dateOfBirth}</b><br/>
          BodyType<b>{userStats.user.bodyType}</b><br/>
          Sex <b>{userStats.user.sex}</b>
          <br/>
          <Button color='primary' variant='contained' onClick={handleChange}> {t('change_data')} </Button>
        </div>
      </Paper>
       ) : null}
       <div className='Logout'>
        <Link to="/WelcomePage">
        <Button color='secondary' variant='contained' onClick={handleLogout}> {t('log-out')} </Button>
        </Link>
        <br/>
        <br/>
        {adminTools}
        </div>
        
      </div>)
}





 