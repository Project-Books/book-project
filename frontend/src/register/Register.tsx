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

import React, { Component } from 'react'
import './Register.css'
import Button from '@material-ui/core/Button'
import Password from '../shared/form/Password'
import EmailAddress from '../shared/form/EmailAddress'
import { Link } from "react-router-dom"
import logo from '../shared/media/logo/logo-black.png'
import Verb from '../shared/http/verb'
import Endpoints from '../shared/api/endpoints'

type RegisterProps = {
}

interface IState {
  email: string,
  password: string,
  passwordsMatch: boolean,
  isEmailDirty: boolean,
  isPasswordDirty: boolean,
  areCredentialsInvalid: boolean
}

class Register extends Component<{}, IState> {
  constructor(props: RegisterProps) {
    super(props)

    this.state = {
      email: '',
      password: '',
      passwordsMatch: true,
      isEmailDirty: false,
      isPasswordDirty: false,
      areCredentialsInvalid: false
    }

    this.handlePasswordChanged = this.handlePasswordChanged.bind(this)
    this.handleConfirmPasswordChanged = this.handleConfirmPasswordChanged.bind(this)
    this.onEmailChanged = this.onEmailChanged.bind(this);
    this.onCreateAccountClicked = this.onCreateAccountClicked.bind(this)
  }

  handlePasswordChanged(password: string) {
    this.setState({
      password,
      isPasswordDirty: true
    })
  }

  handleConfirmPasswordChanged(password: string) {
    const passwordsMatch = password === this.state.password
    this.setState({passwordsMatch})
  }

  onEmailChanged(email: string) {
    this.setState({
      email,
      isEmailDirty: true
    })
  }

  onCreateAccountClicked() {
    const isFieldEmpty = this.state.email === '' || this.state.password === ''
    this.setState({
      areCredentialsInvalid: isFieldEmpty
    })


    if (!this.state.areCredentialsInvalid) {
      this.sendRegisterRequest()
    }
  }

  sendRegisterRequest(): void {
      const requestOptions = {
        method: Verb.POST,
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          email: this.state.email,
          password: this.state.password
        })
      }

      fetch(Endpoints.register, requestOptions)
        .then(response => response.json())
        .then(data => console.log('data: ', data))
        .catch(error => console.log('error: ', error))
  }

  isPasswordInvalid(): boolean {
    const isPasswordDirtyAndBlank = this.state.password === '' && this.state.isPasswordDirty
    return isPasswordDirtyAndBlank || this.state.areCredentialsInvalid
  }

  render() {
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} alt="Logo" id="app-logo"/>

          <br />
          <EmailAddress
              class="login"
              isInvalid={this.state.areCredentialsInvalid}
              onChange={this.onEmailChanged}
              areCredentialsInvalid={this.state.areCredentialsInvalid}
            />
         
          <br />
          
          <Password 
            fieldName={'Password'} 
            class={'login'} 
            onPasswordChanged={this.handlePasswordChanged}
            isInvalid={this.isPasswordInvalid()}
            errorMessage={'Please enter a password'}
          />

          <br />
          <br />

          <Password 
            fieldName={'Confirm password'} 
            class={'login'} 
            onPasswordChanged={this.handleConfirmPasswordChanged}
            isInvalid={!this.state.passwordsMatch || this.state.areCredentialsInvalid}
            errorMessage={'Passwords do not match. Please try again'}
          />

          <br />
          <br />

          <Button 
            className="login" 
            variant="contained" 
            color="primary" 
            onClick={this.onCreateAccountClicked}>
              Create account
          </Button>

          <br />
          <br />

          <Button className="login" id="createAccount" component={Link} to="/sign-in">
            Sign in instead
          </Button>

          <br />
          <br />

        </header>
      </div>
    )
  }
}

export default Register
