import React, { useState } from 'react';
import { Container} from '@mui/system';
import {TextField,Button} from '@mui/material';
import {useNavigate} from "react-router-dom"

export default function Login(){

  const navigate = useNavigate();
    
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
          if (response.status == 200){
            console.log('Logged in successfully')
            return await response;
          } else{
            throw await response;
          }  
        }).then(async(response) => {
         localStorage.setItem('token', response.message)
        return await response;
        }).then((response) => {
          if (response.status == 200){
            console.log("redirect");
           navigate("/profile");
           
          }
          else {console.log (response.status)}
        })
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


