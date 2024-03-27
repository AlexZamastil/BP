import React from 'react';
import { useEffect, useState } from 'react';
import { Route, Routes } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import Navbar from './Navbar';
import AboutProject from './AboutProject';
import Profile from './Profile';
import WelcomePage from './WelcomePage';
import Login from './Login';
import Registration from './Registration';
import PageNotFound from './PageNotFound';
import UpdateData from './UpdateData';
import AddExercise from './AddExercise';
import Training from './Training';
import GenerateTraining from './GenerateTraining';
import Exercise from './Exercise';
import AdminTools from './AdminTools';
import TokenExpired from './TokenExpired';
import ChangePassword from './ChangePassword';
import i18next from 'i18next';
import { callAPI } from './CallAPI';
import getXSRFtoken from './XSRF_token';

function App() {
  const { t } = useTranslation();
  const [year] = useState(new Date().getFullYear());
  const locale = localStorage.getItem("Localization");
  const xsrfToken = getXSRFtoken();
  const [loggedIn] = useState(1) 

  useEffect(() => {
    callAPI("POST","initConnection",null,xsrfToken)
    .then(response => {
      console.log(response.data);
    })
    .catch(error => {
      console.error('Failed to establish communication with the backend.');
        console.error('Error initiating communication:', error);
        loggedIn +=1;
    });
}, [loggedIn]);

  useEffect(() => {
    i18next.changeLanguage(locale)
    localStorage.setItem("Localization", locale)

    if(locale === null) {
      console.log("Changing language to czech");
      i18next.changeLanguage("cs")
      localStorage.setItem("Localization", "cs")
     
    }
  }, [locale])
  
  return (
    <>
      <Navbar/>
      <div className='container'>
        <Routes>
          <Route path="/aboutproject" element={<AboutProject />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/welcomePage" element={<WelcomePage />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Registration />} />
          <Route path="/UpdateData" element={<UpdateData />} />
          <Route path="/addExercise" element={<AddExercise />} />
          <Route path="/training" element={<Training />} />
          <Route path="/generateTraining/:trainingType" element={<GenerateTraining />} />
          <Route path="/exercise/:exerciseID" element={<Exercise />} />
          <Route path="/adminTools" element={<AdminTools />} />
          <Route path="/tokenExpired" element={<TokenExpired/>} />
          <Route path="/changePassword" element={<ChangePassword/>} />
          <Route path="*" element={<PageNotFound />} />
          <Route path="/" element={<WelcomePage />} />
        </Routes>
      </div>
      <footer> {t('footertext1')} <br/><br/> © {year} - <a href='https://www.uhk.cz/cs/fakulta-informatiky-a-managementu/fim' target="_blank" rel="noopener noreferrer">UHK FIM</a> <br/>{t('copyright2')}</footer>
    </>
  );
}

export default App;