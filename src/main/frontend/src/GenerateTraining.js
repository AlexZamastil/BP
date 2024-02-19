import React, { useState } from "react";
import { Container } from '@mui/system';
import { TextField, Button } from '@mui/material';
import { Paper } from '@mui/material';
import { DatePicker } from '@mui/x-date-pickers';
import InputAdornment from '@mui/material/InputAdornment';
import { useParams } from 'react-router-dom';
import { TimeField } from '@mui/x-date-pickers/TimeField';
import dayjs from 'dayjs';
import utc from 'dayjs/plugin/utc';
import timezone from 'dayjs/plugin/timezone';
import 'dayjs/locale/en';
import { FormHelperText } from '@mui/material';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import { callAPI } from './CallAPI';
import getXSRFtoken from './XSRF_token';

dayjs.extend(utc);
dayjs.extend(timezone);

export default function GenerateTraining() {
    const { trainingType } = useParams();
    const timeZone = "UTC";

    const [trainingData, setTrainingData] = useState(() => ({
        startDay: dayjs().toISOString().split("T")[0],
        goal: trainingType,
        lengthOfRaceInMeters: 0,
        wantedTime: null,
        actualRunLength: 0,
        actualTime: null,
        raceDay: dayjs().toISOString().split("T")[0],
        elevationProfile: "CROSS"
    }));

    const xsrfToken = getXSRFtoken();

    const setRaceDay = (selectedDate) => {
        const formattedDate = selectedDate ? selectedDate.toISOString().split("T")[0] : null;
        setTrainingData({ ...trainingData, raceDay: formattedDate });
    };

    const onlyNumbers = (e) => {
        const inputValue = e.target.value.replace(/[^0-9]/g, '');
        e.target.value = inputValue;
    };

    function generateTraining() {
        callAPI("POST", "training/generateTraining", JSON.stringify(trainingData), xsrfToken)
        console.log(trainingData);
        console.log(xsrfToken);
        console.log("Actual Time:", trainingData.actualTime);
        console.log("Wanted Time:", trainingData.wantedTime);
    }

    return (
        <div>
            <Container>
                <Paper elevation={3} className='paper'>
                    <form noValidate autoComplete="off">
                        <h1>set up training</h1>

                        <TextField
                            style={{ margin: '10px auto' }}
                            label="Average run length   "
                            id="outlined-start-adornment"
                            sx={{ m: 1, width: '25ch' }}
                            InputProps={{
                                inputMode: 'numeric',
                                pattern: '[0-9]*',
                                startAdornment: <InputAdornment position="start">Meters</InputAdornment>,
                            }}
                            onInput={onlyNumbers}
                            onChange={(e) => setTrainingData({ ...trainingData, actualRunLength: e.target.value })}
                        />

                        <TimeField
                            style={{ margin: '10px auto' }}
                            sx={{ m: 1, width: '25ch' }}
                            label="Average run time"
                            format="HH:mm:ss"
                            onChange={(e) => setTrainingData({ ...trainingData, actualTime: e })}
                            timezone={timeZone}
                        />

                        <DatePicker
                            className='datepicker'
                            style={{ margin: '10px auto' }}
                            sx={{ m: 0, width: '25ch' }}
                            format="DD-MM-YYYY"
                            label="Race Date"
                            onChange={(newDate) => setRaceDay(newDate)}
                        />

                        <TextField
                            style={{ margin: '10px auto' }}
                            label="Race length"
                            id="outlined-start-adornment"
                            sx={{ m: 1, width: '25ch' }}
                            InputProps={{
                                inputMode: 'numeric',
                                pattern: '[0-9]*',
                                startAdornment: <InputAdornment position="start">Meters</InputAdornment>,
                            }}
                            onInput={onlyNumbers}
                            onChange={(e) => setTrainingData({ ...trainingData, lengthOfRaceInMeters: e.target.value })}
                        />

                        <TimeField
                            style={{ margin: '10px auto' }}
                            sx={{ m: 1, width: '25ch' }}
                            label="Wanted time"
                            format="HH:mm:ss"
                            onChange={(e) => setTrainingData({ ...trainingData, wantedTime: e })}
                            timezone={timeZone}
                        />

                        {onlyRunSelect()}
                        <br />

                        <Button variant="contained" onClick={generateTraining}> Submit </Button>
                    </form>
                </Paper>
            </Container>
        </div>
    )

    function onlyRunSelect() {
        if (trainingData.goal === "RUN") {
            return <>

                <Select
                    labelId="demo-simple-select-label"
                    id="demo-simple-select"
                    label="Race type"
                    sx={{ m: 1, width: '25ch' }}
                    style={{ margin: '10px auto' }}
                    onChange={(e) => setTrainingData({ ...trainingData, elevationProfile: e.target.value })}
                >
                    <MenuItem value={"FLAT_ROAD"}> FLAT </MenuItem>
                    <MenuItem value={"CROSS"}> CROSS </MenuItem>
                    <MenuItem value={"UPHILL"}> UPHILL </MenuItem>
                </Select>

                <FormHelperText>Select elevation profile, that defines major part of the race</FormHelperText>
            </>
        } else return null;
    }
}