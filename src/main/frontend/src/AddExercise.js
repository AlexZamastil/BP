import { Paper, Button, TextField } from '@mui/material';
import { useEffect, useState } from 'react';
import { Container } from '@mui/system';
import { FormHelperText } from '@mui/material';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';
import getXSRFtoken from './XSRF_token';
import { callAPI, callAPIMultipartFile, callAPINoAuth } from './CallAPI';
import { useNavigate } from 'react-router-dom';

export default function AddExercise() {

  const navigate = useNavigate()
  const [name, setName] = useState(null);
  const [name_eng, setName_eng] = useState(null);
  const [description, setDescription] = useState(null);
  const [description_eng, setDescription_eng] = useState(null);
  const [type, setType] = useState(null);
  const [category, setCategory] = useState(null);
  const [style, setStyle] = useState(null);
  const [length, setLength] = useState(null);
  const [series, setSeries] = useState(null);
  const [repetitions, setRepetitions] = useState(null);
  const [tags, setTags] = useState([]);
  const [picture, setPicture] = useState(null);
  const [exerciseType, setExerciseType] = useState('');

  const [selectedFile, setSelectedFile] = useState(null);

  const [isAdmin, setIsAdmin] = useState(false);

  const [exerciseTags, setExerciseTags] = useState([]);

  const handleExerciseTypeChange = (selectedType) => {
    setExerciseType(selectedType);
  };

  const xsrfToken = getXSRFtoken();

  callAPI("GET", "user/getUserData", null, null)
    .then(async response => {
      const userData = response.data;
      setIsAdmin((userData.user.role === "ADMIN"));
    }
    )
    .catch(error => {
      console.log(error);
      if(error.response && error.response.data === "Token expired"){
        navigate("/tokenExpired")
   }
    })

  useEffect(() => {
    callAPINoAuth("GET", "tag/getExerciseTags", null, null)
      .then(async (response) => {
        const exerciseTagsText = response.data;
        const exerciseTagsArray = exerciseTagsText
          .slice(1, -1)
          .split(', ')
          .map((item) => item.trim());
        console.log(exerciseTagsArray);
        setExerciseTags(exerciseTagsArray);
      })
      .catch(async (error) => {
        console.error("Failed to fetch exercise tags");
        console.error(error);
        if(error.response && error.response.data === "Token expired"){
          navigate("/tokenExpired")
     }
      })
  }, [])

  const addExercise = async (e) => {
    e.preventDefault();

    const formData = new FormData();

    if (exerciseType === "RUN") {
      formData.append('exerciseRequest', new Blob([JSON.stringify({

        name: name,
        name_eng: name_eng,
        description: description,
        description_eng: description_eng,
        type: exerciseType,
        length: length,
        category: category,
        tags: tags
      })], { type: 'application/json' }));

    } else if (exerciseType === "GYM") {
      formData.append('exerciseRequest', new Blob([JSON.stringify({

        name: name,
        name_eng: name_eng,
        description: description,
        description_eng: description_eng,
        type: exerciseType,
        series: series,
        repetitions: repetitions,
        tags: tags
      })], { type: 'application/json' }));

    } else if (exerciseType === "SWIMMING") {
      formData.append('exerciseRequest', new Blob([JSON.stringify({

        name: name,
        name_eng: name_eng,
        description: description,
        description_eng: description_eng,
        type: exerciseType,
        length: length,
        style: style,
        tags: tags
      })], { type: 'multipart' }));
    }

    formData.append('imageData', selectedFile, {type: 'multipart/form-data'});

    try {
      callAPIMultipartFile("POST", "exercise/addExercise", formData, xsrfToken)
        .then((response) => {
          const responseBody = response.data;
          console.log(responseBody);
        })
        .catch((error) => {
          console.error("Request failed with status:", error);
        })
    } catch (error) {
      console.error("Error:", error);
      if(error.response && error.response.data === "Token expired"){
              navigate("/tokenExpired")
         }
    }
  };

  const handleImageChange = (e) => {
    setSelectedFile(e.target.files[0])
    if (selectedFile) {
      const reader = new FileReader();

      reader.onload = (e) => {
        const base64Data = e.target.result;
        setPicture(base64Data);
      };

      reader.readAsDataURL(selectedFile);
    }
  };

  const renderExerciseForm = () => {
    switch (exerciseType) {
      case 'RUN':
        return (
          <>
            <TextField
              style={{ margin: '10px auto' }}
              sx={{ m: 1, width: '25ch' }}
              id="outlined-basic"
              label="length (m)"
              variant="outlined"
              fullWidth
              value={length}
              onChange={(e) => setLength(e.target.value)}
            />
            <FormHelperText>Run category</FormHelperText>
            <Select
              style={{ margin: '10px auto' }}
              labelId="demo-simple-select-label"
              id="demo-simple-select"
              value={category}
              label="category"
              sx={{ m: 1, width: '25ch' }}
              onChange={(e) => setCategory(e.target.value)}
            >
              <MenuItem value={"INTERVALS"}>Intervals</MenuItem>
              <MenuItem value={"TEMPO"}>tempo</MenuItem>
              <MenuItem value={"UPHILL"}>uphill</MenuItem>
              <MenuItem value={"ENDURANCE"}>endurance</MenuItem>
              <MenuItem value={"SPRINT"}>sprint</MenuItem>
            </Select>

          </>
        );
      case 'GYM':
        return (
          <> <TextField
            style={{ margin: '10px auto' }}
            sx={{ m: 1, width: '25ch' }}
            id="outlined-basic"
            label="repetitions"
            variant="outlined"
            fullWidth
            value={repetitions}
            onChange={(e) => setRepetitions(e.target.value)}
          />
            <TextField
              style={{ margin: '10px auto' }}
              sx={{ m: 1, width: '25ch' }}
              id="outlined-basic"
              label="series"
              variant="outlined"
              fullWidth
              value={series}
              onChange={(e) => setSeries(e.target.value)}
            /></>
        );
      case 'SWIMMING':
        return (
          <>

            <TextField
              style={{ margin: '10px auto' }}
              sx={{ m: 1, width: '25ch' }}
              id="outlined-basic"
              label="length (m)"
              variant="outlined"
              fullWidth
              value={length}
              onChange={(e) => setLength(e.target.value)}
            />

            <FormHelperText>Swimming style</FormHelperText>
            <Select
              style={{ margin: '10px auto' }}
              labelId="demo-simple-select-label"
              id="demo-simple-select"
              value={style}
              label="style"
              sx={{ m: 1, width: '25ch' }}
              onChange={(e) => setStyle(e.target.value)}
            >
              <MenuItem value={"BREASTSSTROKE"}>breasts stroke</MenuItem>
              <MenuItem value={"BUTTERFLYSTROKE"}>butterfly stroke</MenuItem>
              <MenuItem value={"BACKSTROKE"}>back stroke</MenuItem>
              <MenuItem value={"FRONTCRAWLSTROKE"}>front crawl stroke</MenuItem>
            </Select>




          </>
        );
      default:
        return null;
    }
  };

  return (
    <div>
      {isAdmin !== undefined ? (
        isAdmin ? (
          <Container>
            <Paper elevation={3} className='paper'>
              <form noValidate autoComplete="off">
                <h1>Add exercise</h1>

                <TextField
                  style={{ margin: '10px auto' }}
                  sx={{ m: 1, width: '25ch' }}
                  id="outlined-basic"
                  label="Name"
                  variant="outlined"
                  fullWidth
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                />

                <TextField
                  style={{ margin: '10px auto' }}
                  sx={{ m: 1, width: '25ch' }}
                  id="outlined-basic"
                  label="Name_eng"
                  variant="outlined"
                  fullWidth
                  value={name_eng}
                  onChange={(e) => setName_eng(e.target.value)}
                />

                <TextField
                  style={{ margin: '10px auto' }}
                  sx={{ m: 1, width: '25ch' }}
                  id="outlined-basic"
                  label="description"
                  variant="outlined"
                  fullWidth
                  value={description}
                  onChange={(e) => setDescription(e.target.value)}
                />

                <TextField
                  style={{ margin: '10px auto' }}
                  sx={{ m: 1, width: '25ch' }}
                  id="outlined-basic"
                  label="description_eng"
                  variant="outlined"
                  fullWidth
                  value={description_eng}
                  onChange={(e) => setDescription_eng(e.target.value)}
                />


                <FormHelperText>Exercise type</FormHelperText>
                <Select
                  style={{ margin: '10px auto' }}
                  sx={{ m: 1, width: '25ch' }}
                  select
                  label="Select Exercise Type"
                  variant="outlined"
                  fullWidth
                  value={exerciseType}
                  onChange={(e) => handleExerciseTypeChange(e.target.value)}
                >
                  <MenuItem value="RUN">Run</MenuItem>
                  <MenuItem value="GYM">Gym</MenuItem>
                  <MenuItem value="SWIMMING">Swimming</MenuItem>
                </Select>

                {renderExerciseForm()}

                <TagSelection pool={exerciseTags} maxSelection={2} />
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


                <Button variant="contained" onClick={addExercise}> Submit </Button>
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

  function TagSelection({ pool }) {

    const handleTagSelection = (tag) => {
      setTags((prevSelectedTags) => {
        if (prevSelectedTags.includes(tag)) {
          return prevSelectedTags.filter((item) => item !== tag);
        } else {
          return [...prevSelectedTags, tag];
        }
      });
    };

    console.log(tags);

    return (
      <div>
        <h1>Tag Selection</h1> <br />
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





