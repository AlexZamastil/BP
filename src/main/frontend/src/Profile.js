import { Paper, Button } from '@mui/material';
import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link } from "react-router-dom"
import { useNavigate } from "react-router-dom"
import { callAPI } from './CallAPI';

export default function Profile() {
  const [userStats, setUserStats] = useState([]);
  const [bmiColor, setBmiColor] = useState(null);
  const [trainings, setTrainings] = useState([]);
  const [age,setAge] = useState(null);
  const navigate = useNavigate();
  const { t } = useTranslation();

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/WelcomePage");
    window.location.reload(false);
  }

  const handleChange = () => {
    navigate("/UpdateData");
    window.location.reload(false);
  }

  const handleAdminTools = () => {
    navigate("/AdminTools");
    window.location.reload(false);
  }

  const handleAge = (date) => {
    const today = new Date();
    const day = new Date(date);
    const difference = (today - day);
    const ageInYears = Math.floor(difference/1000/60/60/24/365.25);
    setAge(ageInYears);
  }

  const [adminTools, setAdminTools] = useState(null);

  useEffect(() => {
    callAPI("GET", "user/getUserData", null, null)
      .then((response => response.data))
      .then((userStats) => {
        setUserStats(userStats);
        handleAge(userStats.user.dateOfBirth)
        if (userStats.user && userStats.user.role === "ADMIN") {
          setAdminTools(
            <Button color="dark" style={{ margin: '10px' }} variant="contained" onClick={handleAdminTools}>
              {t('admin_button')}
            </Button>
          );
        }
        if (userStats.bmi) {
          const bmiValue = parseFloat(userStats.bmi);
          if (bmiValue >= 30) {
            setBmiColor('#FF3333');
          } else if (bmiValue >= 25 && bmiValue < 30) {
            setBmiColor('#FFFF66');
          } else if (bmiValue > 18.5 && bmiValue < 25) {
            setBmiColor('#66FF66');
          } else if (bmiValue <= 18.5 ) {
            setBmiColor('#99CCFF');
          }}
      })
      .catch((error) => {
        console.log(error)
        console.log(error.response)
        // console.log(error.response.data)
        if (error.response && error.response.data === "Token expired") {
          navigate("/tokenExpired")
        }
      });

      callAPI("GET", "training/getTrainings", null, null)
      .then(response => {
        setTrainings(response)
      })
      .catch((error) => {
        console.log(error)
        console.log(error.response)
        console.log(error.response.data)
        if (error.response && error.response.data === "Token expired") {
          navigate("/tokenExpired")
        }
      });
  }, [localStorage.getItem("Localization")])

  return (
    <>
      <div className='profile_bg'>
        <div className='profile'>
          <div className='profile_info'>
            {userStats.user ? (
              <Paper elevation={3} className='paperProfile'>
                <div>
                  <h2 className='header'>{t('personal_info')}</h2>

                  {t('username')} <b> {userStats.user.nickname}</b> <br />
                  {t('email')}<b>{userStats.user.email}</b><br />
                  {t('age')}<b>{age}</b><br />
                  {t('sex')}<b> {t(userStats.user.sex)}</b><br />
                  {t('height')}<b>{userStats.user.height}</b><br />
                  {t('weight')}<b>{userStats.user.weight}</b><br />
                  {t('body_type')}<b>{t(userStats.user.bodyType)}</b><br />
                  <br />
                  <Button color='primary' variant='contained' onClick={handleChange}> {t('change_data')} </Button>
                </div>
              </Paper>
            ) : null}


          </div>

          <div className='profile_stats'> <Paper elevation={3} className='paperProfile'>
            <div>
              <h2 className='header'>{t('personal_stats')}</h2>
              <div style={{ margin: "5px", padding: "5px" }}>
                {t('water_intake')} <b style={{ padding: "10px" }}><br />
                  {userStats.waterintake} L </b> <br />
              </div>

              <div style={{ margin: "5px", padding: "5px", borderRadius: "10px", backgroundColor: bmiColor }}>
                {t('bmi')} <br />
                {userStats.bmi} <br />
              </div>
              <br />
            </div>
          </Paper></div>
        </div>


        <div className='Logout'>
          <Link to="/WelcomePage">
            <Button color='secondary' style={{ margin: '10px' }} variant='contained' onClick={handleLogout}> {t('log-out')} </Button>
          </Link>

          {adminTools}
        </div>
      </div>
    </>
  )

}





