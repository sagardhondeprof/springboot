import * as React from 'react';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogTitle from '@mui/material/DialogTitle';
import { Grid } from '@mui/material';

export default function AddDialog({ open1, handleClose, data, onChange, handleFormSubmit, error }) {
    const { id, firstName, lastName, email, date } = data;

    return (
        <div>
            <Dialog open={open1} onClose={handleClose}>
                <form onSubmit={handleFormSubmit}>
                    <DialogTitle>{id ? "Update Employee" : "Add Employee"}</DialogTitle>
                    <DialogContent>
                        <Grid container spacing={4}>
                            <Grid item xs={6}><TextField
                                autoFocus
                                autoComplete='off'
                                margin="dense"
                                id="firstName"
                                label="First Name"
                                fullWidth
                                variant="outlined"
                                size="small"
                                value={firstName}
                                error = {error.firstName}
                                onChange={e => onChange(e)}
                                
                            />
                                <p style={{ color: "red", marginTop: '0px' }}>{error.firstName}</p>
                            </Grid>
                            <Grid item xs={6}>
                                <TextField
                                    autoFocus
                                    margin="dense"
                                    id="lastName"
                                    label="Last Name"
                                    fullWidth
                                    variant="outlined"
                                    size="small"
                                    value={lastName}
                                    error={error.lastName}
                                    onChange={e => onChange(e)}
                                />
                                <p style={{ color: "red", marginTop: '0px' }}>{error.lastName}</p>
                            </Grid>
                        </Grid>

                        <TextField
                            autoFocus
                            margin="dense"
                            id="email"
                            label="Email"
                            fullWidth
                            variant="outlined"
                            size="small"
                            type="email"
                            value={email}
                            error={error.email}
                            onChange={e => onChange(e)}
                        />
                        <p style={{ color: "red", marginTop: '0px' }}>{error.email}</p>
                        <TextField
                            autoFocus
                            margin="dense"
                            id="date"
                            fullWidth
                            variant="outlined"
                            size="small"
                            type="date"
                            value={date}
                            error={error.date}
                            onChange={e => onChange(e)}
                        />
                        <p style={{ color: "red", marginTop: '0px' }}>{error.date}</p>
                    </DialogContent>
                    <DialogActions>
                        <Button color="secondary" variant='outlined' onClick={handleClose}>Cancel</Button>
                        <Button type='submit' variant='outlined'>{id ? "Update" : "Add"}</Button>
                    </DialogActions>
                </form>
            </Dialog>
        </div>
    );
}
