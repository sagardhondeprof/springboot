import * as React from "react";
import Menu from "@mui/material/Menu";
import MenuItem from "@mui/material/MenuItem";
import { Button, IconButton } from "@mui/material";
import AccountCircleIcon from "@mui/icons-material/AccountCircle";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";
import ModeEditOutlineIcon from "@mui/icons-material/ModeEditOutline";

const DATA_LIST_URL = "http://localhost:8080/";
const UPDATE_USER_PROFILE =" http://localhost:8080/registration/uploadprofile"

export default function Profile({profile}) {
  const [anchorEl, setAnchorEl] = React.useState(null);
  const [roles, setroles] = React.useState([]);
  const [image, setimage] = React.useState("assets/profile.jpg");

  const open = Boolean(anchorEl);
  let location = useLocation();

  React.useEffect(() => {
    setroles(location.state.roles);
    setimage(`data:image/jpeg;base64,${profile}`);
  }, [roles]);

  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleUpadteProfile = async (e) => {
    setimage(URL.createObjectURL(e.target.files[0]));
    let totaldata = new FormData();
    let username = localStorage.getItem("username");
    totaldata.append("username", username);
    totaldata.append("profile", e.target.files[0]);
    let lt = localStorage.getItem("accessToken");
    try {
      const response = await axios.post(UPDATE_USER_PROFILE, totaldata, {
        headers: {
          Authorization: JSON.parse(lt),
        },
      });
    } catch (e) {
      console.log(e);
    }
  };

  const handleClose = () => {
    setAnchorEl(null);
  };
  let navigate = useNavigate();

  return (
    <div>
      <IconButton onClick={handleClick}>
        <img
          src={image}
          alt="profile"
          height="30px"
          width="30px"
          style={{ borderRadius: 33 }}
        />
      </IconButton>
      <Menu
        id="basic-menu"
        anchorEl={anchorEl}
        open={open}
        onClose={handleClose}
        MenuListProps={{
          "aria-labelledby": "basic-button",
        }}
      >
        <MenuItem>
          Logged In as: <br></br>
          Sagar
        </MenuItem>

        <MenuItem>
          <Button component="label">
            Upload Profile
            <input type="file" hidden onChange={handleUpadteProfile}></input>
          </Button>
        </MenuItem>
      </Menu>
    </div>
  );
}
