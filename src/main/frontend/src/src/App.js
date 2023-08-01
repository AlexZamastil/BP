import Navbar from "./Navbar"
import AboutProject from "./AboutProject"
import Profile from "./Profile"
import { Route, Routes } from "react-router-dom"

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
    </>
  )
}

export default App