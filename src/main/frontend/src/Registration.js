import React, { useState } from 'react';
import { Container} from '@mui/system';
import {TextField,Button} from '@mui/material';

export default function Registration(){
    const [nickname, setNickname] = useState('');
    const [password, setPassword] = useState('');
    const [email, setEmail] = useState('');
    const [birthdate, setBirthdate] = useState('');
  

    const handleClick=(e)=>{
      e.preventDefault();
      
      const user = {
        method:"POST",
        headers:{
            'Authorization' : '',
            'Content-Type':'application/json'
          },
            body : JSON.stringify({
              email : email,
              nickname : nickname,
              password : password,
              birthdate: birthdate,
            })
        };
        console.log(user.body);
      fetch('http://localhost:8080/api/user/register',user)
      .then(async (response)=>{
            if (response.status === 200){
              console.log('REGISTERED')
              return await response.json();
            } else{
              throw await response.json()}
            })
            .catch((err) => {
              if(err.message == "Failed to fetch");
              console.log('error' + err.message);
            })
  }
    return (

        <Container>

<form noValidate autoComplete="off">

  <h1>Registration form</h1>

  
 <TextField style={{margin:"10px auto"}} id="outlined-basic" label="Nickname" variant="outlined" fullWidth 
  value={nickname}
  onChange={(e)=>setNickname(e.target.value)} />

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
