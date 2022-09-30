import { Card, CardContent, Typography, Box, Toolbar, Dialog, DialogTitle, DialogContent, DialogContentText, TextField, Button, DialogActions } from "@mui/material";
import { clear } from "@testing-library/user-event/dist/clear";
import axios from "axios";
import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";

const DATA_LIST_URL = "http://localhost:8080/";
const EMPLOYEE_SKILLS_LIST = "http://localhost:8080/skills/getskills/"
const ADD_EMPLOYEE_SKILLS = "http://localhost:8080/skills/addskill"
const DELETE_EMPLOYEE_SKILLS = "http://localhost:8080/skills/deleteskills/"
const initialValue = { languages: '', frameworks: '', tools: '', qualificaion: '', projects: '' }
//const lang = { id: 2, languages: 'python java'}

export default function EmployeeDetail() {

  const [open, setOpen] = React.useState(false);
  const [skill, setSkill] = React.useState()
  const [data, setData] = React.useState({})
  const [fieldvalue, setFieldvalue] = React.useState();
  const location = useLocation();
  const { params } = location.state;
  const [employee, setemployee] = useState({});
  const [skills, setSkills] = useState({});

  const handleClickOpen = (skill) => {
    setSkill(skill);
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    clear();
  };

  const handleAdd = async () => {
    let x = 0;
    console.log(skill)
    //await setData({...data, id: params, [skill]:fieldvalue})
    console.log(data)
    addEmpSkills();
    handleClose();
  }

  

  React.useEffect(() => {
    if(fieldvalue != "" || fieldvalue != null){
    setData({...data, id: params, [skill]:fieldvalue})
    }
  }, [fieldvalue])

  const onChange = (value) => {
    setFieldvalue(value)
  } 

  const clear = () => {
    setSkill();
    setData({});
    setFieldvalue()
  }

  useEffect(() => {
    getEmployeeSkills();
    //addEmpSkills();
    if (params !== undefined || params !== null) {
      findByid();
    }
  }, []);

  const addEmpSkills = async () => {
    try {
      let lt = localStorage.getItem("accessToken");
      const response = await axios.post(ADD_EMPLOYEE_SKILLS, data, {
        headers: {
          Authorization: JSON.parse(lt),
        },
      })
      console.log(response);
      if(response.status === 202){
        getEmployeeSkills();
      }
    } catch (error) {

    }
  }

  const getEmployeeSkills = async () => {
    let lt = localStorage.getItem("accessToken");
    const response = await axios.get(EMPLOYEE_SKILLS_LIST + params, {
      headers: {
        Authorization: JSON.parse(lt),
      },
    });
    console.log(response.data)
    if(response.status === 200){
      setSkills(response.data)
    }
    
  }
  const findByid = async () => {
    try {
      let lt = localStorage.getItem("accessToken");
      const response = await axios.get(DATA_LIST_URL + "searchbyid/" + params, {
        headers: {
          Authorization: JSON.parse(lt),
        },
      });

      setemployee(response.data);
    } catch (error) { }
  };

  const deleteskills = async(e) => {
    e.preventDefault();
    try {
      let lt = localStorage.getItem("accessToken");
      const response = await axios.delete(DELETE_EMPLOYEE_SKILLS + params, {
        headers: {
          Authorization: JSON.parse(lt),
        },
      })
      if(response.status === 202){
        console.log("dlete")
        setSkills()
        getEmployeeSkills();
        
      }
    } catch (error) {
      
    }
  }

  return (
    <>
      <Box
        component="main"
        sx={{
          flexGrow: 1,
          height: "100vh",
          overflow: "auto",
        }}
      >
        <Toolbar />
        <Box sx={{ marginLeft: 7, marginTop: 5 }}>
          <Typography
            sx={{ fontSize: 18 }}
            color="text.secondary">
            Employee Name : {employee.firstName} {employee.lastName}
          </Typography>
        </Box>
        {/* {!skills && */}
        <Box sx={{marginLeft: 85, marginBottom: -2}}>
          <Button variant="outlined" onClick={deleteskills}>Delete All</Button>
        </Box>
        {/* } */}
        {params && (
          <div style={{ marginTop: "40px", marginLeft: "50px" }}>
            <Box m="auto">
              <Card sx={{ maxWidth: 750 }}>
                <CardContent>
                  <Typography
                    sx={{ fontSize: 14 }}
                    color="text.secondary"
                    gutterBottom
                  >
                    Programming Languages:
                  </Typography>
                  <Typography variant="h6" component="div">
                    {skills?.languages ? skills.languages : "No data here add Something"}
                  </Typography>
                  {!skills?.languages &&
                  <Button variant="outlined" onClick={()=>handleClickOpen('languages')} sx={{marginLeft: 80, marginTop: -13}}>
                    Add
                  </Button>}
                </CardContent>
              </Card>
              <Card sx={{ maxWidth: 750, marginTop: 3 }}>
                <CardContent>
                  <Typography
                    sx={{ fontSize: 14 }}
                    color="text.secondary"
                    gutterBottom
                  >
                    Frameworks Known:
                  </Typography>
                  <Typography variant="h6" component="div">
                    {skills?.frameworks ? skills.frameworks : "No data here add Something"}
                  </Typography>
                  {!skills?.frameworks &&
                  <Button variant="outlined" onClick={()=> handleClickOpen('frameworks')} sx={{marginLeft: 80, marginTop: -13}}>
                    Add
                  </Button>}
                </CardContent>
              </Card>
              <Card sx={{ maxWidth: 750, marginTop: 3 }}>
                <CardContent>
                  <Typography
                    sx={{ fontSize: 14 }}
                    color="text.secondary"
                    gutterBottom
                  >
                    Projects:
                  </Typography>
                  <Typography variant="h6" component="div">
                    {skills?.projects ? skills.projects : "No data here add Something"}
                  </Typography>
                  {!skills?.projects &&
                  <Button variant="outlined" onClick={()=> handleClickOpen('projects')} sx={{marginLeft: 80, marginTop: -13}}>
                    Add
                  </Button>}
                </CardContent>
              </Card>
              <Card sx={{ maxWidth: 750, marginTop: 3 }}>
                <CardContent>
                  <Typography
                    sx={{ fontSize: 14 }}
                    color="text.secondary"
                    gutterBottom
                  >
                    Tools familiar with
                  </Typography>
                  <Typography variant="h6" component="div">
                    {skills?.tools ? skills.tools : "No data here add Something"}
                  </Typography>
                  {!skills?.tools &&
                  <Button variant="outlined" onClick={()=> handleClickOpen('tools')} sx={{marginLeft: 80, marginTop: -13}}>
                    Add
                  </Button>}
                </CardContent>
              </Card>
              <Card sx={{ maxWidth: 750, marginTop: 3, marginBottom: 2 }}>
                <CardContent>
                  <Typography
                    sx={{ fontSize: 14 }}
                    color="text.secondary"
                    gutterBottom
                  >
                    Educational Qualification:
                  </Typography>
                  <Typography variant="h6" component="div">
                    {skills?.qualification ? skills.qualification : "No data here add Something"}
                  </Typography>
                  {!skills?.qualification &&
                  <Button variant="outlined" onClick={()=>handleClickOpen("qualification")} sx={{marginLeft: 80, marginTop: -13}}>
                    Add
                  </Button>}
                </CardContent>
              </Card>
            </Box>
          </div>
        )}
      </Box>
      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>Add Skills</DialogTitle>
        <DialogContent>
          
          <TextField
            autoFocus
            margin="dense"
            id="name"
            fullWidth
            variant="outlined"
            value={fieldvalue}
            onChange = {(e) => {
              onChange(e.target.value)
            }}
            //sx = {{Width: 200}}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleAdd}>Add</Button>
        </DialogActions>
      </Dialog>
    </>
  );
}
