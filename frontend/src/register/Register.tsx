/*
The book project lets a user keep track of different books they would like to read, are currently
reading, have read or did not finish.
copyright (c) 2020  karan kumar

this program is free software: you can redistribute it and/or modify it under the terms of the
gnu general public license as published by the free software foundation, either version 3 of the
license, or (at your option) any later version.

this program is distributed in the hope that it will be useful, but without any
warranty; without even the implied warranty of merchantability or fitness for a particular
purpose.  see the gnu general public license for more details.

you should have received a copy of the gnu general public license along with this program.
if not, see <https://www.gnu.org/licenses/>.
*/

import Button from '@material-ui/core/Button';
import React, { Component } from 'react';
import { Link } from "react-router-dom";
import zxcvbn from 'zxcvbn';
import Endpoints from '../shared/api/endpoints';
import EmailAddress from '../shared/form/EmailAddress';
import Password from '../shared/form/Password';
import PasswordStrengthMeter from '../shared/form/PasswordStrengthMeter';
import Verb from '../shared/http/verb';
import logo from '../shared/media/logo/logo-black.png';
import './Register.css';

type RegisterProps = {
}

interface IState {
  email: string,
  password: string,
  passwordsMatch: boolean,
  passwordStrengthScore?: number,
  isEmailDirty: boolean,
  isPasswordDirty: boolean,
  areCredentialsInvalid: boolean
}

class Register extends Component<Record<string, unknown>, IState> {
  constructor(props: RegisterProps) {
    super(props)

    this.state = {
      email: '',
      password: '',
      passwordsMatch: true,
      passwordStrengthScore: undefined,
      isEmailDirty: false,
      isPasswordDirty: false,
      areCredentialsInvalid: false
    }

    this.handlePasswordChanged = this.handlePasswordChanged.bind(this)
    this.handleConfirmPasswordChanged = this.handleConfirmPasswordChanged.bind(this)
    this.onEmailChanged = this.onEmailChanged.bind(this);
    this.onCreateAccountClicked = this.onCreateAccountClicked.bind(this)
  }

  handlePasswordChanged(password: string): void {
    const score = password
      ? zxcvbn(password).score
      : undefined;

    this.setState({
      password,
      isPasswordDirty: true,
      passwordStrengthScore: score
    })
  }

  handleConfirmPasswordChanged(password: string): void {
    const passwordsMatch = password === this.state.password
    this.setState({ passwordsMatch })
  }

  onEmailChanged(email: string): void {
    this.setState({
      email,
      isEmailDirty: true
    })
  }

  onCreateAccountClicked(): void {
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
        // eslint-disable-next-line @typescript-eslint/naming-convention
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
          <img src={logo} alt="Logo" id="app-logo" />

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

          <PasswordStrengthMeter score={this.state.passwordStrengthScore}></PasswordStrengthMeter>

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
