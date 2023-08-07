import { Link, useMatch, useResolvedPath } from "react-router-dom"
import { useTranslation } from 'react-i18next';
import styling from "./styling.css"
import tempImage from "./sources/temp.png"

export default function Navbar() {

  const {t} = useTranslation();

  return (
    <nav className="nav">
      <Link to="/welcomePage" className="site-title">
      <img src={tempImage} width={50} alt="temp" />

      </Link>
      <ul>
        <CustomLink className="c-link" to="/profile"> <p>{t('profile')}</p> </CustomLink>
        <CustomLink className="c-link" to="/aboutproject"> <p>{t('about-project')}</p> </CustomLink>
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