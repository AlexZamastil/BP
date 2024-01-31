import React, { useEffect, useState } from 'react';
import { Container} from '@mui/system';
import { Paper } from '@mui/material';
import {TextField,Button} from '@mui/material';
import {useNavigate} from "react-router-dom";
import { useTranslation } from 'react-i18next';

export default function Login(){

  const navigate = useNavigate();
  const [password, setPassword] = useState('');
  const [email, setEmail] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const [csrfToken,setCsrfToken] = useState("") 

  const {t} = useTranslation();

  useEffect(() => {
    const xsrfToken = getCookie('XSRF-TOKEN');
    setCsrfToken(xsrfToken);
    console.log(csrfToken);
  },[])

    
  const logInRequest =(e)=>{
    e.preventDefault();
  
   fetch("https://localhost:8443/api/nonauthorized/user/login",{
        method:"POST",
        headers:{
          'Authorization': 'No Auth',
           'X-XSRF-TOKEN': csrfToken ,
            "Content-Type":"application/json"},credentials : "include",
            
            body : JSON.stringify({
                email : email,
                password : password
            })
        }).then(async(response)=>{
          
          if (response.status === 200){
            console.log('Logged in successfully')
            const token = await response.text();
            localStorage.setItem('token', token);
            localStorage.setItem('user', email);
            console.log("redirect to profile");
          navigate("/profile");
          } else{const errorResponse = await response.text();
            setErrorMessage(errorResponse);
            console.error('Login failed:', errorResponse);
            console.log(csrfToken);
          }}).catch(error => {
          console.error('Error occurred during login:', error);
          setErrorMessage('An error occurred during login');
        })
   }

    
    return (
      <div className='loginBG'>
      <Container >
        <Paper elevation={3} className='paper'>
      <form noValidate autoComplete="off">
      
        <h1>{t("login_form")}</h1>
      
      <TextField  style={{ margin: "10px auto", opacity: 1 }} id="outlined-basic" label="Email" variant="outlined" fullWidth sx={{ m: 1, width: '25ch' }}
        value={email}
        onChange={(e)=>setEmail(e.target.value)} />
      
        <TextField   style={{ margin: "10px auto", opacity: 1 }} type="password"  id="outlined-basic" label={t("password")} variant="outlined" fullWidth sx={{ m: 1, width: '25ch' }}
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
    function getCookie(name) {
      const cookies = document.cookie.split(';');
      for (const cookie of cookies) {
          const [cookieName, cookieValue] = cookie.trim().split('=');
          if (cookieName === name) {
              return cookieValue;
          }
      }
      return null;
  }
}


