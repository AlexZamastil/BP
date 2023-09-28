import { Paper, Button, TextField} from '@mui/material';
import { useEffect, useState } from 'react';
import { Container} from '@mui/system';
import { splitFieldInternalAndForwardedProps } from '@mui/x-date-pickers/internals';

export default function AddExercise(){
  const [name,setName] = useState(null);
  const [name_eng,setName_eng] = useState(null);
  const [description,setDescription] = useState(null);
  const [description_eng,setDescription_eng] = useState(null);
  const [type,setType] = useState(null);
  const [category_style,setCategory_Style] = useState(null);
  const [length,setLength] = useState(null);
  const [series,setSeries] = useState(null);
  const [repetitions,setRepetitions] = useState(null);
  const [tags,setTags] = useState([]);
  const [picture, setPicture] = useState(null);

  const [userStats,setUserStats] = useState([]);

  const [SFILE,setSelectedFile] = useState(null);

  const [isAdmin, setIsAdmin] = useState(false);

  const [exerciseTags, setExerciseTags] = useState([]); 
    useEffect(() => {
        fetch("http://localhost:8080/api/authorized/user/getuserdata", {
            method: "GET",
            headers: {
                'Authorization': localStorage.getItem("token")
            }
        })
        .then(async (response) => {
            const userData = await response.json();
            setUserStats(userData);
            setIsAdmin(userData.user.adminPrivileges);
        })
        .catch(error => {
            console.error(error);
        });
    }, []);

    useEffect(() => {
      fetch("http://localhost:8080/api/nonauthorized/getExerciseTags", {
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
    
      formData.append('addExerciseRequest', new Blob([JSON.stringify({
        name: name,
        name_eng: name_eng,
        description: description,
        description_eng: description_eng,
        series: series,
        repetitions: repetitions,
        tags: tags
      })], { type: 'application/json' }));
    
   
      formData.append('imageData', SFILE);
    
      try {
        const response = await fetch("http://localhost:8080/api/privileged/addExercise", {
          method: "POST",
          headers: {
            'Authorization': localStorage.getItem("token"),
          },
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
      if (SFILE) {
        const reader = new FileReader();
    
        reader.onload = (e) => {
          const base64Data = e.target.result;
          setPicture(base64Data);
  
          addExercise(e, SFILE);
        };
    
        reader.readAsDataURL(SFILE);
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
                  <TextField
                   style={{ margin: '10px auto' }}
                   sx={{ m: 1, width: '25ch' }}
                   id="outlined-basic"
                   label="type"
                   variant="outlined"
                   fullWidth
                   value={type}
                   onChange={(e) => setType(e.target.value)}
                 />
                  <TextField
                   style={{ margin: '10px auto' }}
                   sx={{ m: 1, width: '25ch' }}
                   id="outlined-basic"
                   label="category_style"
                   variant="outlined"
                   fullWidth
                   value={category_style}
                   onChange={(e) => setCategory_Style(e.target.value)}
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
                  <TextField
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
                 />
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
    }
    
    
    
    
    
 