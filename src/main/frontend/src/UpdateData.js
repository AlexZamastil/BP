import React, { useState, useEffect } from 'react';
import { Container} from '@mui/system';
import {TextField,Button} from '@mui/material';
import { Paper } from '@mui/material';
import { DatePicker } from '@mui/x-date-pickers';
import {useNavigate} from "react-router-dom"
import InputAdornment from '@mui/material/InputAdornment';
import InputLabel from '@mui/material/InputLabel';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import {FormHelperText} from '@mui/material';

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
  const [sex,setSex] = useState("");
  const [token, setToken] = useState("");
  const [csrfToken,setCsrfToken] = useState("") 

  useEffect((csrfToken) => {
    const xsrfToken = getCookie('XSRF-TOKEN');
    setCsrfToken(xsrfToken);
    console.log(csrfToken);
  },[])
  

  useEffect(() => {
    fetch("https://localhost:8443/api/authorized/user/getuserdata", {
      method: "GET",
      headers: {
        'Authorization': localStorage.getItem("token")
      }
    })
    .then(async (response) => response.json())
    .then(async (response) => {
      setId(response.user.id);
      setEmail(response.user.email);
      setNickname(response.user.nickname);
      setPassword(response.user.password);
      setBirthdate(new Date(response.user.dateOfBirth));
      setWeight(response.user.weight);
      setHeight(response.user.height);
      setBodyType(response.user.bodyType);
      setSex(response.user.sex);
      setToken(response.user.token);
    
    });

  }, []);


  
  const date = useState("");
  const formattedBirthdate = birthdate ? birthdate.toISOString().split('T')[0] : '';

  const handleUpdate = (e) => {
    e.preventDefault();

    const user = {
      method: 'POST',
      headers: {
        'Authorization': localStorage.getItem("token"),
        'Content-Type': 'application/json',
        'X-XSRF-TOKEN': csrfToken 
      },credentials : "include",
        body: JSON.stringify({
        id : id,
        email: email,
        nickname: nickname,
        password: password,
        dateOfBirth: formattedBirthdate,
        weight: weight,
        height: height,
        bodyType: bodyType,
        sex : sex,
        token : token
      }),
    };

    console.log(user.body);

    fetch('https://localhost:8443/api/authorized/user/updateData', user)
      .then(async (response) => {
        if (response.status === 200) {
                console.log("DATA UPDATED SUCCESSFULLY");
                navigate("/Profile")
        } else {
          throw await response;
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