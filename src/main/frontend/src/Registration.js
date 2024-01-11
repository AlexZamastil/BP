import React, { useState } from 'react';
import { Container} from '@mui/system';
import {TextField,Button} from '@mui/material';
import { Paper } from '@mui/material';
import { DatePicker } from '@mui/x-date-pickers';
import {useNavigate} from "react-router-dom"

export default function Registration() {
  const navigate = useNavigate();
  const [errorMessage, setErrorMessage] = useState('');
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
          
          if (response.status === 200){
            console.log('Logged in successfully')
            const token = await response.text();
            localStorage.setItem('token', token);
            localStorage.setItem('user', email);
            console.log("redirect to profile");
          navigate("/profile");
          }})
        } else {const errorResponse = await response.text();
          setErrorMessage(errorResponse);
          console.error('Registration failed:', errorResponse);
        }}).catch(error => {
        console.error('Error occurred during login:', error);
        setErrorMessage('An error occurred during login');
      });
  };

  return (
    <div className='loginBG'>
    <Container>
       <Paper elevation={3} className='paper'>
      <form noValidate autoComplete="off">
        <h1>Registration form</h1>

        <TextField
          style={{ margin: '10px auto' }}
          sx={{ m: 1, width: '25ch' }}
          id="outlined-basic"
          label="Nickname"
          variant="outlined"
          fullWidth
          value={nickname}
          onChange={(e) => setNickname(e.target.value)}
        />

        <TextField
          style={{ margin: '10px auto' }}
          sx={{ m: 1, width: '25ch' }}
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
          sx={{ m: 1, width: '25ch' }}
          id="outlined-basic"
          label="Password"
          variant="outlined"
          fullWidth
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <DatePicker 
        className='datepicker'
        sx={{ m: 1, width: '25ch' }}
        style={{ margin: '10px auto' }}
         format="YYYY-MM-DD"
         maxDate={today.getDate}
         value={date}
         onChange={(newDate) => setBirthdate(newDate)}
         />

        <Button variant="contained" onClick={handleClick}> Submit </Button>

        {errorMessage && (
              <div style={{ color: 'red', marginTop: '10px' }}>
                {errorMessage}
              </div>
            )}
      </form>
      </Paper>
    </Container>
    </div>
  );
}