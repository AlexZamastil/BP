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
import { useTranslation } from 'react-i18next';
import InputAdornment from '@mui/material/InputAdornment';


export default function Registration() {
  const navigate = useNavigate();
  const { t } = useTranslation()
  const [errorMessage, setErrorMessage] = useState('');

  const  [user,setUser] = useState({
    email: "",
    nickname: "",
    password: "",
    weight: null,
    height: null,
    dateOfBirth: null,
    sex: null,
    bodyType: null
  });
  const formattedBirthdate = user.birthdate ? user.birthdate.toISOString().split('T')[0] : '';
  const xsrfToken = getXSRFtoken();

  const onlyNumbers = (e) => {
    const inputValue = e.target.value.replace(/[^0-9]/g, '');
    e.target.value = inputValue;
};


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
            <h1>{t('registration_form')}</h1>

            <TextField
              style={{ margin: '10px auto' }}
              sx={{ m: 1, width: '25ch' }}
              id="outlined-basic"
              label={t('username')}
              variant="outlined"
              fullWidth
              value={user.nickname}
              onChange={(e) => setUser({ ...user, nickname: e.target.value })}
            />

            <TextField
              style={{ margin: '10px auto' }}
              sx={{ m: 1, width: '25ch' }}
              id="outlined-basic"
              label={t('email')}
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
              label={t('password')}
              variant="outlined"
              fullWidth
              value={user.password}
              onChange={(e) => setUser({ ...user, password: e.target.value })}
            />

            <FormHelperText>{t('select_sex')}</FormHelperText>
            <Select
              labelId="demo-simple-select-label"
              style={{ margin: '10px auto' }}
              id="demo-simple-select"
              value={user.sex}
              label={t('sex')}
              sx={{ m: 1, width: '30ch' }}
              onChange={(e) => setUser({ ...user,sex: e.target.value})}
            >
              <MenuItem value={"MALE"}>{t('man')}</MenuItem>
              <MenuItem value={"FEMALE"}>{t('women')}</MenuItem>
            </Select>


            <DatePicker
              className='datepicker'
              sx={{ m: 1, width: '25ch' }}
              style={{ margin: '10px auto' }}
              
              format="YYYY-MM-DD"
              label={t('birthdate')}
              onChange={(newDate) => setUser({ ...user, dateOfBirth: newDate})}
            />

          <TextField
                            style={{ margin: '10px auto', padding: "10px" }}
                            label={t('weight')}
                            id="outlined-start-adornment"
                            sx={{ m: 1, width: '13ch' }}
                            InputProps={{
                                inputMode: 'numeric',
                                pattern: '[0-9]*',
                                startAdornment: <InputAdornment position="start">kg</InputAdornment>,
                            }}
                            onInput={onlyNumbers}
                            onChange={(e) => setUser({ ...user, weight: e.target.value })}
                        />

          <TextField
                            style={{ margin: '10px auto', padding: "10px" }}
                            label={t('height')}
                            id="outlined-start-adornment"
                            sx={{ m: 1, width: '13ch' }}
                            InputProps={{
                                inputMode: 'numeric',
                                pattern: '[0-9]*',
                                startAdornment: <InputAdornment position="start">cm</InputAdornment>,
                            }}
                            onInput={onlyNumbers}
                            onChange={(e) => setUser({ ...user, height: e.target.value })}
                        />  

            <FormHelperText>{t('select_bodytype')}</FormHelperText>
            <Select
              labelId="demo-simple-select-label"
              style={{ margin: '10px auto' }}
              id="demo-simple-select"
              value={user.bodyType}
              label={t('body_type')}
              sx={{ m: 1, width: '30ch' }}
              onChange={(e) => setUser({ ...user, bodyType: e.target.value })}
            >
              <MenuItem value={"AVERAGE"}>{t('average')}</MenuItem>
              <MenuItem value={"ATHLETIC"}>{t('athletic')}</MenuItem>
              <MenuItem value={"OBESE"}>{t('obese')}</MenuItem>
              <MenuItem value={"MUSCULAR"}>{t('muscular')}</MenuItem>
            </Select>

            <Button variant="contained" onClick={handleRegistration}> {t('submit')} </Button>

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