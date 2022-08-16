import * as React from "react";
import ListItemButton from "@mui/material/ListItemButton";
import ListItemIcon from "@mui/material/ListItemIcon";
import ListItemText from "@mui/material/ListItemText";
import DashboardIcon from "@mui/icons-material/Dashboard";
import BarChartIcon from "@mui/icons-material/BarChart";
import LayersIcon from "@mui/icons-material/Layers";
import ListAltIcon from "@mui/icons-material/ListAlt";
import { useNavigate } from "react-router-dom";
import SchoolIcon from '@mui/icons-material/School';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';

export default function Listitems1({ roles }) {
  let navigate = useNavigate();

  const [role, setrole] = React.useState([]);
  const [selectedIndex, setSelectedIndex] = React.useState(0);

  React.useEffect(() => {
    //console.log("from list", roles)
    setrole(roles);
  }, [roles]);

  React.useEffect(() => {
    switch (window.location.pathname) {
      case "/datatable":
        setSelectedIndex(0);
        break;
      case "/employeedetail":
        setSelectedIndex(2);
        break;
      case "/employeeform":
        setSelectedIndex(3);
        break;
      case "/basicdetails":
        setSelectedIndex(4);
        break;
      default:
        break;
    }
  });

  return (
    <React.Fragment>
      {/* <ListItemButton onClick={() => navigate(`add-employee`)}> */}
      <ListItemButton
        onClick={() => navigate("/datatable", { state: { roles: role } })}
        selected={selectedIndex === 0}
      >
        <ListItemIcon>
          <DashboardIcon />
        </ListItemIcon>
        <ListItemText primary="Datatable" />
      </ListItemButton>
      <ListItemButton selected={selectedIndex === 1}>
        <ListItemIcon>
          <ListAltIcon />
        </ListItemIcon>
        <ListItemText primary="Tab 1" />
      </ListItemButton>
      <ListItemButton
        onClick={() => navigate("/employeedetail", { state: { roles: role } })}
        selected={selectedIndex === 2}
      >
        <ListItemIcon>
          <BarChartIcon />
        </ListItemIcon>
        <ListItemText primary="Tab 2" />
      </ListItemButton>
      <ListItemButton  selected={selectedIndex === 3}>
        <ListItemIcon>
          <SchoolIcon/>
        </ListItemIcon>
        <ListItemText primary="Educational Details" />
      </ListItemButton>
      <ListItemButton  selected={selectedIndex === 4} >
        <ListItemIcon>
          <AccountCircleIcon/>
        </ListItemIcon>
        <ListItemText primary="Basic Details" />
      </ListItemButton>
      {role.includes("Admin") && (
        <ListItemButton selected={selectedIndex === 5}>
          <ListItemIcon>
            <LayersIcon />
          </ListItemIcon>
          <ListItemText primary="Tab 3" />
        </ListItemButton>
      )}
    </React.Fragment>
  );
}
