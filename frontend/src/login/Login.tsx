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
import './Login.css'
import Button from '@material-ui/core/Button'
import Password from '../shared/form/Password'
import EmailAddress from '../shared/form/EmailAddress'
import { Link } from "react-router-dom"
import logo from '../shared/media/logo/logo-black.png'
import Endpoints from '../shared/api/endpoints'
import Verb from '../shared/http/verb'
import { RouteComponentProps } from 'react-router-dom';
import * as routes from '../shared/routes'

interface IState {
  email: string,
  password: string,
  isEmailDirty: boolean,
  areCredentialsInvalid: boolean,
  loginFailed: boolean
}

type LoginProps = Record<string, unknown> & RouteComponentProps

class Login extends Component<LoginProps, IState> {
  constructor(props: LoginProps) {
    super(props)

    this.state = {
      email: '',
      password: '',
      isEmailDirty: false,
      areCredentialsInvalid: false,
      loginFailed: false
    }

    this.onPasswordChanged = this.onPasswordChanged.bind(this)
    this.onEmailChanged = this.onEmailChanged.bind(this);
    this.onClickLogin = this.onClickLogin.bind(this)
    this.sendLoginRequest = this.sendLoginRequest.bind(this)
    this.sendLoginRequestIfCredentialsAreValid = this.sendLoginRequestIfCredentialsAreValid.bind(this)
  }

  onClickLogin() {
    this.setState({
      areCredentialsInvalid: this.state.email === '' && this.state.password === ''
    }, this.sendLoginRequestIfCredentialsAreValid)
  }

  onEmailChanged(email: string) {
    this.setState({
      email,
      isEmailDirty: true
    })
  }

  onPasswordChanged(password: string) {
    console.log(`login password: ${password}`)
    this.setState({ password })
  }

  isEmailInvalid(): boolean {
    const isEmailDirtyAndBlank = this.state.email === '' && this.state.isEmailDirty
    return isEmailDirtyAndBlank || this.state.areCredentialsInvalid
  }

  sendLoginRequestIfCredentialsAreValid() {
    if (!this.state.areCredentialsInvalid) {
      this.sendLoginRequest()
    }
  }

  sendLoginRequest(): void {
    const requestOptions = {
      method: Verb.POST,
      headers: {
        // eslint-disable-next-line @typescript-eslint/naming-convention
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        username: this.state.email,
        password: this.state.password
      })
    }

    fetch(Endpoints.login, requestOptions)
      .then(response => {
        if (response.ok) {
          this.props.history.push(routes.MY_BOOKS)
        } else {
          this.setState({ loginFailed: true });
        }
      })
      .catch(error => {
        this.setState({ loginFailed: true })
        console.error('error: ', error)
      })
  }

  renderLoginError() {
    return <p style={{ color: 'red' }}>Your email or password is incorrect. Please try again</p>
  }

  render() {
    return (
      <div className="center-table">
      	<div className="center-table-cell">
	        <img src={logo} alt="Logo" className="center app-logo" />
	
	        <br />
	        <br />
	        <br />
	
	        <div className="center login-form">
	          <EmailAddress
	            class="center login"
	            isInvalid={this.isEmailInvalid()}
	            onChange={this.onEmailChanged}
	            areCredentialsInvalid={this.state.areCredentialsInvalid}
	          />
	
	          <br />
	
	          <Password
	            fieldName={'Password'}
	            class={'center login'}
	            onPasswordChanged={this.onPasswordChanged}
	            isInvalid={this.state.areCredentialsInvalid}
	            errorMessage={'Please enter a password'}
	          />
	
	          <br />
	          <br />
	
	          <Button
	            className="center login"
	            variant="contained"
	            color="primary"
	            onClick={this.onClickLogin}>
	            Log in
	            </Button>
	
	          <br />
	          <br />
	
	          <Button className="center login" id="createAccount" component={Link} to={routes.SIGN_UP}>
	            Create account
	            </Button>
	
	          {this.state.loginFailed && this.renderLoginError()}
	
	        </div>
	  	</div>
      </div>
    )
  }
}

export default Login;
