import React from 'react'
import {Alert, Snackbar} from "@mui/material"

function CustomSnackbar({message, severity, sopen, onClose}) {

  return (
    <>
        <Snackbar
        autoHideDuration={3000}
        open = {sopen}
        onClose = {onClose}
        >
          <Alert  severity={severity} sx={{ width: '100%' }}>
          {message}
          </Alert>
        </Snackbar>
    </>
  )
}

export default CustomSnackbar