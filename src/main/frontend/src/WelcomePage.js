import { useTranslation } from 'react-i18next';

export default function WelcomePage() {

    const {t} = useTranslation();

    return(
      <>
     
        <div className='welcomeDiv1'> <h1> {t("welcomepage_text1")}</h1>
        <p> {t("welcomepage_text3")}</p>
        </div>
        
        <div className='welcomeDiv2'>
           <h1 className='h11'> {t("welcomepage_inspiration2")}</h1>
           <h1 className='h22'> {t("welcomepage_inspiration")}</h1> 
           <h1 className='h33'> {t("welcomepage_inspiration3")}</h1>
           <h1 className='h44'> {t("welcomepage_inspiration4")}</h1>
           </div>
        </>
    )
}

