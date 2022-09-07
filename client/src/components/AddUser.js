import * as React from "react";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import CssBaseline from "@mui/material/CssBaseline";
import TextField from "@mui/material/TextField";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import axios from "axios";
import { useState } from "react";
import {
  Alert,
  FormControl,
  InputLabel,
  MenuItem,
  OutlinedInput,
  Select,
} from "@mui/material";

const ADD_USER = "http://localhost:8080/registration/adduser";
const ROLES_LIST_URL = "http://localhost:8080/roles/getroles";

const theme = createTheme();
const ITEM_HEIGHT = 48;
const ITEM_PADDING_TOP = 8;
const MenuProps = {
  PaperProps: {
    style: {
      maxHeight: ITEM_HEIGHT * 4.5 + ITEM_PADDING_TOP,
      width: 250,
    },
  },
};
function getStyles(name, personName, theme) {
  return {
    fontWeight:
      personName.indexOf(name) === -1
        ? theme.typography.fontWeightRegular
        : theme.typography.fontWeightMedium,
  };
}

export default function SignUp() {
  const [displayName, setDisplayName] = useState("");
  const [userName, setUserName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [mobileNumber, setMobileNumber] = useState("");
  const [roles, setRoles] = useState([]);
  const [roledata, setroledata] = useState([]);
  const [isSubmit, setIsSubmit] = useState(false);
  const [formErrors, setFormErrors] = useState({});
  const [userTakenError, setuserTakenError] = useState("");

  const validate = (values) => {
    let errors = {};
    const regex = /^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/i;
    if (!values.userName) {
      errors["userName"] = "First Name is required";
    }
    if (!values.displayName) {
      errors.displayName = "Last Name is required";
    }
    if (!values.email) {
      errors.email = "Email is required";
    } else if (!regex.test(values.email)) {
      errors.email = "Email is not valid";
    }
    if (!values.password) {
      errors.password = "Password is required";
    } else if (values.password < 6) {
      errors.password = "Password is too short";
    }
    if (!values.mobileNumber) {
      errors.mobileNumber = "Mobile no. is required";
    } else if (values.mobileNumber < 10) {
      errors.mobileNumber = "incorrect";
    }
    return errors;
  };

  //handle submit

  const handleSubmit = async (e) => {
    e.preventDefault();
    let data = {
      userName,
      displayName,
      email,
      password,
      mobileNumber,
      roles,
    };
    let errorObj = validate(data);
    setFormErrors(errorObj);

    if (Object.keys(errorObj).length === 0) {
      let lt = localStorage.getItem("accessToken");

      //data should be added as parameter after url not in "body:"
      axios
        .post(ADD_USER, data, {
          headers: {
            Authorization: JSON.parse(lt),
          },
        })
        .then((response) => {
          if (response.status === 201) {
            setIsSubmit(true);
            setuserTakenError("");
          }
        })
        .catch((error) => {
          if (error.response?.status === 409) {
            console.log(error, "error");
            setuserTakenError("Username already Taken");
            setIsSubmit(false);
          }
          console.error();
        });
    }
  };
  const handleRolesChange = (event) => {
    const {
      target: { value },
    } = event;
    setRoles(
      // On autofill we get a stringified value.
      typeof value === "string" ? value.split(",") : value
    );
  };

  const fectRoles = async () => {
    try {
      let lt = localStorage.getItem("accessToken");
      const response = await axios.get(ROLES_LIST_URL, {
        headers: {
          Authorization: JSON.parse(lt),
        },
      });
      setroledata(response.data);
      if ((await response).status === 202) {
        setroledata(response.data.map((item) => item.roleName));
      }
    } catch (error) {
      console.log(error);
    }
  };

  React.useEffect(() => {
    fectRoles();
  }, []);

  return (
    <ThemeProvider theme={theme}>
      <div>
        {Object.keys(formErrors).length === 0 && isSubmit ? (
          <Alert
            severity="success"
            color="success"
            style={{ marginTop: "50px" }}
          >
            User Added Successfully !
          </Alert>
        ) : (
          <p></p>
        )}
      </div>
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
            Add User
          </Typography>
          <Box
            component="form"
            noValidate
            onSubmit={handleSubmit}
            sx={{ mt: 3 }}
          >
            <Grid container spacing={2}>
              <Grid item xs={12} sm={6}>
                <TextField
                  autoComplete="UserName"
                  name="UserName"
                  value={userName}
                  onChange={(e) => setUserName(e.target.value)}
                  required
                  fullWidth
                  id="UserName"
                  label="UserName"
                  autoFocus
                />
                <p style={{ color: "red" }}>{formErrors.userName}</p>
                <p style={{ color: "red" }}>{userTakenError}</p>
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  required
                  fullWidth
                  id="displayName"
                  label="displayName"
                  name="displayName"
                  autoComplete="displayName"
                  value={displayName}
                  onChange={(e) => setDisplayName(e.target.value)}
                />
                <p style={{ color: "red" }}>{formErrors.displayName}</p>
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  id="email"
                  label="Email Address"
                  name="email"
                  autoComplete="email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                />
                <p style={{ color: "red" }}>{formErrors.email}</p>
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name="password"
                  label="Password"
                  type="password"
                  id="password"
                  autoComplete="new-password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
                <p style={{ color: "red" }}>{formErrors.password}</p>
              </Grid>
              <Grid item xs={12}>
                <TextField
                  required
                  fullWidth
                  name="mobileNumber"
                  label="Mobile Number"
                  type="tel"
                  id="mobileNumber"
                  autoComplete="mobileNumber"
                  value={mobileNumber}
                  onChange={(e) => setMobileNumber(e.target.value)}
                />
                <p style={{ color: "red" }}>{formErrors.mobileNumber}</p>
              </Grid>
              <Grid item xs={12}>
                <FormControl sx={{ m: 1, width: 300 }}>
                  <InputLabel id="roles">Roles</InputLabel>
                  <Select
                    labelId="roles"
                    id="roles"
                    multiple
                    value={roles}
                    onChange={handleRolesChange}
                    input={<OutlinedInput label="Roles" />}
                    MenuProps={MenuProps}
                  >
                    {roledata.map((name) => (
                      <MenuItem
                        key={name}
                        value={name}
                        style={getStyles(name, roles, theme)}
                      >
                        {name}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
              </Grid>
            </Grid>
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
            >
              Add User
            </Button>
          </Box>
        </Box>
      </Container>
    </ThemeProvider>
  );
}
