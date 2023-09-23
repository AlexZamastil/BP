import { Paper, Button, TextField} from '@mui/material';
import { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Link} from "react-router-dom"
import { Container} from '@mui/system';
import { useNavigate} from "react-router-dom"

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

  const [userStats,setUserStats] = useState([]);

  const [isAdmin, setIsAdmin] = useState(false);


    const navigate = useNavigate();

    const {t} = useTranslation();

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
    

    const addExercise =(e)=>{
        e.preventDefault();
        fetch("http://localhost:8080/api/privileged/addExercise",{
            method:"POST",
            headers:{
              'Authorization': localStorage.getItem("token"),
                "Content-Type":"application/json"},
                body : JSON.stringify({
                    name : name,
                    name_eng : name_eng,
                    description : description,
                    description_eng : description_eng,
                    series : series,
                    repetitions : repetitions,
                    tags : tags
                })
            })
    }
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
    }
    
    
    
    
    
 