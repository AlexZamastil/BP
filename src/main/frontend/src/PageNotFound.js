import styling from "./styling.css";
import error from "./sources/error-icon.png";
import { useTranslation } from 'react-i18next';

/**
 * Author: Alex Zamastil
 * File renders an error 404 - not found. This page is referenced from any URL that is not defined.
 */

export default function PageNotFound() {

     const { t } = useTranslation();

     return (<div className="error404">
          <img src={error} width={80} alt="ERROR" />
          {t("page_not_found")}
     </div>)
}