import { useTranslation } from 'react-i18next';
import WelcomePage from './WelcomePage';
import { Link} from "react-router-dom"

export default function Profile(){
    const {t} = useTranslation();
    return( <div>
        <Link to="/WelcomePage">
        <button on onClick={LogOut}> {t('log-out')} </button>
    </Link>
        
        
          
      </div>)
}

function LogOut(){

   
    localStorage.clear("user");
    console.log(localStorage.getItem("user"));
    
  }

 