import React from 'react'
import { Button, IconButton } from '@mui/material';
import axios from 'axios';
import ImportExportIcon from '@mui/icons-material/ImportExport';
import PictureAsPdfIcon from '@mui/icons-material/PictureAsPdf';


const DATA_LIST_URL = "http://localhost:8080/";

export default function CustomExport({page}) {

const handleExport = async() => {
try {
    //console.log(page)
    let lt = localStorage.getItem("accessToken");
    const response = await axios.get(DATA_LIST_URL + 'exportpdf/' + page, {
        headers: {
            'Content-Type': 'application/pdf',
            Authorization: JSON.parse(lt)
        },
        responseType: 'blob',
    })
    //console.log(response)
    
    
    const url =  window.URL.createObjectURL(
        new Blob([response.data],{type: 'application/pdf'}),
      );
      const link = document.createElement('a');
      link.href = url;
      link.download = 'employeeinfo.pdf'
  
      // Append to html link element page
      document.body.appendChild(link);
  
      // Start download
      link.click();
  
      // Clean up and remove the link
      link.parentNode.removeChild(link);
  }catch(error){
    console.log(error);
}
}



  return (
    <>
    <IconButton onClick={handleExport}><PictureAsPdfIcon /> </IconButton>
    </>
  );
}
