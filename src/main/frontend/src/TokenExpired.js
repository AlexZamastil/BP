import error from "./sources/error-icon.png";
import { useTranslation } from 'react-i18next';
import {useNavigate} from "react-router-dom";
import { Button } from "@mui/material";

export default function TokenExpired() {

    const navigate = useNavigate();
     const { t } = useTranslation();

     return (<div className="error404">
          <img src={error} width={80} alt="ERROR" />
          {t("token_expired")}
          <br/>
          <Button color='primary' variant='contained' onClick={returnToLogin}> {t('log-in')} </Button>
     </div>)

     function returnToLogin(){
          localStorage.removeItem("token")
        navigate("/login");
     }
}