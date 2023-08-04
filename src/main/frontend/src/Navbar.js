import { Link, useMatch, useResolvedPath } from "react-router-dom"
import { useTranslation } from 'react-i18next';

export default function Navbar() {

  const {t} = useTranslation();

  return (
    <nav className="nav">
      <Link to="/" className="site-title">
        Site Name
      </Link>
      <ul>
        <CustomLink to="/profile">{t('profile')}</CustomLink>
        <CustomLink to="/aboutproject">{t('about-project')}</CustomLink>
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