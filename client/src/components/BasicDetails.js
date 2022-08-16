import {
  Box, Button, Checkbox, Container, FormControl,
  FormControlLabel, FormGroup, FormLabel, Grid, Radio,
  RadioGroup, TextField, Toolbar, Typography,
} from "@mui/material";

import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";
import { AdapterDateFns } from "@mui/x-date-pickers/AdapterDateFns";
import { LocalizationProvider } from "@mui/x-date-pickers/LocalizationProvider";
import Select from "react-select";
import countryList from "react-select-country-list";
import { useMemo } from "react";
import { DesktopDatePicker } from "@mui/x-date-pickers";
import axios from "axios";
const initialValues = {
  mobile: "",
  dob: "",
  gender: "female",
  hobby: "",
  country: "",
};

const ADD_BASIC_DETAILS = "http://localhost:8080/basicdetails";
const GET_BASIC_DETAILS = "http://localhost:8080/getbasicdetails/";

export default function BasicDetails() {
  let location = useLocation();
  const [data, setdata] = useState({});
  const [date, setDate] = React.useState(new Date().toISOString().slice(0, 10));
  const [nation, setNation] = useState("");
  const [fgender, setFgender] = useState("female");
  const options = useMemo(() => countryList().getData(), []);
  const [image, setimage] = useState(null);
  const [profile_image, setprofile_image] = useState(null);
  const [formData, setFormData] = useState({});
  const { mobile, dob, gender, hobby, country } = formData;
  const [hobbies, setHobbies] = React.useState({
    trading: false,
    coding: false,
    design: false,
    reading: false,
  });
  const { trading, coding, design, reading } = hobbies;


  const uploadHandler = (e) => {
    setimage(URL.createObjectURL(e.target.files[0]));
    setprofile_image(e.target.files[0])
  };

  const dropHandler = (newValue) => {
    setNation(newValue);
    setFormData({ ...formData, country: newValue.label });
  };

  const handleDateChange = (newValue) => {
    setDate(newValue.toISOString().slice(0, 10));
    setFormData({ ...formData, dob: newValue.toISOString().slice(0, 10) });
  };
  const handleGenderChange = (event) => {
    setFgender(event.target.value)
    setFormData({ ...formData, gender: event.target.value });
  };

  const handleChange = (event) => {
    setHobbies({
      ...hobbies,
      [event.target.name]: event.target.checked,
    });
    setfhobbies();
  };

  const setfhobbies = () => {
    console.log(hobbies)
    let keys = Object.keys(hobbies);

    let filtered = keys.filter(function (key) {
      return hobbies[key]
    });
    setFormData({ ...formData, hobby: filtered.toString() })
  }

  const onChange = (e) => {
    console.log(e.target.id);
    const { value, id } = e.target;
    setFormData({ ...formData, [id]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    let totaldata = new FormData();
    totaldata.append('employeedata', JSON.stringify(formData));
    totaldata.append('profile', profile_image)
    for (var value of totaldata.values()) {
      console.log(value);
    }
    let lt = localStorage.getItem("accessToken");
    try {
      const response = await axios.post(ADD_BASIC_DETAILS, totaldata, {
        headers: {

          Authorization: JSON.parse(lt)
        }
      })
      console.log(response)
      setFormData(initialValues);
      setimage(null);
      setHobbies({ trading: false, coding: false, design: false, reading: false, })
    } catch (e) {
      console.log(e);
    }
  };

  const getBasicDetails = async () => {
    let lt = localStorage.getItem("accessToken");
    let x = location.state.data.id;
    try {
      const response = await axios.get(GET_BASIC_DETAILS + x, {
        headers: {
          Authorization: JSON.parse(lt)
        }
      })
      if (response.status === 200) {
        console.log(response.data)
        setDate(response.data.dob)
        setFormData({ ...formData, mobile: response.data.mobile, gender: response.data.gender })
        setNation(response.data.country)
        setFgender(response.data.gender)
        const nat = options.filter(val => val.label === response.data.country)
        setNation(nat[0])
        const subhobby = response.data.hobby
        if (subhobby.includes('trading')) {
          setHobbies({ ...hobbies, trading: true })
        }
        if (subhobby.includes('coding')) {
          setHobbies({ ...hobbies, coding: true })
        }
        if (subhobby.includes('design')) {
          setHobbies({ ...hobbies, design: true })
        }
        if (subhobby.includes('reading')) {
          setHobbies({ ...hobbies, reading: true })
        }
        setimage(`data:image/jpeg;base64,${response.data.profile}`)
      }
    } catch (e) {
      console.log(e)
    }
  }

  useEffect(() => {
    setdata(location.state.data);
    setFormData({ ...formData, empId: location.state.data.id })
    getBasicDetails();
  }, []);


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
        <form onSubmit={handleSubmit}>
          <Container sx={{ display: "flex" }} spacing={2}>
            <Box sx={{ mt: 8, ml: 4 }}>
              <Grid container spacing={2}>
                <Grid item xs={6}>
                  <TextField
                    autoComplete="firstName"
                    name="firstName"
                    value={data.firstName}
                    required
                    fullWidth
                    id="firstName"
                    label="First Name"
                    disabled="true"
                  ></TextField>
                </Grid>
                <Grid item xs={6}>
                  <TextField
                    autoComplete="lastName"
                    name="lastName"
                    value={data.lastName}
                    required
                    fullWidth
                    id="lastName"
                    label="Last Name"
                    disabled="true"
                  ></TextField>
                </Grid>
              </Grid>
              <Grid container spacing={1}>
                <Grid item xs={12} sx={{ mt: 4 }}>
                  <TextField
                    autoComplete="email"
                    name="email"
                    value={data.email}
                    required
                    fullWidth
                    id="email"
                    label="Email"
                    disabled="true"
                  ></TextField>
                </Grid>
              </Grid>

              <Grid container spacing={1}>
                <Grid item xs={12} sx={{ mt: 4 }}>
                  <TextField
                    autoComplete="id"
                    name="id"
                    value={data.id}
                    required
                    fullWidth
                    id="id"
                    label="Employee ID"
                    disabled="true"
                  ></TextField>
                </Grid>
              </Grid>
              <Grid container spacing={1}>
                <Grid item xs={12} sx={{ mt: 4 }}>
                  <TextField
                    autoComplete="joiningdate"
                    name="joiningdate"
                    value={data.date}
                    fullWidth
                    id="joiningdate"
                    label="Joining Date"
                    disabled="true"
                  ></TextField>
                </Grid>
              </Grid>
              <Grid container spacing={2}>
                <Grid item xs={12} sx={{ mt: 4 }}>
                  <TextField
                    autoComplete="mobile"
                    name="mobile"
                    fullWidth
                    id="mobile"
                    label="Mobile"
                    value={mobile || ""}
                    onChange={(e) => onChange(e)}
                  ></TextField>
                </Grid>
              </Grid>
            </Box>
            <Box sx={{ mt: 8, ml: 12 }}>
              <Grid container spacing={1}>
                <Grid item xs={12}>
                  <LocalizationProvider dateAdapter={AdapterDateFns}>
                    <DesktopDatePicker
                      label="DOB"
                      inputFormat="dd/MM/yyyy"
                      value={date}
                      onChange={handleDateChange}
                      renderInput={(params) => <TextField {...params} />}
                    />
                  </LocalizationProvider>
                </Grid>
                <Grid item xs={12} sx={{ mt: 2 }}>
                  <FormControl>
                    <FormLabel id="demo-radio-buttons-group-label">
                      Gender
                    </FormLabel>
                    <RadioGroup
                      row
                      aria-labelledby="demo-radio-buttons-group-label"
                      defaultValue="female"
                      name="radio-buttons-group"
                      value={fgender}
                      onChange={handleGenderChange}
                    >
                      <FormControlLabel
                        value="female"
                        control={<Radio />}
                        label="Female"
                      />
                      <FormControlLabel
                        value="male"
                        control={<Radio />}
                        label="Male"
                      />
                      <FormControlLabel
                        value="other"
                        control={<Radio />}
                        label="Other"
                      />
                    </RadioGroup>
                  </FormControl>
                </Grid>
                <Grid item xs={12} sx={{ mt: 2 }}>
                  <FormControl component="fieldset" variant="standard">
                    <FormLabel component="legend">Hobbies</FormLabel>
                    <FormGroup>
                      <Grid container spacing={2}>
                        <Grid item xs={6}>
                          <FormControlLabel
                            control={
                              <Checkbox
                                checked={trading}
                                onChange={handleChange}
                                name="trading"
                              />
                            }
                            label="Trading"
                          />
                        </Grid>
                        <Grid item xs={6}>
                          <FormControlLabel
                            control={
                              <Checkbox
                                checked={coding}
                                onChange={handleChange}
                                name="coding"
                              />
                            }
                            label="Coding"
                          />
                        </Grid>
                      </Grid>
                      <Grid container spacing={2}>
                        <Grid item xs={6}>
                          <FormControlLabel
                            control={
                              <Checkbox
                                checked={design}
                                onChange={handleChange}
                                name="design"
                              />
                            }
                            label="UI/UX Design"
                          />
                        </Grid>
                        <Grid item xs={6}>
                          <FormControlLabel
                            control={
                              <Checkbox
                                checked={reading}
                                onChange={handleChange}
                                name="reading"
                              />
                            }
                            label="Reading"
                          />
                        </Grid>
                      </Grid>
                    </FormGroup>
                  </FormControl>
                </Grid>
                <Grid item xs={6} sx={{ mt: 2 }}>
                  <Typography>Select Country</Typography>
                  <Select
                    options={options}
                    value={nation}
                    onChange={dropHandler}
                  />
                </Grid>
              </Grid>
              <Grid>
                <Grid item xs={6} sx={{ mt: 3 }}>
                  <Button variant="outlined" component="label">
                    Upload Profile
                    <input type="file" hidden onChange={uploadHandler}></input>
                  </Button>
                </Grid>
              </Grid>
              <Grid item xs={3} sx={{ mt: -54, ml: 50 }}>
                {image && (
                  <>
                    <img
                      src={image}
                      alt="preview"
                      height="150px"
                      width="150px"
                      style={{ display: "inline" }}
                    ></img>
                    <Typography>Profile Preview</Typography>
                  </>
                )}
              </Grid>
            </Box>
          </Container>
          <Button
            type="submit"
            variant="contained"
            sx={{ mt: 5, ml: 50, width: "200px" }}
          >
            Submit
          </Button>
        </form>
      </Box>
    </>
  );
}
