import * as React from 'react';
import { DataGrid } from '@mui/x-data-grid';
import { Box, Button, Container, Grid, IconButton, Paper, TextField, Toolbar } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import axios from 'axios';
import AddDialog from './AddDialog';
import CustomSnackbar from './CustomSnackbar';
import PersonSearchIcon from '@mui/icons-material/PersonSearch';
import CustomExport from './CustomExport';
import { Link, useLocation } from 'react-router-dom';
import EmployeeActionsMenu from './EmployeeActionsMenu';

const DATA_LIST_URL = "http://localhost:8080/";
const ADD_DATA = "http://localhost:8080/addemployee";
const initialValue = { firstName: "", lastName: "", email: "", date: "" }

export default function DataTable() {
  const location = useLocation();

  const [data, setData] = React.useState("");
  const [open1, setOpen1] = React.useState(false);
  const [sopen, setSopen] = React.useState(false);
  const [smessage, setSmessage] = React.useState("");
  const [serror, setSerror] = React.useState("");
  const [formData, setFormData] = React.useState(initialValue)
  const [formErrors, setFormErrors] = React.useState({});
  const [page, setPage] = React.useState(0);
  const [isLoading, setIsLoading] = React.useState(false);
  const [pageSize, setPageSize] = React.useState(6);
  const [total, setTotal] = React.useState(0);
  const [searchTerm, SetSearchTerm] = React.useState('');
  const [role, setrole] = React.useState([]);


  const columns = [
    { field: 'id', headerName: 'ID', width: 70 },
    { field: 'firstName', headerName: 'First name', flex: 1,  
    renderCell: (params) => (
      <Link to="/employeedetail" style={{textDecoration:"none", color: 'inherit'}} state={{ params: params.row.id }}>{params.row.firstName}</Link>
    ),
    },
    { field: 'lastName', headerName: 'Last name', flex: 1 },
    {
      field: 'email', headerName: 'E-Mail', flex: 1, minWidth: 230
    },
    {
      field: 'date', headerName: 'Date of Joining', flex: 1,

    },
    {
      field: 'edit', headerName: 'Actions', sortable: false,
      renderCell: (cellValues) => {
        return (
          // <IconButton
          //   onClick={() => handleClick(cellValues.row)}><EditIcon /></IconButton>
          //<FormDialog />
          <EmployeeActionsMenu handleClick={handleClick} handleDelete={handleDelete} data={cellValues.row} roles={role}/>
        );
      },
      flex: 1
    },
    role.includes('Admin') &&
    {
      field: 'delete', headerName: 'Delete', sortable: false, flex:1,
      renderCell: (cellValues) => {
        return (
          <>
          <IconButton
            onClick={() => handleDelete(cellValues.id)}><DeleteIcon /></IconButton>
          </>
        );
      }
    }
  ];

  const onChange = (e) => {
    console.log(e.target.id)
    const { value, id } = e.target
    setFormData({ ...formData, [id]: value })
  }

  const validate = (values) => {
    const errors = {}
    const regex = /^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/i;
    if (!values.firstName) {
      errors.firstName = "First Name is required";
    }
    if (!values.lastName) {
      errors.lastName = "Last Name is required";
    }
    if (!values.email) {
      errors.email = "Email is required";
    }
    else if (!regex.test(values.email)) {
      errors.email = "Email is not valid";
    }
    if (!values.date) {
      errors.date = "Date is required";
    }
    return errors;
  }

  const handleFormSubmit = (event) => {
    event.preventDefault();
    console.log(formData);
    //setFormErrors(validate(formData))
    let errorObj = validate(formData);
    setFormErrors(errorObj);
    console.log(formErrors);
    if (Object.keys(errorObj).length === 0) {

      let lt = localStorage.getItem("accessToken");
      if (formData.id) {
        axios.put(DATA_LIST_URL + 'update/' + formData.id, formData, {
          headers: {
            Authorization: JSON.parse(lt)
          }
        })
          .then(resp => {
            console.log(resp.status)
            if (resp.status === 200) {
              setSopen(true);
              setSmessage("Updated Successfully");
              setSerror("success");
              handleClose();
              refetch();
            }
          }).catch((error) => {
            console.log("hi")
            console.log(error)
            setSopen(true);
            setSmessage("Not able to Update");
            setSerror("error");
            handleClose();
          })
        setFormData(initialValue);
      }
      else {
        console.log(formData)
        axios.post(ADD_DATA, formData, {
          headers: {
            Authorization: JSON.parse(lt)
          }
        })
          .then(resp => {
            console.log(resp.status)
            if (resp.status === 201) {
              setSopen(true);
              setSmessage("Added Successfully");
              setSerror("success");
              handleClose();
              refetch();
            }
          }).catch((error) => {
            console.log(error)
            setSopen(true);
            setSmessage("Not able to Add");
            setSerror("error");
            handleClose();
          })

        setFormData(initialValue);
      }
    }
  }

  const handleSclose = (event, reason) => {
    if (reason === 'clickaway') {
      return;
    }

    setSopen(false);
  }

  const handleClickOpen = () => {
    setOpen1(true);
  };

  const handleClose = () => {
    setOpen1(false);
    setFormData(initialValue);
    setFormErrors(initialValue);
  };

  const handleDelete = (id) => {
    let lt = localStorage.getItem("accessToken");
    axios.delete(DATA_LIST_URL + 'delete/' + id, {
      headers: {
        Authorization: JSON.parse(lt)
      }
    })
      .then(resp => console.log(resp))
      .catch((error) => {
        console.log(error)
        setSopen(true);
        setSmessage("Not able to Delete");
        setSerror("error");
      })

    setData(data.filter(data => data.id !== id));
  }

  const handleClick = (cellValues) => {
    //const{createdBy, createdDate, lastUpdateDate, lastUpdatedBy, ...newCellValues} = cellValues
    setFormData(cellValues);
    handleClickOpen();
  }

  function refetch() {
    fetchData();
  }

  React.useEffect(() => {
    fetchData();
  }, [page, pageSize])

  React.useEffect(()=>{
    //console.log(location.state.roles,"ssss")
    setrole(location.state.roles) 
  },[role])

  const fetchData = async () => {
    try {
      let lt = localStorage.getItem("accessToken");
      setIsLoading(true);
      const response = await axios.get(DATA_LIST_URL + 'pagablelist/' + page, {
        headers: {
          Authorization: JSON.parse(lt)
        }
      })
      //console.log(response.data);
      setData(response.data.content)
      setTotal(response.data.totalElements);
      setIsLoading(false);
    } catch (error) {
      console.log(error)
      setSopen(true);
      setSmessage("Not able to Fetch Data");
      setSerror("error");
    }
  }

  const handleSearch = async (event) => {
    SetSearchTerm(event.target.value);
  }

  const handleSearchClick = async () => {
    try {
      console.log(searchTerm)
      let lt = localStorage.getItem("accessToken");
      const response = await axios.get(DATA_LIST_URL + 'likesearch/' + searchTerm, {
        headers: {
          Authorization: JSON.parse(lt)
        }
      })
      console.log(response);
      setData(response.data)
    } catch (error) {
      console.log(error)
      setSopen(true);
      setSmessage("Data Not Found");
      setSerror("error");
    }
  }

  const handleBlur = (event) => {
    if (event.target.value === '') {
      refetch();
    }
  }

  return (
    <>
      <Box
        component="main"
        sx={{
          backgroundColor: (theme) =>
            theme.palette.mode === 'light'
              ? theme.palette.grey[100]
              : theme.palette.grey[900],
          flexGrow: 1,
          height: '100vh',
          overflow: 'auto',
        }}
      >
        <Toolbar />
        <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
          <Grid container spacing={3} >
            <Grid item xs={12} >
              <Paper sx={{ p: 2, display: 'flex', flexDirection: 'column', height: '100%' }}>
                <Grid>
                  <Grid align="left" sx={{ mb: "-37px" }}>
                    <CustomExport page={page} />
                  </Grid>
                  <Grid align="right" >
                    <TextField size='small' variant='standard' label="Search" color='primary' sx={{ mr: '20px', mt: '-8px' }}
                      onChange={(event) => handleSearch(event)}
                      onBlur={(event) => handleBlur(event)}></TextField>
                    <IconButton sx={{ ml: '-50px' }}><PersonSearchIcon onClick={handleSearchClick} /></IconButton>
                    <Button variant='outlined' onClick={handleClickOpen}>ADD EMPLOYEE</Button>
                  </Grid>
                </Grid>
                <AddDialog open1={open1} handleClose={handleClose} data={formData} onChange={onChange}
                  handleFormSubmit={handleFormSubmit} error={formErrors} />
                <div style={{ height: 425, width: '100%' }}>
                  <DataGrid
                    rows={data}
                    columns={columns}
                    rowCount={total}
                    loading={isLoading}
                    rowsPerPageOptions={[6, 10, 20]}
                    pagination
                    page={page}
                    pageSize={pageSize}
                    paginationMode="server"
                    onPageChange={(newPage) => {
                      setPage(newPage)
                      //console.log(newPage);
                    }}
                    onPageSizeChange={(newPageSize) => setPageSize(newPageSize)}
                    checkboxSelection
                    sx={{ pl: '24px' }}
                    disableColumnMenu
                    disableSelectionOnClick
                  />
                </div>
              </Paper>
            </Grid>
          </Grid>
        </Container>
        <CustomSnackbar message={smessage} severity={serror} sopen={sopen} onClose={handleSclose} />
      </Box>
    </>

  );
}
