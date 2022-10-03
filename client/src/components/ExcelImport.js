import React, { useState } from "react";
import DownloadIcon from "@mui/icons-material/Download";
import { Button, IconButton } from "@mui/material";
import axios from "axios";
const IMPORT_EXCEL_URL = "http://localhost:8080/importemployees";

export default function ExcelImport({ fetchData }) {
  const [error, seterror] = useState("");
  const [importSuccess, setimportSuccess] = useState("");

  const handleImport = async (event) => {
    let lt = localStorage.getItem("accessToken");
    const formData = new FormData();
    const file = event.target.files[0];
    formData.append("empExcel", file);
    var response = await axios
      .post(IMPORT_EXCEL_URL, formData, {
        headers: {
          "Content-Type": "multipart/form-data",
          Authorization: JSON.parse(lt),
        },
      })
      .catch((error) => {
        if (error.response?.status === 415) {
          console.log(error, "error");
          seterror("Unsupported Media");
        }
      });

    if (response.status === 200) {
      fetchData();
      setimportSuccess("Imported Successfully")
    
    }
  };
  return (
    <div>
      <Button variant="outlined" component="label">
        Excel Import
        <input type="file" hidden onChange={handleImport}></input>
      </Button>{" "}
      <p style={{ color: "red" }}>{error}</p>
      <p style={{ color: "blue" }}>{importSuccess}</p>
    </div>
  );
}
