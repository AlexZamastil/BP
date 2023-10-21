import React, { useState } from "react";
import { Container } from '@mui/system';
import { TextField, Button } from '@mui/material';
import { Paper } from '@mui/material';
import { DatePicker, TimePicker } from '@mui/x-date-pickers';
import InputAdornment from '@mui/material/InputAdornment';
import { useParams } from 'react-router-dom';
import { TimeField } from '@mui/x-date-pickers/TimeField';

export default function GenerateTraining() {
    const { trainingType } = useParams();
    const [trainingData, setTrainingData] = useState(() => ({
        goal: trainingType,
        lengthOfRaceInMeters: 0,
        wantedTimeInSeconds: 0,
        actualRunLength: 0,
        actualTimeInSeconds: 0,
        raceDay: new Date(),
        startDay: new Date()
    }));
    const onlyNumbers = (e) => {
        const inputValue = e.target.value.replace(/[^0-9]/g, '');
        
        e.target.value = inputValue;
      };

     //TODO : prevest Date na cas v sekundach

    function generateTrainingx() {
      //  console.log(averageTime);
       
    }
        return (
            <div>
                  <Container>
                <Paper elevation={3} className='paper'>
                    <form noValidate autoComplete="off">
                        <h1>set up training</h1>

                        <TextField
                            style={{ margin: '10px auto' }}
                            label="Race length"
                            id="outlined-start-adornment"
                            sx={{ m: 1, width: '25ch' }}
                            InputProps={{
                                inputMode: 'numeric',
                                pattern: '[0-9]*',
                                startAdornment: <InputAdornment position="start">M</InputAdornment>,
                            }}
                            onInput={onlyNumbers}
                            onChange={(e) => setTrainingData({ ...trainingData, lengthOfRaceInMeters: e.target.value })}
                        />
                        <TimeField
                            style={{ margin: '10px auto' }}
                            sx={{ m: 1, width: '25ch' }}
                            label="Average Time"
                            format="HH:mm:ss"
                           // value={wantedTime}
                           // onChange={(newValue) => setWantedTime(newValue)}
                        />
                        <TextField
                            style={{ margin: '10px auto' }}
                            label="Average run length   "
                            id="outlined-start-adornment"
                            sx={{ m: 1, width: '25ch' }}
                            InputProps={{
                                inputMode: 'numeric',
                                pattern: '[0-9]*',
                                startAdornment: <InputAdornment position="start">M</InputAdornment>,
                            }}
                            onInput={onlyNumbers}
                            onChange={(e) => setTrainingData({ ...trainingData, lengthOfRaceInMeters: e.target.value })}
                        />

                        <TimeField
                            style={{ margin: '10px auto' }}
                            sx={{ m: 1, width: '25ch' }}
                            label="Average Time"
                            format="HH:mm:ss"
                           // value={averageTime}
                           // onChange={(newValue) => setAverageTime(newValue)}
                        />
                        <DatePicker 
                                className='datepicker'
                                style={{ margin: '10px auto' }}
                                sx={{ m: 1, width: '25ch' }}
                                format="DD-MM-YYYY"
                                label="Race Date"
                                onChange={(newDate) => setTrainingData({ ...trainingData, raceDay: newDate })}
                                />
                        <Button variant="contained" onClick={generateTrainingx}> Submit </Button>
                    </form>
                </Paper>
            </Container>
            </div>
        );
    };
