/*
The book project lets a user keep track of different books they would like to read, are currently
reading, have read or did not finish.
Copyright (C) 2020  Karan Kumar

This program is free software: you can redistribute it and/or modify it under the terms of the
GNU General Public License as published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.
If not, see <https://www.gnu.org/licenses/>.
*/

import React, {Component} from 'react';
import './Login.css';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Password from '../shared/form/Password';
import { Link } from "react-router-dom";
import logo from '../shared/media/logo.png';

class Login extends Component {
  constructor(props: LoginProps) {
    super(props)

    // this.state = {
    //   username: '',
    //   password: ''
    // }
  }

  render() {
    return (
      <div >
          <img src={logo} alt="Logo" id="app-name"/>

          <br />
          <br />
          <br />

          <div className="login-form">
            <TextField 
              id="standard-basic" 
              className="login" 
              label="Username" 
              variant="outlined" 
              required 
              autoFocus 
            />

            <br />
            <br />

            <Password fieldName={'Password'} class={'login'} />

            <br />
            <br />

            <Button className="login" variant="contained" color="primary">
              Log in
            </Button>

            <br />
            <br />
            <br />
            <br />

            <Button className="login" id="createAccount" component={Link} to="/sign-up">
              Create account
            </Button>
          </div>
          
      </div>
    );
  }
}

type LoginProps = {
}

export default Login;
