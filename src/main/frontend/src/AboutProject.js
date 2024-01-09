
import { useTranslation } from 'react-i18next';

export default function AboutProject(){

    const {t} = useTranslation();

    return(<div>
        <h1 >{t("about_project")}</h1>
        
    </div>)
    
}