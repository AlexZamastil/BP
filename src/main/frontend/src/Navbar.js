import { Link, useMatch, useResolvedPath } from "react-router-dom"
import { useTranslation } from 'react-i18next';
import logo_transparent from "./sources/logo_transparent.png"

export default function Navbar(props) {

  const {t} = useTranslation();



  return (
    
    
        <nav className="nav">
        <Link to="/welcomePage" className="logo">
        <img src={logo_transparent}  width={200} alt="temp" />
  
        </Link>
        <ul>
          <CustomLink className="c-link" to="/aboutproject"> <p>{t('about-project')}</p> </CustomLink>
  
          {localStorage.getItem("user") == null ? (
              <><CustomLink className="c-link" to="/login"> <p>{t('log-in')}</p> </CustomLink>
                <CustomLink className="c-link" to="/register"> <p>{t('register')}</p> </CustomLink>
              </>
              
        ) : (
          <CustomLink className="c-link" to="/profile"> <p>{props}</p> </CustomLink>
          
        )
        }
        </ul>
        
        
      </nav>
      )
}

function CustomLink({ to, children, ...props }) {
  const resolvedPath = useResolvedPath(to)
  const isActive = useMatch({ path: resolvedPath.pathname, end: true })

  return (
    <li className={isActive ? "active" : ""}>
      <Link to={to} {...props}>
        {children}
      </Link>
    </li>
  )
}


function ShowLogin(){
  const {t} = useTranslation();
  if (localStorage.getItem("user")== null){
  return(
    <ul>
      <CustomLink className="login-link" to="/login"> <p>{t('log-in')}</p> </CustomLink>
      <CustomLink className="register-link" to="/register"> <p>{t('register')}</p> </CustomLink>
      </ul>
      
  )
}} 