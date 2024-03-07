import React from 'react';
import { useEffect } from 'react';
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
import i18next from 'i18next';

function App() {
  const { t } = useTranslation();

  const locale = localStorage.getItem("Localization");


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
      {Navbar()}
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
          <Route path="*" element={<PageNotFound />} />
          <Route path="/" element={<WelcomePage />} />
          <Route path="/tokenExpired" element={<TokenExpired/>} />
        </Routes>
      </div>
      <footer> {t('footertext1')} <br/><br/> {t('copyright')}<a href='https://www.uhk.cz/cs/fakulta-informatiky-a-managementu/fim' target="_blank" rel="noopener noreferrer">UHK FIM</a> <br/>{t('copyright2')}</footer>
    </>
  );
}

export default App;