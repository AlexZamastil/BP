import { useTranslation } from 'react-i18next';

export default function WelcomePage() {

    const {t} = useTranslation();

    return(
    <ShowLogin/>
    )

}


  function LogIN(){
    localStorage.setItem("user", "TestUser0231")
    console.log(localStorage.getItem("user"))
    window.location.reload(false)
}


  function ShowLogin(){
    const {t} = useTranslation();
    if (localStorage.getItem("user")== null){
    return(
      <div>
        <button on onClick={LogIN}> {t('log-in')} </button>
          
          <p>{t('welcome-page')}</p>
          
      </div>
    
    )
  }} 
