import React from 'react';
import './App.css';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import InputAdornment from '@material-ui/core/InputAdornment';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <h1 id="app-name">
          Book Project
        </h1>

        <TextField id="standard-basic" label="Username" />

        <br />
        <br />

        <TextField 
          id="standard-basic" 
          label="Password" 
          type="password"
        />

        <br />
        <br />

        <Button id="login" variant="contained" color="primary">
          Log in
        </Button>
        
      </header>
    </div>
  );
}

export default App;
