import React, { useState } from 'react';
import { Container} from '@mui/system';
import { Paper } from '@mui/material';
import {TextField,Button} from '@mui/material';
import {useNavigate} from "react-router-dom";
import { useTranslation } from 'react-i18next';
import {callAPINoAuth} from './CallAPI';
import getXSRFtoken from './XSRF_token';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';

export default function Login(){

  const navigate = useNavigate();
  const [password, setPassword] = useState(null);
  const [email, setEmail] = useState(null);
  const [errorMessage, setErrorMessage] = useState('');

  const {t} = useTranslation();
  const theme = useTheme();
  const isSmallScreen = useMediaQuery(theme.breakpoints.down('sm'));

 const xsrfToken = getXSRFtoken();

  const logInRequest =(e)=>{
    e.preventDefault();

    const data = JSON.stringify({
      email : email,
      password : password
  })

    callAPINoAuth("POST","user/login",data,xsrfToken)
    .then(response => {
          console.log('Logged in successfully');
          const token = response.data;
          localStorage.setItem('token', token);
          navigate("/profile");
          window.location.reload(false);

  })
  .catch(error => {
      let errorMessage = 'An error occurred during login';
      if (error.response) {
          errorMessage = error.response.data || errorMessage;
          console.error('Login failed:', errorMessage);
      } else if (error.request) {
          console.error('No response received:', error.request);
      } else {
          console.error('Error occurred during login:', error.message);
      }
      setErrorMessage(errorMessage);
      console.error("Login failed", error);
  });
    
   }

    
    return (
      <div className='loginBG'>
      <Container >
        <Paper elevation={3} className='paper'>
        <form noValidate autoComplete="off" className='loginForm' >
          
      
        <h1>{t("login_form")}</h1>
      
      <TextField  style={{ margin: "10px auto", opacity: 1 }} id="outlined-basic" label="Email" variant="outlined"  sx={{ m: 1, width: isSmallScreen ? '20ch' : '25ch' }}
        value={email}
        onChange={(e)=>setEmail(e.target.value)} />
      
        <TextField   style={{ margin: "10px auto", opacity: 1 }} type="password"  id="outlined-basic" label={t("password")} variant="outlined"  sx={{ m: 1, width: isSmallScreen ? '20ch' : '25ch' }}
       value={password}
       onChange={(e)=>setPassword(e.target.value)} />
      
      
       <Button variant="contained" onClick={logInRequest}> {t("submit")} </Button>
       {errorMessage && (
              <div style={{ color: 'red', marginTop: '10px' }}>
                {errorMessage}
              </div>
            )}
      </form>
      </Paper>
      </Container>
      </div>
     
    )
}


