import { useTranslation } from 'react-i18next';

export default function WelcomePage() {

    const {t} = useTranslation();

    return(<button onClick = {LogIN}> {t('log-in')} </button>)

}
function LogIN(){
    localStorage.setItem("user", "honza")
    console.log(localStorage.getItem("user"))
    window.location.reload(false)
}
