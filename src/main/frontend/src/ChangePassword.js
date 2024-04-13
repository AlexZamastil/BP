import { useState } from "react";
import { callAPI } from "./CallAPI";
import getXSRFtoken from "./XSRF_token";
import { Container } from '@mui/system';
import { Paper } from '@mui/material';
import { TextField, Button } from '@mui/material';
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';

/**
 * Author: Alex Zamastil
 * Page containing a form for password reset. The form demands old and new password.
 */

export default function ChangePassword() {
  const [oldPassword, setOldPassword] = useState(null);
  const [newPassword, setNewPassword] = useState(null);
  const [errorMessage, setErrorMessage] = useState('')
  const [successMessage, setSuccessMessage] = useState('')
  const xsrfToken = getXSRFtoken();
  const theme = useTheme();
  const isSmallScreen = useMediaQuery(theme.breakpoints.down('sm'));
  const { t } = useTranslation();
  const navigate = useNavigate();

  //handler for sending a password reset request to server 
  const handlePasswordChange = (e) => {
    e.preventDefault();

    const data = JSON.stringify({
      oldPassword: oldPassword,
      newPassword: newPassword
    })

    callAPI("POST", "user/passwordReset", data, xsrfToken)
      .then((response) => {
        setErrorMessage(null)
        setSuccessMessage(response.data);
      })
      .catch(error => {
        setSuccessMessage(null)
        if (error.response && error.response.data === "Token expired") {
          navigate("/tokenExpired")
        }
        let errorMessage = 'An error occurred';
        if (error.response) {
          errorMessage = error.response.data || errorMessage;
        } else if (error.request) {
          console.error('No response received:', error.request);
        } else {
          console.error('Error occurred:', error.message);
        }
        setErrorMessage(errorMessage);
      });

      setOldPassword("");
      setNewPassword("");

  }
  //file returns a form
  return (
    <div>
      <Container style={{padding : "20px"}}>
        <Paper elevation={3} className='paper'>
          <form noValidate autoComplete="off">

            <h1>{t("change_password")}</h1>

            <TextField style={{ margin: "10px auto", opacity: 1 }}
             type="password"
              id="outlined-basic"
              label={t("old_password")}
              variant="outlined"
              fullWidth sx={{ m: 1, width: isSmallScreen ? '20ch' : '25ch' }}
              value={oldPassword}
              onChange={(e) => setOldPassword(e.target.value)} />

            <TextField style={{ margin: "10px auto", opacity: 1 }}
             type="password"
              id="outlined-basic"
              label={t("new_password")}
              variant="outlined"
              fullWidth sx={{ m: 1, width: isSmallScreen ? '20ch' : '25ch' }}
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)} />


            <Button variant="contained" onClick={handlePasswordChange}> {t("submit")} </Button>
            {errorMessage && (
              <div style={{ color: 'red', marginTop: '10px' }}>
                {errorMessage}
              </div>
            )}
            {successMessage && (
              <div style={{ color: 'green', marginTop: '10px' }}>
                {successMessage}
              </div>
            )}
          </form>
        </Paper>
      </Container>
    </div>

  )
}