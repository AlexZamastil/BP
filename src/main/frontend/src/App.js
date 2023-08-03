import Navbar from "./Navbar"
import AboutProject from "./AboutProject"
import Profile from "./Profile"
import { Route, Routes } from "react-router-dom"
import { useEffect, useState} from "react";
import WelcomePage from "./WelcomePage"

function App() {


  return (
    <>
      <Navbar />
      <div className="container">
        <Routes>
          <Route path="/aboutproject" element={<AboutProject/>} />
          <Route path="/profile" element={<Profile/>} />
        </Routes>
        
      </div>
      
      <RenderContent/>
      
    </>
  )
}

function RenderContent() {

    if (localStorage.getItem("user")== null){
      return(
        <div>
            <p>welcomepage</p>
            <WelcomePage/>
        </div>
      
      )
    } else {
      return(
        <div>
            <p>logged in</p>
            <button on onClick={LogOut}> log out </button>
        </div>
      
      )
      
    }
}
function LogOut(){
  localStorage.clear("user")
  console.log(localStorage.getItem("user"))
  window.location.reload(false)
}



export default App