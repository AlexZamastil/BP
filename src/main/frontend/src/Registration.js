import React, { useState } from 'react';
import { Container } from '@mui/system';
import { TextField, Button } from '@mui/material';
import { Paper } from '@mui/material';
import { DatePicker } from '@mui/x-date-pickers';
import { useNavigate } from "react-router-dom";
import { FormHelperText } from '@mui/material';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import { callAPINoAuth } from './CallAPI';
import getXSRFtoken from './XSRF_token';


export default function Registration() {
  const navigate = useNavigate();
  const [errorMessage, setErrorMessage] = useState('');

  const  [user,setUser] = useState({
    email: "",
    nickname: "",
    password: "",
    dateOfBirth: null,
    sex: null
  });
  const formattedBirthdate = user.birthdate ? user.birthdate.toISOString().split('T')[0] : '';
  const xsrfToken = getXSRFtoken();

  const handleRegistration = (e) => {
    e.preventDefault();

    console.log(user);

    callAPINoAuth("POST", "user/register", user, xsrfToken)
      .then(response => {
        console.log("registered");
        callAPINoAuth("POST", "user/login", user, xsrfToken)
          .then((response) => {
            console.log('Logged in successfully')
            const token = response.data;
            localStorage.setItem('token', token);
            localStorage.setItem('user', user.email);
            console.log("redirect to profile");
            navigate("/profile");
          })
          .catch((error)=>{
            console.log(error.data)
          })
         
      })
      .catch(error => {
        let errorMessage = 'An error occurred during registration';
        if (error.response) {
            errorMessage = error.response.data || errorMessage;
            console.error('Registration failed:', errorMessage);
        } else if (error.request) {
            console.error('No response received:', error.request);
        } else {
            console.error('Error occurred during registration:', error.message);
        }
        setErrorMessage(errorMessage);
        console.error("Login failed", error);
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
              value={user.nickname}
              onChange={(e) => setUser({ ...user, nickname: e.target.value })}
            />

            <TextField
              style={{ margin: '10px auto' }}
              sx={{ m: 1, width: '25ch' }}
              id="outlined-basic"
              label="Email"
              variant="outlined"
              fullWidth
              value={user.email}
              onChange={(e) => setUser({ ...user, email: e.target.value })}
            />

            <TextField
              type="password"
              style={{ margin: '10px auto' }}
              sx={{ m: 1, width: '25ch' }}
              id="outlined-basic"
              label="Password"
              variant="outlined"
              fullWidth
              value={user.password}
              onChange={(e) => setUser({ ...user, password: e.target.value })}
            />

            <FormHelperText>Select your biological sex</FormHelperText>
            <Select
              labelId="demo-simple-select-label"
              id="demo-simple-select"
              value={user.sex}
              label="Sex"
              sx={{ m: 1, width: '25ch' }}
              onChange={(e) => setUser({ ...user,sex: e.target.value})}
            >
              <MenuItem value={"MALE"}>Male</MenuItem>
              <MenuItem value={"FEMALE"}>Female</MenuItem>
            </Select>


            <DatePicker
              className='datepicker'
              sx={{ m: 1, width: '25ch' }}
              style={{ margin: '10px auto' }}
              format="YYYY-MM-DD"
              label="Birthdate"
              onChange={(newDate) => setUser({ ...user, dateOfBirth: newDate})}
            />

            <Button variant="contained" onClick={handleRegistration}> Submit </Button>

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