import React, { useState } from 'react';
import { Container} from '@mui/system';
import {TextField,Button} from '@mui/material';
import { DatePicker } from '@mui/x-date-pickers';
import {useNavigate} from "react-router-dom"




export default function Registration() {
  const navigate = useNavigate();
  const today = new Date();
  const [nickname, setNickname] = useState('');
  const [password, setPassword] = useState('');
  const [email, setEmail] = useState('');
  const [birthdate, setBirthdate] = useState(null);
  const date = useState("");
  const formattedBirthdate = birthdate ? birthdate.toISOString().split('T')[0] : '';
  

  const handleClick = (e) => {
    e.preventDefault();

    const user = {
      method: 'POST',
      headers: {
        'Authorization': '',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        email: email,
        nickname: nickname,
        password: password,
        dateOfBirth: formattedBirthdate
      })
    };

    console.log(user.body);

    fetch('http://localhost:8080/api/nonauthorized/user/register', user)
      .then(async (response) => {
        if (response.status === 200) {
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
         localStorage.setItem('user', email)
        return await response;
        }).then((response) => {
          if (response.status == 200){
            console.log("redirect");
           navigate("/profile");
           
          }
          else {console.log (response.status)}
        })
        } else {
          throw await response;
        }
      });
  };

  return (
    <Container>
      <form noValidate autoComplete="off">
        <h1>Registration form</h1>

        <TextField
          style={{ margin: '10px auto' }}
          id="outlined-basic"
          label="Nickname"
          variant="outlined"
          fullWidth
          value={nickname}
          onChange={(e) => setNickname(e.target.value)}
        />

        <TextField
          style={{ margin: '10px auto' }}
          id="outlined-basic"
          label="Email"
          variant="outlined"
          fullWidth
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <TextField
          type="password"
          style={{ margin: '10px auto' }}
          id="outlined-basic"
          label="Password"
          variant="outlined"
          fullWidth
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <DatePicker 
         format="YYYY-MM-DD"
         maxDate={today.getDate}
         value={date}
         onChange={(newDate) => setBirthdate(newDate)}
         />
        

        <Button variant="contained" onClick={handleClick}>
          Submit
        </Button>
      </form>
    </Container>
  );
}