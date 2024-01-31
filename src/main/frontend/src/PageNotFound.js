import styling from "./styling.css";
import error from "./sources/error-icon.png";
import { useTranslation } from 'react-i18next';

export default function PageNotFound() {

     const {t} = useTranslation();

return (<div className="error404">
     <img src={error}  width={80} alt="ERROR"/> 
     {t("page_not_found")}
     </div>)
}