import { Paper, Button, TextField} from '@mui/material';
import { useEffect, useState } from 'react';
import { Container} from '@mui/system';
import InputLabel from '@mui/material/InputLabel';
import Select from '@mui/material/Select';
import MenuItem from '@mui/material/MenuItem';

export default function AddExercise(){
  const [name,setName] = useState(null);
  const [name_eng,setName_eng] = useState(null);
  const [description,setDescription] = useState(null);
  const [description_eng,setDescription_eng] = useState(null);
  const [type,setType] = useState(null);
  const [category,setCategory] = useState(null);
  const [style,setStyle] = useState(null);
  const [length,setLength] = useState(null);
  const [series,setSeries] = useState(null);
  const [repetitions,setRepetitions] = useState(null);
  const [tags,setTags] = useState([]);
  const [picture, setPicture] = useState(null);
  const [exerciseType, setExerciseType] = useState('');

  //const [userStats,setUserStats] = useState([]);

  const [selectedFile,setSelectedFile] = useState(null);

  const [isAdmin, setIsAdmin] = useState(false);

  const [exerciseTags, setExerciseTags] = useState([]); 

  const [csrfToken,setCsrfToken] = useState("");

  const handleExerciseTypeChange = (selectedType) => {
    setExerciseType(selectedType);
  };

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
        .then(async (response) => {
            const userData = await response.json();
            //setUserStats(userData);
            setIsAdmin(userData.user.adminPrivileges);
        })
        .catch(error => {
            console.error(error);
        });
    }, []);

    useEffect(() => {
      fetch("https://localhost:8443/api/nonauthorized/getExerciseTags", {
        method: "GET",
        headers: {
          'Authorization': ""
        }
      })
      .then(async (response) => {
        if (response.ok) {
          const exerciseTagsText = await response.text();
          const exerciseTagsArray = exerciseTagsText
            .slice(1, -1) 
            .split(', ')
            .map((item) => item.trim()); 
  
          console.log(exerciseTagsArray);
          setExerciseTags(exerciseTagsArray);
        } else {
          console.error("Failed to fetch exercise tags");
        }
      })
      .catch(error => {
        console.error(error);
      });
    }, []);

    const addExercise = async (e) => {
      e.preventDefault();
    
 
      const formData = new FormData();

      if (exerciseType == "RUN") {
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

      } else if (exerciseType == "GYM") {
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

      } else if (exerciseType == "SWIMMING") {
        formData.append('exerciseRequest', new Blob([JSON.stringify({

          name: name,
          name_eng: name_eng,
          description: description,
          description_eng: description_eng,
          type: exerciseType,
          length: length,
          style: style,
          tags: tags
        })], { type: 'application/json' }));
      }

      formData.append('imageData', selectedFile);
    
      try {
        const response = await fetch("https://localhost:8443/api/privileged/addExercise", {
          method: "POST",
          headers: {
            'Authorization': localStorage.getItem("token"),
            'X-XSRF-TOKEN': csrfToken 
          },credentials : "include",
          body: formData 
        });
    
        if (response.ok) {
          const responseBody = await response.text();
          console.log(responseBody);
        } else {
          console.error("Request failed with status:", response.status);
        }
      } catch (error) {
        console.error("Error:", error);
      }
    };
    const handleImageChange = (e) => {
       setSelectedFile(e.target.files[0])    
      if (selectedFile) {
        const reader = new FileReader();
    
        reader.onload = (e) => {
          const base64Data = e.target.result;
          setPicture(base64Data);
  
          addExercise(e, base64Data);
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
          <TextField
                   style={{ margin: '10px auto' }}
                   sx={{ m: 1, width: '25ch' }}
                   id="outlined-basic"
                   label="category_style"
                   variant="outlined"
                   fullWidth
                   value={category}
                   onChange={(e) => setCategory(e.target.value)}
                 />

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
                   label="category_style"
                   variant="outlined"
                   fullWidth
                   value={style}
                   onChange={(e) => setStyle(e.target.value)}
                 />
            
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


                <InputLabel id="demo-simple-select-label"> Exercise type </InputLabel>
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

                  <TagSelection pool={exerciseTags} maxSelection={2}/>
                  <br/>

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

      function TagSelection({pool}) {
      
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
            <h1>Tag Selection</h1> <br/>
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
    
    
    
    
    
 