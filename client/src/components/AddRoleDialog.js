import * as React from "react";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import { Grid, Paper, Switch, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@mui/material";
import axios from "axios";
const ACCESS_LIST_URL = "http://localhost:8080/roles/getaccesslist";

export default function AddRoleDialog({
  addDialog,
  handleClose,
  onChange,
  handleFormSubmit,
  data,
  error,
  handleAccessChange,

}) {
  const [screendata, setScreendata] = React.useState([]);


  const fetchAccessNames = async () => {
    let lt = localStorage.getItem("accessToken");
    const response = await axios.get(ACCESS_LIST_URL, {
      headers: {
        Authorization: JSON.parse(lt),
      },
    });
    console.log(response.data)
    setScreendata(response.data);
  }

  React.useEffect(() => {
    fetchAccessNames();
  }, [])

  return (
    <div>
      <Dialog open={addDialog} onClose={handleClose}>
        <form onSubmit={handleFormSubmit}>
          <DialogContent>
            <Grid container spacing={4}>
              <Grid item xs={6}>
                <TextField
                  autoFocus
                  autoComplete="off"
                  margin="dense"
                  id="roleName"
                  label="Role"
                  fullWidth
                  variant="outlined"
                  size="small"
                  value={data.roleName}
                  onChange={(e) => onChange(e)}
                />
              </Grid>
              <Grid item xs={6}>
                <TextField
                  autoFocus
                  margin="dense"
                  id="description"
                  label="Description"
                  fullWidth
                  variant="outlined"
                  size="small"
                  value={data.description}
                  onChange={(e) => onChange(e)}
                />
              </Grid>
            </Grid>
            <TableContainer component={Paper} style={{ 'padding': '5px ' }}>
              <Table sx={{ minWidth: 100 }} size="small" aria-label="custom pagination table" >
                <TableHead>
                  <TableRow>
                    <TableCell style={{ 'fontWeight': 'bold' }}>Screen Name</TableCell>
                    <TableCell align="left" style={{ 'fontWeight': 'bold' }}>Read</TableCell>
                    <TableCell align="left" style={{ 'fontWeight': 'bold' }}>Write</TableCell>
                  </TableRow>
                </TableHead>
                <TableBody>
                  {screendata.map((row) => (
                    <TableRow
                      key={row.accessName}
                      sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                    >
                      <TableCell component="th" scope="row">
                        {row.accessName}
                      </TableCell>
                      <TableCell>
                        <Switch defaultUnchecked color="secondary" onChange={handleAccessChange}
                          name={row.accessName} id='read' />
                      </TableCell>
                      <TableCell>
                        <Switch defaultUnchecked color="secondary" onChange={handleAccessChange}
                          name={row.accessName} id='write' />
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </TableContainer>
          </DialogContent>
          <DialogActions>
            <Button color="secondary" variant="contained" onClick={handleClose}>
              Cancel
            </Button>
            <Button type="submit" variant="contained">
              {data.id ? "Update" : "Add Role"}
            </Button>
          </DialogActions>
        </form>
      </Dialog>
    </div>
  );
}
