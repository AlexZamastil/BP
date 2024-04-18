import { Paper, Button, TextField } from '@mui/material';
import { useEffect, useState } from 'react';
import { Container } from '@mui/system';
import { FormHelperText } from '@mui/material';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import getXSRFtoken from './XSRF_token';
import { callAPI, callAPIMultipartFile, callAPINoAuth } from './CallAPI';
import { useNavigate } from 'react-router-dom';
import useMediaQuery from '@mui/material/useMediaQuery';
import { useTheme } from '@mui/material/styles';
import { useTranslation } from 'react-i18next';

/**
 * Author: Alex Zamastil
 * Page containing a form for adding a food. This page is only accesible by administrator. 
 */


export default function AddFood() {
    const { t } = useTranslation();
    const navigate = useNavigate()
    const [name, setName] = useState(null);
    const [name_eng, setName_eng] = useState(null);
    const [description, setDescription] = useState(null);
    const [description_eng, setDescription_eng] = useState(null);
    const [tags, setTags] = useState([]);
    const [picture, setPicture] = useState(null);
    const [calories, setCalories] = useState(null);

    const [selectedFile, setSelectedFile] = useState(null);

    const [isAdmin, setIsAdmin] = useState(false);

    const [foodTags, setFoodTags] = useState([]);

    const theme = useTheme();
    const isSmallScreen = useMediaQuery(theme.breakpoints.down('sm'));
    const xsrfToken = getXSRFtoken();
    const [errorMessage, setErrorMessage] = useState('');

    //handler that allows input only numbers, used in textfields
    const onlyNumbers = (e) => {
        const inputValue = e.target.value.replace(/[^0-9.]/g, '');
        e.target.value = inputValue;
    };

    //getting user data from server
    useEffect(() => {
        callAPI("GET", "user/getUserData", null, null)
            .then(async response => {
                const userData = response.data;
                setIsAdmin((userData.user.role === "ADMIN"));
            }
            )
            .catch(error => {
                console.log(error);
                if (error.response && error.response.data === "Token expired") {
                    navigate("/tokenExpired")
                }
            })
    }, []);
    //getting exercise tags from server
    useEffect(() => {
        callAPINoAuth("GET", "tag/getFoodTags", null, null)
            .then(async (response) => {
                const foodTagsText = response.data;
                const foodTagsArray = foodTagsText
                    .slice(1, -1)
                    .split(', ')
                    .map((item) => item.trim());
                setFoodTags(foodTagsArray);
            })
            .catch(async (error) => {
                console.error("Failed to fetch food tags");
                console.error(error);
                if (error.response && error.response.data === "Token expired") {
                    navigate("/tokenExpired")
                }
            })
    }, [])
    // method for trying to send an exercise to server
    const addFood = async (e) => {
        e.preventDefault();
        const formData = new FormData();

        formData.append('foodRequest', new Blob([JSON.stringify({

            name: name,
            name_eng: name_eng,
            description: description,
            description_eng: description_eng,
            calories: calories,
            tags: tags
        })], { type: 'application/json' }));


        if (selectedFile == null) {
            setErrorMessage(t("null_image"));
            return;
        }
        try {
            formData.append('imageData', selectedFile, { type: 'multipart/form-data' });
            callAPIMultipartFile("POST", "food/addFood", formData, xsrfToken)
                .then((response) => {
                    const responseBody = response.data;
                    console.log(responseBody);
                    navigate("/food/" + responseBody)
                })
                .catch((error) => {
                    if (error.response && error.response.data === "Token expired") {
                        navigate("/tokenExpired")
                    }

                    let errorMessage = "An error occurred";
                    if (error.response) {
                        errorMessage = error.response.data || errorMessage;
                    }
                    setErrorMessage(errorMessage);
                })
        } catch (error) {
            if (error.response && error.response.data === "Token expired") {
                navigate("/tokenExpired")
            }
            setErrorMessage("Falied to communicate with server");
        }
    };

    //method for uploading a picture
    const handleImageChange = (e) => {
        const file = e.target.files[0];

        if (file) {
            const reader = new FileReader();

            reader.onload = (event) => {
                setPicture(event.target.result);
            };

            reader.readAsDataURL(file);
            setSelectedFile(file);
        }
    };

    //file returns a form
    return (
        <div>
            {isAdmin !== undefined ? (
                isAdmin ? (
                    <Container style={{ padding: "20px" }}>
                        <Paper elevation={3} className='paper'>
                            <form noValidate autoComplete="off">
                                <h1>{t("add_food")}</h1>

                                <TextField
                                    style={{ margin: '10px auto' }}
                                    sx={{ m: 1, width: isSmallScreen ? '20ch' : '25ch' }}
                                    id="outlined-basic"
                                    label={t("name")}
                                    variant="outlined"
                                    fullWidth
                                    value={name}
                                    onChange={(e) => setName(e.target.value)}
                                />

                                <TextField
                                    style={{ margin: '10px auto' }}
                                    sx={{ m: 1, width: isSmallScreen ? '20ch' : '25ch' }}
                                    id="outlined-basic"
                                    label={t("name_eng")}
                                    variant="outlined"
                                    fullWidth
                                    value={name_eng}
                                    onChange={(e) => setName_eng(e.target.value)}
                                />

                                <TextField
                                    style={{ margin: '10px auto' }}
                                    sx={{ m: 1, width: isSmallScreen ? '20ch' : '25ch' }}
                                    id="outlined-basic"
                                    label={t("description")}
                                    variant="outlined"
                                    fullWidth
                                    value={description}
                                    onChange={(e) => setDescription(e.target.value)}
                                />

                                <TextField
                                    style={{ margin: '10px auto' }}
                                    sx={{ m: 1, width: isSmallScreen ? '20ch' : '25ch' }}
                                    id="outlined-basic"
                                    label={t("description_eng")}
                                    variant="outlined"
                                    fullWidth
                                    value={description_eng}
                                    onChange={(e) => setDescription_eng(e.target.value)}
                                />

                                <TextField
                                    style={{ margin: '10px auto' }}
                                    sx={{ m: 1, width: isSmallScreen ? '20ch' : '25ch' }}
                                    id="outlined-basic"
                                    label={t("calories_gain")}
                                    variant="outlined"
                                    fullWidth
                                    onInput={onlyNumbers}
                                    value={calories}
                                    onChange={(e) => setCalories(e.target.value)}
                                />




                                {TagSelection(foodTags)}
                                <br />

                                <input
                                    type="file"
                                    accept="image/*"
                                    enctype='multipart/form-data'
                                    onChange={handleImageChange}
                                />

                                {picture && (
                                    <div>
                                        <h3>Preview:</h3>
                                        <img src={picture} alt="Selected" style={{ maxWidth: '100%' }} />
                                    </div>
                                )}


                                <Button style={{ margin: "10px" }} variant="contained" onClick={addFood}>{t("submit")} </Button>
                                {errorMessage && (
                                    <div style={{ color: 'red', marginTop: '10px' }}>
                                        {errorMessage}
                                    </div>
                                )}
                            </form>
                        </Paper>
                    </Container>

                ) : (
                    <p>Admin only</p>
                )
            ) : (
                <p>Loading...</p>
            )}
        </div>
    );
    //method for setting up a list of tags of the exercise
    function TagSelection(pool) {

        const handleTagSelection = (tag) => {
            setTags((prevSelectedTags) => {
                if (prevSelectedTags.includes(tag)) {
                    return prevSelectedTags.filter((item) => item !== tag);
                } else {
                    return [...prevSelectedTags, tag];
                }
            });
        };

        return (
            <div>
                <h1>{t("tag_selection")}</h1> <br />
                <ul>
                    {pool.map((tag) => (
                        <li className='listStyleTypeNone' key={tag}>
                            <label>
                                <input
                                    type="checkbox"
                                    checked={tags.includes(tag)}
                                    onChange={() => handleTagSelection(tag)}
                                />
                                {tag}
                            </label>
                        </li>
                    ))}
                </ul>
            </div>
        );
    }

}





