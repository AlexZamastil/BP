import React, { useState, useEffect } from "react";
import { Container } from '@mui/system';
import { TextField, Button } from '@mui/material';
import { Paper } from '@mui/material';
import { DatePicker } from '@mui/x-date-pickers';
import InputAdornment from '@mui/material/InputAdornment';
import { useNavigate, useParams } from 'react-router-dom';
import dayjs from 'dayjs';
import utc from 'dayjs/plugin/utc';
import timezone from 'dayjs/plugin/timezone';
import 'dayjs/locale/en';
import { FormHelperText } from '@mui/material';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import { callAPI } from './CallAPI';
import getXSRFtoken from './XSRF_token';
import { useTranslation } from "react-i18next";
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';
dayjs.extend(utc);
dayjs.extend(timezone);

export default function GenerateTraining() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const { trainingType } = useParams();
    const [pageContent, setPageContent] = useState(null);
    const [age,setAge] = useState(null);
    const [trainingData, setTrainingData] = useState(() => ({
        elevationProfile: "CROSS"
    }));
    const theme = useTheme();
    const isSmallScreen = useMediaQuery(theme.breakpoints.down('sm'));
    function displayHeartBeatInfo(){
        if(age != null){
            const maxHB = Math.floor(211-0.8*age);
            const avgMin = Math.floor(maxHB * 0.6);
            const avgMax = Math.floor(maxHB * 0.85);
            return(
                <>
                {t('HB_info_1')} {age} {t('HB_info_2')} {maxHB} {t('HB_info_3')} {t('HB_info_4')} {avgMin} - {avgMax} {t('HB_info_5')} 
                </>
            )
        }
    }

    function onlyRunSelect() {
        if (trainingData.goal === "RUN") {
            return <>
                <Select
                    labelId="demo-simple-select-label"
                    id="demo-simple-select"
                    label="Race type"
                    sx={{ m: 1, width: isSmallScreen ? '20ch' : '25ch' }}
                    style={{ margin: '10px auto' }}
                    onChange={(e) => {
                        setTrainingData(trainingData.elevationProfile = e.target.value)
                    }}
                >
                    <MenuItem value={"FLAT_ROAD"}> FLAT </MenuItem>
                    <MenuItem value={"CROSS"}> CROSS </MenuItem>
                    <MenuItem value={"UPHILL"}> UPHILL </MenuItem>
                </Select>

                <FormHelperText>{t('select_elevation')}</FormHelperText>
            </>
        } else return null;
    }
    function generateTraining(raceDate, raceLength, wantedPace) {
        const data = {
            startDay: dayjs().toISOString().split("T")[0],
            goal: trainingType,
            lengthOfRaceInMeters: raceLength,
            wantedPace: wantedPace,
            raceDay: raceDate,
            elevationProfile: trainingData.elevationProfile
        };

        console.log(data);
        /*callAPI("POST", "training/generateTraining", JSON.stringify(trainingData), xsrfToken)
            .catch((error) => {
                if (error.response && error.response.data === "Token expired") {
                    navigate("/tokenExpired")
                }
            })
            */

    }
    const xsrfToken = getXSRFtoken();

    function setRaceDay(date) {
        const formattedDate = date ? date.toISOString().split("T")[0] : null;
        return formattedDate;
    }

    const onlyNumbers = (e) => {
        const inputValue = e.target.value.replace(/[^0-9.]/g, '');
        e.target.value = inputValue;
    };

    
  const handleAge = (date) => {
    const today = new Date();
    const day = new Date(date);
    const difference = (today - day);
    const ageInYears = Math.floor(difference/1000/60/60/24/365.25);
    setAge(ageInYears);
  }

    function addAverageValues(length, pace) {
        console.log(length)
        console.log(pace)
        const data = {
            averageRunLength: length,
            averageRunPace: pace
        }
        console.log(data)
        callAPI("POST", "user/addAverageValues", data, xsrfToken)
            .then((response) => {
                console.log(response.data);
                window.location.reload(false);
            })
            .catch((error) => {
                if (error.response && error.response.data === "Token expired") {
                    navigate("/tokenExpired")
                }
            })

    };

    useEffect(() => {
        callAPI("GET", "user/getUserData", null, null)
            .then(async (response) => {
                const userStats = await response.data;
                handleAge(userStats.user.dateOfBirth)
                if (userStats.user.averageRunLength === null || userStats.user.averageRunLength === 0 || userStats.user.averageRunPace === null || userStats.user.averageRunPace === 0) {
                    setPageContent(addAverageValuesForm);
                }
                else {
                    setPageContent(generateTrainingForm);
                }
            })
    }, [t,age]);


    function generateTrainingForm() {

        let raceDate = new Date();
        let raceLength = 0;
        let wantedPace = 0;
        let elevationProfile = "CROSS"
        return (
            <div>
                <Container>
                    <Paper elevation={3} className='paper'>
                        <form noValidate autoComplete="off">
                            <h1> {t('begin_training')}</h1>
                            <br />
                            <DatePicker
                                className='datepicker'
                                style={{ margin: '20px' }}
                                sx={{ m: 0, width: isSmallScreen ? '20ch' : '25ch' }}
                                format="DD-MM-YYYY"
                                label={t('race_date')}
                                onChange={(e) => {
                                    setRaceDay(e)
                                    raceDate = setRaceDay(e)
                                }}
                            />
                            <br />
                            <TextField
                                style={{ margin: '10px auto' }}
                                label={t('race_length')}
                                id="outlined-start-adornment"
                                sx={{ m: 1, width: isSmallScreen ? '20ch' : '25ch' }}
                                InputProps={{
                                    inputMode: 'numeric',
                                    pattern: '[0-9]*',
                                    startAdornment: <InputAdornment position="start">{t('meters')}</InputAdornment>,
                                }}
                                onInput={onlyNumbers}
                                onChange={(e) =>
                                    raceLength = e.target.value
                                }
                            />


                            <TextField
                                style={{ margin: '10px auto' }}
                                sx={{ m: 1, width: isSmallScreen ? '20ch' : '25ch' }}
                                InputProps={{
                                    inputMode: 'numeric',
                                    pattern: '[0-9]*',
                                    startAdornment: <InputAdornment position="start">Min/Km</InputAdornment>,
                                }}
                                placeholder="6.5"
                                label={t('wanted_pace')}
                                onInput={onlyNumbers}
                                onChange={(e) => {
                                    wantedPace = e.target.value;
                                }}
                            />
                            <br />

                            {onlyRunSelect()}
                            <br />

                            <Button variant="contained" onClick={() => generateTraining(raceDate, raceLength, wantedPace, elevationProfile)}> {t('submit')} </Button>
                        </form>
                    </Paper>
                </Container>
            </div>
        )
    }
    function addAverageValuesForm() {
        let length = 0;
        let pace = 0;
        return (
            <>
                <div className="trainingDiv1">
                    <Container>
                        <Paper elevation={3} className="paper">
                            {t('average_values_null')}
                            <br />
                            <br />
                            {t('average_values_null2')}
                            <br />
                            <TextField
                                style={{ margin: '10px auto' }}
                                label={t('average_length')}
                                id="outlined-start-adornment"
                                sx={{ m: 1, width: '25ch' }}
                                InputProps={{
                                    inputMode: 'numeric',
                                    pattern: '[0-9]*',
                                    startAdornment: <InputAdornment position="start">{t('meters')}</InputAdornment>,
                                }}
                                placeholder="5000"
                                onInput={onlyNumbers}
                                onChange={(e) => {
                                    length = e.target.value;
                                }}
                            />
                            <br />
                            <TextField
                                style={{ margin: '10px auto' }}
                                sx={{ m: 1, width: '25ch' }}
                                InputProps={{
                                    inputMode: 'numeric',
                                    pattern: '[0-9]*',
                                    startAdornment: <InputAdornment position="start">Min/Km</InputAdornment>,
                                }}
                                placeholder="6.5"
                                label={t('average_pace')}
                                onInput={onlyNumbers}
                                onChange={(e) => {
                                    pace = e.target.value;
                                }}
                            />
                            <br />
                            <Button variant="contained" onClick={() => addAverageValues(length, pace)}> {t('add_average_values')} </Button>
                        </Paper>
                    </Container>
                </div>

                <div className="trainingDiv1">
                <Container>
                        <Paper elevation={3} className="paper">
                        <h1>{t('how_to_get_average_values_header')}</h1>
                            <br/>
                        {t('how_to_get_average_values_text')}
                        </Paper>
                    </Container>
                </div>

                <div className="trainingDiv1">
                <Container>
                        <Paper elevation={3} className="paper">
                        <h1>{t('heartbeat_info')}</h1>
                            <br/>
                        {t('heartbeat_paragraph')}
                            <br/>
                            <br/>
                           {displayHeartBeatInfo()}
                        
                        </Paper>
                    </Container>
                </div>
            </>
        )
    }

    return <div className="createTrainingDiv">
        {pageContent}
    </div>
}