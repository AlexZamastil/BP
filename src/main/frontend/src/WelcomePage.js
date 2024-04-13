
import { useTranslation } from 'react-i18next';
import {Link} from "react-router-dom"

/**
 * Author: Alex Zamastil
 * The welcome page of the project. Refers to login and registration page. 
 */

export default function WelcomePage() {

    const {t} = useTranslation();

    return(
      <>
     
        <div className='welcomeDiv1'> <h1> {t("welcomepage_text1")}</h1>
        <p> {t("welcomepage_text3")}</p>
        </div>
<div className='flexDiv'>
    <div className='welcomeDiv2'>
        <h1 className='h11'> {t("welcomepage_inspiration2")}</h1>
        <h1 className='h22'> {t("welcomepage_inspiration")}</h1> 
        <h1 className='h33'> {t("welcomepage_inspiration3")}</h1>
        <h1 className='h44'> {t("welcomepage_inspiration4")}</h1>
    </div>
    <div className='flex2'>
    <Link to="/Login" class="animatedLink2">{t("log-in")}</Link>
    <Link to="/Register" class="animatedLink">{t("register_now")}</Link>
    </div>
</div>
        </>
    )
}

