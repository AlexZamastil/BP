import { useTranslation } from 'react-i18next';
import { useNavigate, useParams } from "react-router-dom";
import {  Button } from '@mui/material';
import { callAPI } from './CallAPI';
import getXSRFtoken from './XSRF_token';

/**
 * Author: Alex Zamastil
 * File renders a component for deleting a training.
 */

export default function DeleteUser() {

     const { t } = useTranslation();
     const navigate = useNavigate();
     const xsrfToken = getXSRFtoken();

     const {trainingID} = useParams();
// Function to handle deletion of the user
     function handleDelete() {
            callAPI("DELETE","user/deleteUser",null,xsrfToken)
            .then((response)=>{
                console.log("User deleted")
                navigate("/")
            }
                
            )
            .catch()
     }
 // Rendering delete user button with a assurance text
     return (<div className="error404">
          
          {t("delete_user")}
          <br/>

         <Button style={{ margin: '10px', color: 'black' }} color="dark" variant="contained" onClick={() => navigate("/training")}>{t("no")}</Button>
        <Button style={{ margin: '10px' }} color="secondary" variant="contained" onClick={() => handleDelete()}>{t("delete_user_button")}</Button>
        
     </div>)
}