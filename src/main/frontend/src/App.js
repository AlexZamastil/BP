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
import AddFood from './AddFood';
import Food from './Food';
import Training from './Training';
import GenerateTraining from './GenerateTraining';
import Exercise from './Exercise';
import AdminTools from './AdminTools';
import TokenExpired from './TokenExpired';
import ChangePassword from './ChangePassword';
import DeleteTraining from './DeleteTraining';
import DeleteUser from './DeleteUser';
import i18next from 'i18next';
import { callAPI } from './CallAPI';
import getXSRFtoken from './XSRF_token';

/**
 * Author: Alex Zamastil
 * Main component of the web app.
 */

function App() {
  const { t } = useTranslation();
  const [year] = useState(new Date().getFullYear());
  const locale = localStorage.getItem("Localization");
  const xsrfToken = getXSRFtoken();
  const [loggedIn, setLoggedIn] = useState(1)
// Effect hook for initializing connection with the backend
  useEffect(() => {
    callAPI("POST", "initConnection", null, xsrfToken)
      .then(response => {
        console.log(response.data);
      })
      .catch(error => {
        console.error('Failed to establish communication with the backend.');
        console.error('Error initiating communication:', error);
        setLoggedIn(loggedIn + 1);
      });
  }, [loggedIn]);
// Effect hook for setting document title
  useEffect(() => {
    document.title = 'Runora';
  }, []);
// Effect hook for handling localization
  useEffect(() => {
    i18next.changeLanguage(locale)
    localStorage.setItem("Localization", locale)

    if (locale === null) {
      console.log("Changing language to czech");
      i18next.changeLanguage("cs")
      localStorage.setItem("Localization", "cs")

    }
  }, [locale])
  // Whole navigation is based on this return element. Based on library React Router
  return (
    <>
      <Navbar />
      <div className='container'>
        <Routes>
          <Route path="/aboutproject" element={<AboutProject />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/welcomePage" element={<WelcomePage />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Registration />} />
          <Route path="/UpdateData" element={<UpdateData />} />
          <Route path="/addExercise" element={<AddExercise />} />
          <Route path="/addFood" element={<AddFood />} />
          <Route path="/Food/:foodID" element={<Food />} />
          <Route path="/training" element={<Training />} />
          <Route path="/generateTraining/:trainingType" element={<GenerateTraining />} />
          <Route path="/exercise/:exerciseID" element={<Exercise />} />
          <Route path="/adminTools" element={<AdminTools />} />
          <Route path="/tokenExpired" element={<TokenExpired />} />
          <Route path="/changePassword" element={<ChangePassword />} />
          <Route path="/deleteTraining/:trainingID" element={<DeleteTraining />} />
          <Route path="/deleteUser" element={<DeleteUser />} />
          <Route path="*" element={<PageNotFound />} />
          <Route path="/" element={<WelcomePage />} />
        </Routes>
      </div>
      <footer> © {year} - <a href='https://www.uhk.cz/cs/fakulta-informatiky-a-managementu/fim' target="_blank" rel="noopener noreferrer">UHK FIM</a></footer>
    </>
  );
}

export default App;