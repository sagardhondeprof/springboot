import {
  Button,
  Card,
  CardContent,
  Typography,
  CardActions,
  Box,
} from "@mui/material";
import axios from "axios";
import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";

const DATA_LIST_URL = "http://localhost:8080/";

export default function EmployeeDetail() {
  const location = useLocation();
  const { params } = location.state;
  


  let navigate = useNavigate();

  const [employee, setemployee] = useState({});
  const [allEmp, setallEmp] = useState([]);
  

  useEffect(() => {
    getAllEmployees();
    if(params !==undefined || params !==null)
    {
      findByid()
    }
  },[]);
  const findByid = async () => {
    try {
      let lt = localStorage.getItem("accessToken");
      const response = await axios.get(DATA_LIST_URL + "searchbyid/" + params, {
        headers: {
          Authorization: JSON.parse(lt),
        },
      });

      setemployee(response.data);
    } catch (error) {}
  };
  const getAllEmployees = async () => {
    try {
      let lt = localStorage.getItem("accessToken");
      const response = await axios.get(DATA_LIST_URL + "getallemployees", {
        headers: {
          Authorization: JSON.parse(lt),
        },
      });

      setallEmp(response.data);
    } catch (error) {}
  };
  function back()
  {
    // navigate("/datatable",{state:{roles:role}})
  }
if(params ===undefined || params ===null)
{
  
  
  return (
    <div style={{ marginTop: "100px",marginLeft:"50px" }}>
      <Typography variant="h4" component="div" gutterBottom>
        All Employees
      </Typography>

      {allEmp.map((emp) => (
        
          <Card sm={{ minWidth: 275 }}>
          <CardContent>
            <Typography
              sx={{ fontSize: 14 }}
              color="text.secondary"
              gutterBottom
            >
              Employee Detail :ID({emp.id})
            </Typography>
            <Typography variant="h5" component="div">
              Name : {emp.firstName} {emp.lastName}
            </Typography>
            <Typography sx={{ mb: 1.5 }} color="text.secondary">
              Email : {emp.email}
            </Typography>
          </CardContent>
        </Card>
        
      ))}
    </div>
  );
}

  
  else{
    
    return(
    
      <>
      
      
     {params &&
      <div style={{marginTop:"100px",marginLeft:"50px"}}>
        <Box m="auto">
        <Card sm={{ minWidth: 275 }}>
          <CardContent>
            <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
              Employee Detail :ID({employee.id})
            </Typography>
            <Typography variant="h5" component="div">
              Name : {employee.firstName} {employee.lastName}
            </Typography>
            <Typography sx={{ mb: 1.5 }} color="text.secondary">
              Email : {employee.email}
            </Typography>
          </CardContent>
         
        </Card>
        </Box>
      </div>
      
  }
      </>
    );
  
  }
}
