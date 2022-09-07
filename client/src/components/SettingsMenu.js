import * as React from "react";
import Menu from "@mui/material/Menu";
import MenuItem from "@mui/material/MenuItem";
import { IconButton } from "@mui/material";
import SettingsIcon from "@mui/icons-material/Settings";
import { useLocation, useNavigate } from "react-router-dom";
import axios from "axios";

const DATA_LIST_URL = "http://localhost:8080/";

export default function SettingsMenu() {
  const [anchorEl, setAnchorEl] = React.useState(null);
  const [roles, setroles] = React.useState([]);

  const open = Boolean(anchorEl);
  let location = useLocation();

  React.useEffect(() => {
    setroles(location.state.roles);
  }, [roles]);

  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleLogout = async () => {
    try {
      let lt = localStorage.getItem("accessToken");
      const response = await axios.get(DATA_LIST_URL + "authenticate/logout", {
        headers: {
          Authorization: JSON.parse(lt),
        },
      });

      if (response.status === 200) {
        navigate("/");
      }
    } catch (error) {
      console.log(error);
    }
  };
  const handleAddRole = () => {
    navigate("/addrole", { state: { roles: roles } });
  };
  const handleClose = () => {
    setAnchorEl(null);
  };
  let navigate = useNavigate();

  return (
    <div>
      <IconButton onClick={handleClick}>
        <SettingsIcon
          id="basic-button"
          aria-controls={open ? "basic-menu" : undefined}
          aria-haspopup="true"
          aria-expanded={open ? "true" : undefined}
          sx={{ color: "white" }}
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
        <MenuItem
          onClick={() => navigate("/adduser", { state: { roles: roles } })}
        >
          Add User
        </MenuItem>
        <MenuItem onClick={handleLogout}>Logout</MenuItem>
        {roles.includes("Admin") && (
          <MenuItem onClick={handleAddRole}>Add Role</MenuItem>
        )}
      </Menu>
    </div>
  );
}
