import { Link, useMatch, useResolvedPath } from "react-router-dom";
import { useTranslation } from 'react-i18next';
import logo_transparent from "./sources/logo_transparent.png";
import { Button } from "@mui/material";
import { ButtonGroup } from "@mui/material";
import i18next from "i18next";


export default function Navbar() {
  const { t } = useTranslation();

  function changeLanguage(lng) {
    i18next.changeLanguage(lng);
    localStorage.setItem("Localization", lng); 
  }

 return (
    <>
      <nav className="nav">
        <Link to="/welcomePage" className="logo">
          <img src={logo_transparent} width={200} alt="temp" />

        </Link>
        <ul>
          <CustomLink className="c-link" to="/aboutproject"> <p>{t('about-project')}</p> </CustomLink>

          {localStorage.getItem("token") == null ? (
            <><CustomLink className="loginnav" to="/login"> <p>{t('log-in')}</p> </CustomLink>
              <CustomLink className="registernav" to="/register"> <p>{t('register')}</p> </CustomLink>
            </>

          ) : (<>
            <CustomLink className="c-link" to="/training"> <p> {t("training")}</p> </CustomLink>
            <CustomLink className="c-link" to="/profile"> <p>{t("profile")}</p> </CustomLink>
          </>
          )
          }

        </ul>

        <ButtonGroup
          orientation="vertical"
          aria-label="Vertical button group"
          variant="outlined"
          size="small"
          color="black"
        >
          <Button onClick={() => changeLanguage("cs")}>CZE</Button>
          <Button onClick={() => changeLanguage("en")}>ENG</Button>
        </ButtonGroup>
      </nav>
      <hr className="hr-line" />
    </>
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
