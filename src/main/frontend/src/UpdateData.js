import React, { useState, useEffect } from 'react';
import { TextField, Button } from '@mui/material';
import { Paper } from '@mui/material';
import { DatePicker } from '@mui/x-date-pickers';
import { useNavigate } from "react-router-dom"
import InputAdornment from '@mui/material/InputAdornment';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import { FormHelperText } from '@mui/material';
import { callAPI } from './CallAPI';
import getXSRFtoken from './XSRF_token';
import { useTranslation } from 'react-i18next';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';

/**
 * Author: Alex Zamastil
 * File contains a form for updating user data.
 */

export default function UpdateData() {
  const navigate = useNavigate();
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  const [id, setId] = useState("");
  const [email, setEmail] = useState("");
  const [nickname, setNickname] = useState("");
  const [password, setPassword] = useState("");
  const [birthdate, setBirthdate] = useState("");
  const [weight, setWeight] = useState("");
  const [height, setHeight] = useState("");
  const [bodyType, setBodyType] = useState("");
  const [sex, setSex] = useState("");
  const [role, setRole] = useState("");
  const [token, setToken] = useState("");

  const xsrfToken = getXSRFtoken();
  const { t } = useTranslation();
  const [errorMessage, setErrorMessage] = useState('');
  const theme = useTheme();
  const isSmallScreen = useMediaQuery(theme.breakpoints.down('sm'));
  useEffect(() => {
    callAPI("GET", "user/getUserData", null, null)
      .then(response => {
        setId(response.data.user.id);
        setEmail(response.data.user.email);
        setNickname(response.data.user.nickname);
        setPassword(response.data.user.password);
        setBirthdate(new Date(response.data.user.dateOfBirth));
        setWeight(response.data.user.weight);
        setHeight(response.data.user.height);
        setBodyType(response.data.user.bodyType);
        setSex(response.data.user.sex);
        setRole(response.data.user.role);
        setToken(response.data.user.token);
      })
  }, [])

  const formattedBirthdate = birthdate ? birthdate.toISOString().split('T')[0] : '';
  //hendler that allows only number input, used in textfields
  const onlyNumbers = (e) => {
    const inputValue = e.target.value.replace(/[^0-9]/g, '');
    e.target.value = inputValue;
  };
  //handler that sends update data request to server
  const handleUpdate = (e) => {
    e.preventDefault();

    const data = {
      id: id,
      email: email,
      nickname: nickname,
      password: password,
      dateOfBirth: formattedBirthdate,
      weight: weight,
      height: height,
      bodyType: bodyType,
      sex: sex,
      role: role,
      token: token
    };

    console.log(data);

    callAPI("POST", "user/updateData", data, xsrfToken)
      .then(response => {
        console.log("DATA UPDATED SUCCESSFULLY");
        navigate("/Profile")
      })
      .catch((error) => {
        console.log(error)
        if (error.response && error.response.data === "Token expired") {
          navigate("/tokenExpired")
        }
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
      })
  };

//file returns a form for updating data
  return (
    <div className='divupdate'>

      <Paper elevation={3} className='paper'>
        <form noValidate autoComplete="off">
          <h1>{t('user_data')}</h1>

          <TextField
            style={{ margin: '10px auto' }}
            id="outlined-basic"
            label={t('username')}
            variant="outlined"
            sx={{ m: 1, width: isSmallScreen ? '20ch' : '25ch' }}
            fullWidth
            value={nickname}
            onChange={(e) => setNickname(e.target.value)}
          />
          <br />
          <TextField
            style={{ margin: '10px auto' }}
            id="outlined-basic"
            label={t('email')}
            variant="outlined"
            fullWidth
            sx={{ m: 1, width: isSmallScreen ? '20ch' : '25ch' }}
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />
          <br />
          <TextField
            style={{ margin: '10px auto' }}
            label={t('weight')}
            value={weight}
            id="outlined-start-adornment"
            sx={{ m: 1, width: isSmallScreen ? '20ch' : '25ch' }}
            onInput={onlyNumbers}
            InputProps={{
              startAdornment: <InputAdornment position="start">kg</InputAdornment>,
            }}
            onChange={(e) => setWeight(e.target.value)}
          />
          <br />
          <TextField
            style={{ margin: '10px auto' }}
            label={t('height')}
            value={height}
            id="outlined-start-adornment"
            sx={{ m: 1, width: isSmallScreen ? '20ch' : '25ch' }}
            onInput={onlyNumbers}
            InputProps={{
              startAdornment: <InputAdornment position="start">M</InputAdornment>,
            }}
            onChange={(e) => setHeight(e.target.value)}
          />
          <br />
          <FormHelperText>{t('select_bodytype')}</FormHelperText>
          <Select
            labelId="demo-simple-select-label"
            id="demo-simple-select"
            value={bodyType}
            label={t('body_type')}
            sx={{ m: 1, width: isSmallScreen ? '20ch' : '25ch' }}
            onChange={(e) => setBodyType(e.target.value)}
          >
            <MenuItem value={"AVERAGE"}>{t('average')}</MenuItem>
            <MenuItem value={"ATHLETIC"}>{t('athletic')}</MenuItem>
            <MenuItem value={"OBESE"}>{t('obese')}</MenuItem>
          </Select>
          <br />
          <FormHelperText>{t('select_sex')}</FormHelperText>
          <Select
            labelId="demo-simple-select-label"
            style={{ margin: '10px auto' }}
            id="demo-simple-select"
            value={sex}
            label={t('sex')}
            sx={{ m: 1, width: isSmallScreen ? '20ch' : '25ch' }}
            onChange={(e) => setSex(e.target.value)}
          >
            <MenuItem value={"MALE"}>{t('man')}</MenuItem>
            <MenuItem value={"FEMALE"}>{t('women')}</MenuItem>
          </Select>
          <br />
          <FormHelperText>{t('select_birth')}</FormHelperText>
          <DatePicker
            className='datepicker'
            sx={{ m: 1, width: isSmallScreen ? '20ch' : '25ch' }}
            style={{ margin: '10px auto' }}
            format="DD-MM-YYYY"
            onChange={(newDate) => setBirthdate(newDate)}
          />
          <br />
          <Button variant="contained" style={{ margin: "10px" }} onClick={handleUpdate}> {t('submit')} </Button>

          {errorMessage && (
            <div style={{ color: 'red', marginTop: '10px' }}>
              {errorMessage}
            </div>
          )}
        </form>
      </Paper>


      <Button variant="contained" color='dark' style={{ margin: "10px" }} onClick={() => navigate("/changePassword")}> {t('change_password')} </Button>
      <Button variant="contained" color='secondary' style={{ margin: "10px" }} onClick={() => navigate("/deleteUser")}> {t('delete_user_button')} </Button>
    </div>
  );
}