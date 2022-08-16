import {
  Box,
  Button,
  Container,
  CssBaseline,
  FormControl,
  Grid,
  InputLabel,
  Paper,
  Select,
  TextField,
  Toolbar,
  Typography,
} from "@mui/material";
import React, { useEffect, useState } from "react";
import { useLocation } from "react-router-dom";

export default function EmployeeEducation() {
  let location = useLocation();
  const [data, setdata] = useState({});

  useEffect(() => {
    setdata(location.state.data);
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
        <Container sx={{ display: "flex" }}>
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
          
            
          </Box>
          </Container>
      </Box>
    </>
  );
}
