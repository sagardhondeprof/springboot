import * as React from "react";
import Button from "@mui/material/Button";
import TextField from "@mui/material/TextField";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import { Grid } from "@mui/material";

export default function AddRoleDialog({
  addDialog,
  handleClose,
  onChange,
  handleFormSubmit,
  data,
  error,
}) {
  return (
    <div>
      <Dialog open={addDialog} onClose={handleClose}>
        <form onSubmit={handleFormSubmit}>
          {/* <DialogTitle>{id ? "Update Employee" : "Add Employee"}</DialogTitle> */}
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
                  // error = {error.firstName}
                  onChange={(e) => onChange(e)}
                />
                {/* <p style={{ color: "red", marginTop: '0px' }}>{error.firstName}</p> */}
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
                  // error={error.lastName}
                  onChange={(e) => onChange(e)}
                />
                {/* <p style={{ color: "red", marginTop: '0px' }}>{error.lastName}</p> */}
              </Grid>
            </Grid>
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
