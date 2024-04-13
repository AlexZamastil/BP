import { useTranslation } from 'react-i18next';
import { useNavigate, useParams } from "react-router-dom";
import { Paper, Button } from '@mui/material';
import { callAPI } from './CallAPI';
import getXSRFtoken from './XSRF_token';


/**
 * Author: Alex Zamastil
 * File renders a component for deleting a training.
 */

export default function DeleteTraining() {

     const { t } = useTranslation();
     const navigate = useNavigate();
     const xsrfToken = getXSRFtoken();

     const {trainingID} = useParams();
// Function to handle deletion of the training
     function handleDelete() {
            callAPI("DELETE","training/deleteTraining/"+trainingID,null,xsrfToken)
            .then((response)=>{
                console.log("training deleted")
            }
                
            )
            .catch()
     }
 // Rendering delete training button with a assurance text
     return (<div className="error404">
          
          {t("delete_training")}
          <br/>

         <Button style={{ margin: '10px', color: 'black' }} color="dark" variant="contained" onClick={() => navigate("/training")}>{t("no")}</Button>
        <Button style={{ margin: '10px' }} color="secondary" variant="contained" onClick={() => handleDelete()}>{t("delete_training_button")}</Button>
        
     </div>)
}