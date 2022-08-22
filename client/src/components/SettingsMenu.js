import * as React from 'react';
import Menu from '@mui/material/Menu';
import MenuItem from '@mui/material/MenuItem';
import { IconButton } from '@mui/material';
import SettingsIcon from '@mui/icons-material/Settings';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const DATA_LIST_URL = "http://localhost:8080/";

export default function SettingsMenu() {
  const [anchorEl, setAnchorEl] = React.useState(null);
  const open = Boolean(anchorEl);
  
  const handleClick = (event) => {
    setAnchorEl(event.currentTarget);
  };
  const handleLogout = async () => {

    //let tocken = localStorage.getItem("accessToken");

    try {
      let lt = localStorage.getItem("accessToken");
      const response = await axios.get(DATA_LIST_URL + 'authenticate/logout' ,{
        headers: {
          Authorization: JSON.parse(lt)
        }
      })
      
      if(response.status === 200){
        //localStorage.clear()
        navigate("/")
      }
    } catch (error) {
      console.log(error)
      
    }
  }
  const handleAddRole =() =>{
    navigate("/addrole")
  }
  const handleClose = () => {
    setAnchorEl(null);
  };
  let navigate = useNavigate();

  return (
    <div>
      <IconButton>
        <SettingsIcon id="basic-button"
        aria-controls={open ? 'basic-menu' : undefined}
        aria-haspopup="true"
        aria-expanded={open ? 'true' : undefined}
        onClick={handleClick}
        sx={{color:"white"}}
        />
      </IconButton>
      <Menu
        id="basic-menu"
        anchorEl={anchorEl}
        open={open}
        onClose={handleClose}
        MenuListProps={{
          'aria-labelledby': 'basic-button',
        }}
      >
        <MenuItem onClick={() => navigate('/signup')} >Add User</MenuItem>
        <MenuItem onClick={handleLogout} >Logout</MenuItem>
        <MenuItem onClick={handleAddRole} >Add Role</MenuItem>
        {/* <MenuItem onClick={handleClose}>My account</MenuItem>
        <MenuItem onClick={handleClose}>Logout</MenuItem> */}
      </Menu>
    </div>
  );
}
