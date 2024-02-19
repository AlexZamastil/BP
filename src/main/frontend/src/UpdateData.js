import React, { useState, useEffect } from 'react';
import { Container } from '@mui/system';
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

  useEffect(()=>{
    callAPI("GET","user/getUserData",null,null)
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
  },[])
  
  const formattedBirthdate = birthdate ? birthdate.toISOString().split('T')[0] : '';

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

    callAPI("POST","user/updateData",data,xsrfToken)
    .then(response => {
      if (response.status === 200) {
        console.log("DATA UPDATED SUCCESSFULLY");
        navigate("/Profile")
      } else {
        throw response;
      }
    });

  };
  return (
    <div className='divupdate'>
      <Container>
        <Paper elevation={3} className='paper'>
          <form noValidate autoComplete="off">
            <h1>Update form</h1>

            <TextField
              style={{ margin: '10px auto' }}
              id="outlined-basic"
              label="Nickname"
              variant="outlined"
              sx={{ m: 1, width: '25ch' }}
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
              sx={{ m: 1, width: '25ch' }}
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />

            <TextField
              style={{ margin: '10px auto' }}
              label="Weight"
              id="outlined-start-adornment"
              sx={{ m: 1, width: '25ch' }}
              InputProps={{
                startAdornment: <InputAdornment position="start">kg</InputAdornment>,
              }}
              onChange={(e) => setWeight(e.target.value)}
            />
            <TextField
              style={{ margin: '10px auto' }}
              label="Height"
              id="outlined-start-adornment"
              sx={{ m: 1, width: '25ch' }}
              InputProps={{
                startAdornment: <InputAdornment position="start">M</InputAdornment>,
              }}
              onChange={(e) => setHeight(e.target.value)}
            />

            <FormHelperText>Select what body type describes you best</FormHelperText>
            <Select
              labelId="demo-simple-select-label"
              id="demo-simple-select"
              value={bodyType}
              label="Body Type"
              sx={{ m: 1, width: '25ch' }}
              onChange={(e) => setBodyType(e.target.value)}
            >
              <MenuItem value={"AVERAGE"}>Average</MenuItem>
              <MenuItem value={"ATHLETIC"}>Athletic</MenuItem>
              <MenuItem value={"OBESE"}>Obese</MenuItem>
            </Select>

            <FormHelperText>Select your biological sex</FormHelperText>
            <Select
              labelId="demo-simple-select-label"
              id="demo-simple-select"
              value={sex}
              label="Sex"
              sx={{ m: 1, width: '25ch' }}
              onChange={(e) => setSex(e.target.value)}
            >
              <MenuItem value={"MALE"}>Male</MenuItem>
              <MenuItem value={"FEMALE"}>Female</MenuItem>
            </Select>

            <DatePicker
              className='datepicker'
              sx={{ m: 1, width: '25ch' }}
              style={{ margin: '10px auto' }}
              format="YYYY-MM-DD"
              onChange={(newDate) => setBirthdate(newDate)}
            />

            <Button variant="contained" onClick={handleUpdate}> Submit change </Button>
          </form>
        </Paper>
      </Container>
    </div>
  );
}