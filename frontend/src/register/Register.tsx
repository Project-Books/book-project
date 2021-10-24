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
import React, {Component, ReactElement} from 'react';
import {Link} from "react-router-dom";
import zxcvbn from 'zxcvbn';
import Endpoints from '../shared/api/endpoints';
import EmailAddress from '../shared/form/EmailAddress';
import Password from '../shared/form/Password';
import PasswordStrengthMeter from '../shared/form/PasswordStrengthMeter';
import Verb from '../shared/http/verb';
import logo from '../shared/media/logo/logo_one_line@1x.png';
import './Register.css';

enum PassStrengthEnum {
    WEAK,
    FAIR,
    GOOD,
    STRONG,
    VERY_STRONG
}

type RegisterProps = {
}

interface IState {
    email: string,
    password: string,
    passwordConfirmed: string,
    passwordStrengthScore?: number,
    isEmailDirty: boolean,
    isPasswordDirty: boolean,
    isEmailInvalid: boolean,
    isPasswordInvalid: boolean,
    serverError: boolean
}

class Register extends Component<Record<string, unknown>, IState> {
    constructor(props: RegisterProps) {
        super(props)

        this.state = {
            email: '',
            password: '',
            passwordConfirmed: '',
            passwordStrengthScore: undefined,
            isEmailDirty: false,
            isPasswordDirty: false,
            isEmailInvalid: false,
            isPasswordInvalid: false,
            serverError: false
        }

        this.handlePasswordChanged = this.handlePasswordChanged.bind(this)
        this.handleConfirmPasswordChanged = this.handleConfirmPasswordChanged.bind(this)
        this.onEmailChanged = this.onEmailChanged.bind(this)
        this.onCreateAccountClicked = this.onCreateAccountClicked.bind(this)
    }

    handlePasswordChanged(password: string): void {
        const score = password
            ? zxcvbn(password).score
            : undefined;

        this.setState({
            password,
            isPasswordDirty: true,
            isPasswordInvalid: password === '',
            passwordStrengthScore: score
        })
    }

    handleConfirmPasswordChanged(password: string): void {
        this.setState({passwordConfirmed: password})
    }

    checkPasswordsMatch(): boolean {
        return this.state.password === this.state.passwordConfirmed
    }

    onEmailChanged(email: string): void {
        this.setState({
            email,
            isEmailDirty: true,
            isEmailInvalid: email === ''
        })
    }

    onCreateAccountClicked(): void {
        this.setState({
            isEmailInvalid: this.state.email === '',
            isPasswordInvalid: this.state.password === ''
        })

        if (!this.state.isEmailInvalid && !this.state.isPasswordInvalid
            && this.checkPasswordsMatch() &&
            this.state.passwordStrengthScore === PassStrengthEnum.VERY_STRONG) {
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

        fetch(Endpoints.user, requestOptions)
            .then(response => {
                if (response.status === 500) {
                    this.setState({serverError:true})
                }
                return response.json()
            })
            .then(data => console.log('data: ', data))
            .catch(error => console.log('error: ', error))
    }

    isEmailInvalid(): boolean {
        const isEmailDirtyAndBlank = this.state.email === '' && this.state.isEmailDirty
        return isEmailDirtyAndBlank || this.state.isEmailInvalid
    }

    isPasswordInvalid(): boolean {
        const isPasswordDirtyAndBlank = this.state.password === '' && this.state.isPasswordDirty
        return isPasswordDirtyAndBlank || this.state.isPasswordInvalid
    }

    // function to display server error
    renderServerError(): ReactElement {
        return (
            <p className="error-message">
              Something went wrong. Please try again later.
            </p>
     )
 }  

    render(): ReactElement {
        return (
            <div className="center-table">
                <div className="center-table-cell">
                    <img src={logo} alt="Logo" className="center" id="app-logo" />

                    <br />
                    <br />                
                    <br />

                    <div className="center">
                        <EmailAddress
                            class="center login"
                            classHelper="center helper"
                            isInvalid={this.isEmailInvalid()}
                            onChange={this.onEmailChanged}
                            errorMessage={'Please enter an email address'}
                        />

                        <br />

                        <Password
                            placeholderText={'Password'}
                            class={'center login'}
                            classHelper={'center helper'}
                            onPasswordChanged={this.handlePasswordChanged}
                            isInvalid={this.isPasswordInvalid()}
                            errorMessage={'Please enter a password'}
                        />
                        <br />
                        <br />

                        <PasswordStrengthMeter score={this.state.passwordStrengthScore} />

                        <br />

                        <Password
                            placeholderText={'Confirm password'}
                            class={'center login'}
                            classHelper={'center helper'}
                            onPasswordChanged={this.handleConfirmPasswordChanged}
                            isInvalid={!this.checkPasswordsMatch() || this.state.isPasswordInvalid}
                            errorMessage={'Passwords do not match. Please try again'}
                        />

                        <br />
                        <br />

                        <Button
                            className="center login"
                            variant="contained"
                            color="primary"
                            onClick={this.onCreateAccountClicked}
                            disableElevation
                            >
                            Create account
                        </Button>

                        <br />
                        <br />

                        <Button 
                          className="center login" 
                          id="createAccount" 
                          component={Link} to="/sign-in"
                          >
                            Sign in instead
                        </Button>
                        
                        {this.state.serverError ? this.renderServerError(): <br></br>}   

                    </div>
                </div>
            </div>
        )
    }
}

export default Register
