import { useTranslation } from 'react-i18next';
import { Link} from "react-router-dom"
import {useNavigate} from "react-router-dom"

export default function Profile(){
    const navigate = useNavigate();

    const {t} = useTranslation();

    function LogOut(){
      localStorage.clear("user");
    }
    const handleLogout = ()=> {
      localStorage.clear("user");
      navigate("/WelcomePage");
      window.location.reload(false);
    }
    return( <div>
        <Link to="/WelcomePage">
        <button on onClick={handleLogout}> {t('log-out')} </button>
        </Link>
      </div>)
}



 