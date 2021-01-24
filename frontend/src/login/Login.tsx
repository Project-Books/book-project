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

import React, {Component} from 'react'
import './Login.css'
import Button from '@material-ui/core/Button'
import Password from '../shared/form/Password'
import EmailAddress from '../shared/form/EmailAddress'
import { Link } from "react-router-dom"
import logo from '../shared/media/logo/logo-black.png'

interface IState {
  email: string,
  password: string,
  isEmailDirty: boolean,
  areCredentialsInvalid: boolean
}

type LoginProps = {
}

class Login extends Component<{}, IState> {
  constructor(props: LoginProps) {
    super(props)

    this.state = {
      email: '',
      password: '',
      isEmailDirty: false,
      areCredentialsInvalid: false
    }

    this.onPasswordChanged = this.onPasswordChanged.bind(this)
    this.onEmailChanged = this.onEmailChanged.bind(this);
    this.onClickLogin = this.onClickLogin.bind(this)
  }

  onClickLogin() {
    this.setState({
      areCredentialsInvalid: this.state.email === '' && this.state.password === ''
    })
  }

  onEmailChanged(email: string) {
    this.setState({
      email,
      isEmailDirty: true
    })
  }

  onPasswordChanged(password: string) {
    console.log(`login password: ${password}`)
    this.setState({password})
  }

  isEmailInvalid(): boolean {
    const isEmailDirtyAndBlank = this.state.email === '' && this.state.isEmailDirty
    return isEmailDirtyAndBlank || this.state.areCredentialsInvalid
  }
  
  render() {
    return (
      <div >
          <img src={logo} alt="Logo" id="app-logo"/>

          <br />
          <br />
          <br />

          <div className="login-form">
            <EmailAddress
              class="login"
              isInvalid={this.isEmailInvalid()}
              onChange={this.onEmailChanged}
              areCredentialsInvalid={this.state.areCredentialsInvalid}
            />

            <br />

            <Password 
              fieldName={'Password'} 
              class={'login'} 
              onPasswordChanged={this.onPasswordChanged}
              isInvalid={this.state.areCredentialsInvalid}
              errorMessage={'Please enter a password'}
            />

            <br />
            <br />

            <Button 
              className="login" 
              variant="contained" 
              color="primary" 
              onClick={this.onClickLogin}>
              Log in
            </Button>

            <br />
            <br />

            <Button className="login" id="createAccount" component={Link} to="/sign-up">
              Create account
            </Button>
          </div>
      </div>
    )
  }
}

export default Login;
