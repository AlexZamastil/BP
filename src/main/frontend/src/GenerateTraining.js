import React, { useState, useEffect } from "react";
import { Container } from '@mui/system';
import { TextField, Button, FormControlLabel, Checkbox } from '@mui/material';
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
import { TimeField } from '@mui/x-date-pickers/TimeField';
dayjs.extend(utc);
dayjs.extend(timezone);

/**
 * Author: Alex Zamastil
 * File for creating a training.
 */

export default function GenerateTraining() {
    const { t } = useTranslation();
    const navigate = useNavigate();
    const { trainingType } = useParams();
    const [pageContent, setPageContent] = useState(null);
    const [age, setAge] = useState(null);
    const [selectPace, setSelectPace] = useState(true);
    const theme = useTheme();
    const [raceDate,setRaceDay] = useState(null);
    const isSmallScreen = useMediaQuery(theme.breakpoints.down('sm'));
    const [length, setLength] = useState(null);
    const [pace, setPace] = useState(null);
    const [time, setTime] = useState(null);
    const [formattedRaceDate, setFormattedRaceDate] = useState(new Date());
    const [raceLength, setRaceLength] = useState(0);
    const [wantedPace, setWantedPace] = useState(0);
    const [elevation, setElevation] = useState("FLAT_ROAD");
    const [monday, setMonday] = useState(false);
    const [tuesday, setTuesday] = useState(false);
    const [wednesday, setWednesday] = useState(false);
    const [thursday, setThursday] = useState(false);
    const [friday, setFriday] = useState(false);
    const [saturday, setSaturday] = useState(false);
    const [sunday, setSunday] = useState(false);
    const [errorMessage, setErrorMessage] = useState(null);
    const xsrfToken = getXSRFtoken();
    function displayHeartBeatInfo() {
        if (age != null) {
            const maxHB = Math.floor(211 - 0.8 * age);
            const avgMin = Math.floor(maxHB * 0.6);
            const avgMax = Math.floor(maxHB * 0.85);
            return (
                <>
                    {t('HB_info_1')} {age} {t('HB_info_2')} {maxHB} {t('HB_info_3')} {t('HB_info_4')} {avgMin} - {avgMax} {t('HB_info_5')}
                </>
            )
        }
    }
    //function for sending create-training request to server
    function generateTraining() {
        let y = checkDays();
        console.log("number of days: " + y);
        if (y < 3 || y > 5) {
            setErrorMessage(t('invalid_day_count'));
            return;
        }
       //
        setRaceDay(formatDay(raceDate));

        const data = {
            startDay: dayjs().toISOString().split("T")[0],
            type: trainingType,
            lengthOfRaceInMeters: raceLength,
            wantedPace: wantedPace,
            raceDay: raceDate,
            elevationProfile: elevation,
            monday: monday,
            tuesday: tuesday,
            wednesday: wednesday,
            thursday: thursday,
            friday: friday,
            saturday: saturday,
            sunday: sunday
        };

        console.log(data);
        callAPI("POST", "training/generateTraining", JSON.stringify(data), xsrfToken)
            .then((response) => {
                navigate("/Training");
            })
            .catch((error) => {
                let errorMessage = 'An error occurred during generating a training';
                if (error.response && error.response.data === "Token expired") {
                    navigate("/tokenExpired")
                }
                if (error.response) {
                    console.log(error.response.data)
                    setErrorMessage(error.response.data)
                }
            })
    }
    //function for maintaining expected time format
    function formatDay(date) {
        const formattedDate = date ? date.toISOString().split("T")[0] : null;
        return(formattedDate);
    }
    //handler that allows input only numbers, used in textfields
    const onlyNumbers = (e) => {
        const inputValue = e.target.value.replace(/[^0-9.]/g, '');
        e.target.value = inputValue;
    };
    //function to count the number of training days
    function checkDays() {
        let days = 0;
        if (monday === true) days++;
        if (tuesday === true) days++;
        if (wednesday === true) days++;
        if (thursday === true) days++;
        if (friday === true) days++;
        if (saturday === true) days++;
        if (sunday === true) days++;
        return days;
    }

    //handler that calculates the age of user
    const handleAge = (date) => {
        const today = new Date();
        const day = new Date(date);
        const difference = (today - day);
        const ageInYears = Math.floor(difference / 1000 / 60 / 60 / 24 / 365.25);
        setAge(ageInYears);
    }
    //getting user data from server and setting page content based on obtained data
    //if user doesn't have average values assigned, the average values form is rendered, the create training form is rendered otherwhise
    useEffect(() => {
        callAPI("GET", "user/getUserData", null, null)
            .then(async (response) => {
                const userStats = await response.data;
                handleAge(userStats.user.dateOfBirth)
                if (userStats.averageRunLength === null || userStats.averageRunLength === 0 || userStats.averageRunPace === null || userStats.averageRunPace === 0) {
                    setPageContent(addAverageValuesForm);
                }
                else {
                    setPageContent(generateTrainingForm(userStats.averageRunLength, userStats.averageRunPace));
                }
            })
    }, [t, age, selectPace, monday, tuesday, wednesday, thursday, friday, saturday, sunday, errorMessage, raceDate, raceLength, wantedPace, elevation, length, pace, time, isSmallScreen]);

    //function that declares the create training form
    function generateTrainingForm(length, pace) {
        return (
            <div>
                <Container>
                    <Paper elevation={3} className='paper'>
                        <form noValidate autoComplete="off">
                            <h1> {t('begin_training')}</h1>
                            <br />
                            <FormHelperText>{t('select_date')}</FormHelperText>
                            <DatePicker
                                className='datepicker'
                                style={{ margin: '20px' }}
                                sx={{ m: 0, width: isSmallScreen ? '20ch' : '25ch' }}
                                format="DD-MM-YYYY"
                                label={t('race_date')}
                                onChange={(e) => {
                                    setRaceDay(e)
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
                                    setRaceLength(e.target.value)
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
                                placeholder="5.8"
                                label={t('wanted_pace')}
                                onInput={onlyNumbers}
                                onChange={(e) => {
                                    setWantedPace(e.target.value)
                                }}
                            />
                            <br />
                            {(trainingType === "RUN") ? (
                                <>
                                    <FormHelperText>{t('select_elevation')}</FormHelperText>
                                    <Select
                                        labelId="demo-simple-select-label"
                                        id="demo-simple-select"
                                        label="Race type"
                                        sx={{ m: 1, width: isSmallScreen ? '20ch' : '25ch' }}
                                        style={{ margin: '10px auto' }}
                                        value={elevation}
                                        onChange={(e) => {
                                            console.log(e.target.value)
                                            setElevation(e.target.value)
                                        }}
                                    >
                                        <MenuItem value={"FLAT_ROAD"}> {t('flat')} </MenuItem>
                                        <MenuItem value={"CROSS"}> {t('cross')} </MenuItem>
                                        <MenuItem value={"UPHILL"}> {t('up_hill')} </MenuItem>
                                    </Select>


                                </>
                            ) : (
                                null
                            )
                            }

                            <FormHelperText>{t('select_days')}</FormHelperText>
                            <FormControlLabel
                                control={<Checkbox checked={monday} onChange={(e) => setMonday(e.target.checked)} />}
                                label={t("monday")}
                            /> <br />
                            <FormControlLabel
                                control={<Checkbox checked={tuesday} onChange={(e) => setTuesday(e.target.checked)} />}
                                label={t("tuesday")}
                            /> <br />
                            <FormControlLabel
                                control={<Checkbox checked={wednesday} onChange={(e) => setWednesday(e.target.checked)} />}
                                label={t("wednesday")}
                            /> <br />
                            <FormControlLabel
                                control={<Checkbox checked={thursday} onChange={(e) => setThursday(e.target.checked)} />}
                                label={t("thursday")}
                            /> <br />
                            <FormControlLabel
                                control={<Checkbox checked={friday} onChange={(e) => setFriday(e.target.checked)} />}
                                label={t("friday")}
                            /> <br />
                            <FormControlLabel
                                control={<Checkbox checked={saturday} onChange={(e) => setSaturday(e.target.checked)} />}
                                label={t("saturday")}
                            /> <br />
                            <FormControlLabel
                                control={<Checkbox checked={sunday} onChange={(e) => setSunday(e.target.checked)} />}
                                label={t("sunday")}
                            /> <br />

                            <Button variant="contained" onClick={() => generateTraining()}> {t('submit')} </Button>
                            <br />
                            {errorMessage && (
                                <div style={{ color: 'red', marginTop: '10px' }}>
                                    {errorMessage}
                                </div>
                            )}
                        </form>
                    </Paper>
                </Container>
                <br />
                <Container>
                    <Paper className='paper'>
                        <h1> {t('average_values')}</h1>
                        {t('average_length')} : {length} <br />
                        {t('average_pace')} : {pace} <br />
                        <Button variant="contained" onClick={() => editAverageValues()}> {t('change_values')} </Button>
                    </Paper>
                </Container>

            </div>
        )
    }
    //function that resets the average values and displays add average values form again
    function editAverageValues() {
        const data = {
            averageRunLength: 0,
            averageRunPace: 0
        }
        callAPI("POST", "user/addAverageValues", data, xsrfToken);
        setPageContent(addAverageValuesForm);
    }
    //function that counts user's pace based on time
    function handlePace(length1,time) {
        if(time === undefined || time === null) {
        } else {
            var dateTime = new Date(time);
            var hours = dateTime.getHours();
            var minutes = dateTime.getMinutes();
            var seconds = dateTime.getSeconds();

            let minutesTotal = hours * 60 + minutes + seconds / 60;
            let pace = (minutesTotal / (length1 / 1000)).toFixed(2);
            setPace(pace);
            
        }
    }
    //function that renders add average values form
    function addAverageValuesForm() {


        const toggleSelect = () => {
            setSelectPace(!selectPace);
        };

        const addAverageValues = () => {
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
                                sx={{ m: 1, width: isSmallScreen ? '20ch' : '25ch' }}
                                InputProps={{
                                    inputMode: 'numeric',
                                    pattern: '[0-9]*',
                                    startAdornment: <InputAdornment position="start">{t('meters')}</InputAdornment>,
                                }}
                                placeholder="5000"
                                onInput={onlyNumbers}
                                onChange={(e) => {
                                    setLength(e.target.value)
                                    handlePace(e.target.value,time)
                                }}
                            />
                            <br />
                            {selectPace ? (
                                <TextField
                                    style={{ margin: '10px auto' }}
                                    sx={{ m: 1, width: isSmallScreen ? '20ch' : '25ch' }}
                                    InputProps={{
                                        inputMode: 'numeric',
                                        pattern: '[0-9]*',
                                        startAdornment: <InputAdornment position="start">Min/Km</InputAdornment>,
                                    }}
                                    placeholder="6.5"
                                    label={t('average_pace')}
                                    onInput={onlyNumbers}
                                    onChange={(e) => {
                                        setPace(e.target.value);
                                    }}
                                />) : (
                                <>
                                    <TimeField
                                        label={t('average_time')}
                                        format="HH:mm:ss"
                                        sx={{ m: 1, width: isSmallScreen ? '20ch' : '25ch' }}
                                        onChange={(e) => {
                                            handlePace(length,e);
                                            setTime(e);
                                        }}
                                    /></>)}

                            <br />

                            <Button onClick={toggleSelect}>
                                {selectPace ? t('select_time') : t('select_pace')}
                            </Button>
                            <br />

                            <Button variant="contained" onClick={() => addAverageValues()}> {t('add_average_values')} </Button>
                        </Paper>
                    </Container>

                </div>

                <div className="trainingDiv1">
                    <Container>
                        <Paper elevation={3} className="paper">
                            <h1>{t('how_to_get_average_values_header')}</h1>
                            <br />
                            {t('how_to_get_average_values_text')}
                        </Paper>
                    </Container>
                </div>

                <div className="trainingDiv1">
                    <Container>
                        <Paper elevation={3} className="paper">
                            <h1>{t('heartbeat_info')}</h1>
                            <br />
                            {t('heartbeat_paragraph')}
                            <br />
                            <br />
                            {displayHeartBeatInfo()}

                        </Paper>
                    </Container>
                </div>
            </>
        )
    }
    //fiel returns the pagecontent, that is assigned based on data obtained from server
    return <div className="createTrainingDiv">
        {pageContent}
    </div>
}