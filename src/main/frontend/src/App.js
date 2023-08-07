import Navbar from "./Navbar"
import AboutProject from "./AboutProject"
import Profile from "./Profile"
import { Route, Routes } from "react-router-dom"
import { useEffect, useState} from "react";
import WelcomePage from "./WelcomePage"
import { useTranslation } from 'react-i18next';


function App() {

  const {t} = useTranslation();

  return (
    <>
      <Navbar />
      <div className="container">
        <Routes>
          <Route path="/aboutproject" element={<AboutProject/>} />
          <Route path="/profile" element={<Profile/>} />
          <Route path="/welcomePage" element={<WelcomePage/>} />
        </Routes>
        
      </div>
      
      
    </>
  )
}

export default App