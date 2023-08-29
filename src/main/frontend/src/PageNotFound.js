import styling from "./styling.css"
import error from "./sources/error-icon.png"

export default function PageNotFound() {
return (<div className="error404">
     <img src={error}  width={80} alt="ERROR"/> 
     Error 404 - Page not found
     </div>)
}