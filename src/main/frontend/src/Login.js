import React, { useState } from 'react';
import { Container} from '@mui/system';
import {TextField,Button} from '@mui/material';

export default function Login(){
    
  const handleClick =(e)=>{
    e.preventDefault();

   fetch("http://localhost:8080/api/nonauthorized/user/login",{
        method:"POST",
        headers:{
          'Authorization': 'No Auth',
            "Content-Type":"application/json"},
            body : JSON.stringify({
                email : email,
                password : password
            })
        }).then(async(response)=>{
          if (response.status === 200){
            console.log('Logged in')
            return await response;

          } else{
            throw await response;
          }  
        }).then((response) => {
          localStorage.setItem('login', true);
          window.dispatchEvent(new Event('storage'));
         localStorage.setItem('token', response.message);
        }).catch((error)=>{
            console.log("error " + error.message)
        })
            console.log(email, password)
  }

    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    return (
      <Container>

      <form noValidate autoComplete="off">
      
        <h1>Login</h1>
      
      <TextField style={{margin:"10px auto"}} id="outlined-basic" label="Email" variant="outlined" fullWidth 
        value={email}
        onChange={(e)=>setEmail(e.target.value)} />
      
        <TextField type="password" style={{margin:"10px auto"}} id="outlined-basic" label="Password" variant="outlined" fullWidth 
       value={password}
       onChange={(e)=>setPassword(e.target.value)} />
      
      
       <Button variant="contained" onClick={handleClick}> Submit </Button>
      </form>
      </Container>
    )
}


