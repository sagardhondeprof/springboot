import * as React from "react";
import Table from "@mui/material/Table";
import TableBody from "@mui/material/TableBody";
import TableCell from "@mui/material/TableCell";
import TableContainer from "@mui/material/TableContainer";
import TableHead from "@mui/material/TableHead";
import TableRow from "@mui/material/TableRow";
import Paper from "@mui/material/Paper";
import { Box, Button, IconButton, Toolbar } from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import EditIcon from "@mui/icons-material/Edit";
import AddRoleDialog from "./AddRoleDialog";
import axios from "axios";

const ROLES_LIST_URL = "http://localhost:8080/roles/getroles";
const ADD_ROLES_URL = "http://localhost:8080/roles/addrole";
const DELETE_ROLE_URL = "http://localhost:8080/roles/deleterole/";
const UPDATE_ROLE_URL = "http://localhost:8080/roles/updaterole/";

const initialValues = {
  roleName: "",
  description: "",
  accessMapping : {
    
  }
};
const accessinitial = {};

export default function BasicTable() {
  const [roles, setroles] = React.useState([]);
  const [roleFormData, setroleFormData] = React.useState(initialValues);
  const [accessMapping, setaccessMapping] = React.useState(accessinitial);

  const handleAccessChange = (event) => {
    console.log(event.target.name,event.target.checked,event.target.id)
    setaccessMapping({
      ...accessMapping,
      [event.target.name]:event.target.id+'_'+event.target.checked
    })
    // setroleFormData({ ...roleFormData, accessMapping })
  }

  const fetchRoles = async () => {
    let lt = localStorage.getItem("accessToken");
    const response = await axios.get(ROLES_LIST_URL, {
      headers: {
        Authorization: JSON.parse(lt),
      },
    });
    setroles(response.data);
  };

  React.useEffect(() => {
    fetchRoles();
  }, []);

  React.useEffect(() => {
    setroleFormData({ ...roleFormData, accessMapping })
  }, [accessMapping])
  

  const [addDialog, setAddDialog] = React.useState(false);
  const handleDelete = async (id) => {
    let lt = localStorage.getItem("accessToken");
    const response = await axios.delete(DELETE_ROLE_URL + id, {
      headers: {
        Authorization: JSON.parse(lt)
      }
    })
    if (response.status === 202) {
      fetchRoles();
    }
  };

  const handleUpdate = (role) => {
    const { createdBy, createdDate, lastUpdateDate, lastUpdatedBy, accessControl, ...newRole } = role
    setroleFormData(newRole)
    setAddDialog(true);
    console.log(newRole);

  };

  const handleClickOpen = () => {
    setAddDialog(true);
  };

  const handleClose = () => {
    setAddDialog(false);
    setroleFormData(initialValues)
    setaccessMapping(accessinitial);
  };

  const onChange = (e) => {
    console.log(e.target.id);
    const { value, id } = e.target;
    setroleFormData({ ...roleFormData, [id]: value });
  };

  const handleSubmit = async (event) => {
    console.log(accessMapping)
    //setroleFormData({ ...roleFormData, accessMapping })
    event.preventDefault();
    if (roleFormData.id) {
      let lt = localStorage.getItem("accessToken");
      const response = axios.put(UPDATE_ROLE_URL + roleFormData.id, roleFormData, {
        headers: {
          Authorization: JSON.parse(lt),
        },
      });
      console.log(response)
      if ((await response).status === 202) {
        fetchRoles();
      }
      setroleFormData(initialValues);
      handleClose()
    } else {
      console.log(roleFormData);
      let lt = localStorage.getItem("accessToken");
      const response = axios.post(ADD_ROLES_URL, roleFormData, {
        headers: {
          Authorization: JSON.parse(lt),
        },
      });
      console.log(response);
      handleClose();
      if ((await response).status === 201) {
        fetchRoles();
      }
      setroleFormData(initialValues);
      setaccessMapping(accessinitial);
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

        <AddRoleDialog
          addDialog={addDialog}
          handleClose={handleClose}
          data={roleFormData}
          onChange={onChange}
          handleFormSubmit={handleSubmit}
          handleAccessChange={handleAccessChange}
        />

        <div style={{ float: "right", padding: "15px" }}>
          <Button variant="contained" onClick={handleClickOpen}>
            NEW ROLE
          </Button>
        </div>

        <TableContainer component={Paper} sx={{ padding: "10px" }}>
          <Table sx={{ minWidth: 650 }} aria-label="simple table">
            <TableHead sx={{ backgroundColor: "#f4bbff" }}>
              <TableRow >
                <TableCell sx={{ fontWeight: "bold" }}>Role ID</TableCell>
                <TableCell sx={{ fontWeight: "bold" }}>Role Name</TableCell>
                <TableCell sx={{ fontWeight: "bold" }}>Description</TableCell>
                <TableCell sx={{ fontWeight: "bold" }}>createdBy</TableCell>
                {/* <TableCell>createdDate</TableCell>
                <TableCell>lastUpdateDate</TableCell> */}
                <TableCell sx={{ fontWeight: "bold" }}>lastUpdatedBy</TableCell>
                <TableCell sx={{ fontWeight: "bold" }}>Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {roles.map((role) => (
                <TableRow
                  key={role.roleName}
                  sx={{
                    "&:last-child td, &:last-child th": { border: 0 },
                    textAlign: "left",
                  }}
                  hover="true"
                >
                  <TableCell align="left" >
                    {role.id}
                  </TableCell>
                  <TableCell align="left" >
                    {role.roleName}
                  </TableCell>
                  <TableCell align="left">{role.description}</TableCell>
                  <TableCell align="left" >
                    {role.createdBy}
                  </TableCell>
                  {/* <TableCell align="left" sx={{ fontWeight: "bold" }}>
                    {role.createdDate}
                  </TableCell>
                  <TableCell align="left" sx={{ fontWeight: "bold" }}>
                    {role.lastUpdateDate}
                  </TableCell> */}
                  <TableCell align="left" >
                    {role.lastUpdatedBy}
                  </TableCell>
                  <TableCell align="left">
                    <IconButton onClick={() => handleDelete(role.id)}>
                      <DeleteIcon />
                    </IconButton>
                    <IconButton onClick={() => handleUpdate(role)}>
                      <EditIcon />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      </Box>
    </>
  );
}
