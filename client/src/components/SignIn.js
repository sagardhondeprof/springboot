import * as React from "react";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import CssBaseline from "@mui/material/CssBaseline";
import TextField from "@mui/material/TextField";
import { Link, useNavigate } from "react-router-dom";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import { useState } from "react";
import axios from "axios";

const theme = createTheme();

export default function SignIn() {
  const [userName, setEmail] = useState("");
  const [userPassword, setuserPassword] = useState("");
  const [userNameError, setuserNameError] = useState(false);
  const [userPasswordError, setuserPasswordError] = useState(false);

  let navigate = useNavigate();

  // React.useEffect(() => {
  //   userName.trim().length === 0
  //     ? setuserNameError(true)
  //     : setuserNameError(false);
  // }, [userName]);

  const handleSubmit = (event) => {
    event.preventDefault();
    let emailError = false;
    let passError = false;

    emailError = userName.trim().length === 0 ? true : false;
    passError = userPassword.trim().length === 0 ? true : false;

    let er = false;

    if (emailError || passError) er = true;
    else er = false;

    emailError && setuserNameError(true);
    passError && setuserPasswordError(true);

    if (!er) {
      let data = {
        userName,
        userPassword,
      };

      axios
        .post("http://localhost:8080/authenticate", data)
        .then((response) => {
          // if (response.status === 200) {
          //console.log(response.data, " response");
          localStorage.setItem(
            "accessToken",
            JSON.stringify("LoginToken " + response?.data?.jwtToken)
          );
          //console.log(response.data.roles);
          navigate("/datatable", {state:{roles:response.data.roles}});
          // }
        })
        .catch((error) => {
          console.error();
        });
    }
  };

  return (
    <ThemeProvider theme={theme}>
      <Container component="main" maxWidth="xs">
        <CssBaseline />
        <Box
          sx={{
            marginTop: 8,
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
          }}
        >
          <Avatar sx={{ m: 1, bgcolor: "secondary.main" }}>
            <LockOutlinedIcon />
          </Avatar>
          <Typography component="h1" variant="h5">
            Sign in
          </Typography>
          <Box
            component="form"
            onSubmit={handleSubmit}
            noValidate
            sx={{ mt: 1 }}
          >
            <TextField
              margin="normal"
              required
              fullWidth
              id="Username"
              label="Username"
              name="email"
              autoComplete="Username"
              value={userName}
              onChange={(e) => {
                setEmail(e.target.value);
                setuserNameError(false);
              }}
              onBlur={(e) => {
                if (!e.target.value) setuserNameError(true);
              }}
              autoFocus
              error={userNameError}
              helperText={"enter Username"}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="userPassword"
              label="userPassword"
              type="password"
              id="userPassword"
              value={userPassword}
              onChange={(e) => {
                setuserPassword(e.target.value);
                setuserPasswordError(false);
              }}
              onBlur={(e) => {
                if (!e.target.value) setuserPasswordError(true);
              }}
              autoComplete="current-userPassword"
              error={userPasswordError}
              helperText={"enter userPassword"}
            />
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Sign In
            </Button>
            <Grid container>
              <Grid item xs>
                <Link
                  to="#"
                  variant="body2"
                  style={{ textDecoration: "none", fontWeight: "bold" }}
                >
                  Forgot userPassword?
                </Link>
              </Grid>
              <Grid item>
                <Link
                  to="/signup"
                  variant="body2"
                  style={{ textDecoration: "none", fontWeight: "bold" }}
                >
                  SignUp here
                </Link>
              </Grid>
            </Grid>
          </Box>
        </Box>
      </Container>
    </ThemeProvider>
  );
}
