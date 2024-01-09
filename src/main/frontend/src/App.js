import Navbar from "./Navbar"
import AboutProject from "./AboutProject"
import Profile from "./Profile"
import { Route, Routes } from "react-router-dom"
import WelcomePage from "./WelcomePage"
import { useTranslation } from 'react-i18next';
import Login from "./Login"
import Registration from "./Registration"
import PageNotFound from "./PageNotFound";
import UpdateData from "./UpdateData";
import AddExercise from "./AddExercise";
import Training from "./Training";
import GenerateTraining from "./GenerateTraining";
import Exercise from "./Exercise"

function App() {

  const {t} = useTranslation();

  return (
    <>
    
      {Navbar(localStorage.getItem("user"))}
      <div className="container">
        <Routes>
          <Route path="/aboutproject" element={<AboutProject/>} />
          <Route path="/profile" element={<Profile/>} />
          <Route path="/welcomePage" element={<WelcomePage/>} />
          <Route path="/login" element={<Login/>} />
          <Route path="/register" element={<Registration/>} />
          <Route path="/UpdateData" element={<UpdateData/>}/>
          <Route path="/addExercise" element={<AddExercise/>}/>
          <Route path="/training" element={<Training/>}/>
          <Route path="/generateTraining/:trainingType" element={<GenerateTraining />} />
          <Route path="/exercise/:exerciseID" element={<Exercise/>}/>
          <Route path="*" element={<PageNotFound/>}/>
          <Route path="/" element={<WelcomePage/>}/>

        </Routes>
        
      </div>
      
      <footer>
      
      {t("footertext1")}
      </footer>
      
    </>
  )
}

export default App