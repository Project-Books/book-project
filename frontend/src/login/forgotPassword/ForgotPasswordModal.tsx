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
import Modal, { IModalProps } from '../../shared/components/Modal'
import Email from '../../shared/form/EmailAddress';
import Button from '@material-ui/core/Button';
import { Hidden } from '@material-ui/core';
import './ForgotPasswordModal.css';

export default class ForgotPasswordModal extends Component<IModalProps> {
  constructor(props: IModalProps) {
	super(props)
	this.onEmailChanged = this.onEmailChanged.bind(this);
  }

  state = {
	isInvalid: false,
	errorMessage: '',
	emailEntered: '',
  }

  onEmailChanged(emailEntered: string): void {
	this.setState({
		emailEntered,
		isInvalid: emailEntered === ''
	})
  }
  
  render(): JSX.Element {
    return (
      <div>
        <Modal open={this.props.open} onClose={this.props.onClose}>
          <div className="modal-container">
            <div className="modal-content">
              <div className="modal-title">
                Forgot your password?
              </div>
              <div className="modal-desc">
                We&apos;ll email you a link to reset your Password
              </div>
            </div>
            <div className="form-container">
              <Email 
                onChange={this.onEmailChanged} 
                isInvalid={this.state.isInvalid} 
                errorMessage={this.state.errorMessage} 
                class='forgotPasswordInput'
               />
              <div className="password-form-spacer" />
              <div className="password-form-spacer" />
              <Hidden smDown>
                <Button
					className="modal-button-primary"
					variant="contained"
					onClick={this.props.onPasswordResetClicked}
					color="primary">
					Send me a password reset link
                </Button>
              </Hidden>
              <Hidden mdUp>
                <Button
					size="small"
					className="modal-button-mobile"
					variant="contained"
					onClick={this.props.onPasswordResetClicked}
					color="primary">
					Reset password
                </Button>
              </Hidden>
              <div className="password-form-spacer" />
				<Hidden smDown>
					<Button 
						className="modal-button-secondary"
						onClick={this.props.onClose}
						variant="contained">
						Cancel
					</Button>
				</Hidden>
				<Hidden mdUp>
					<Button 
						size="small"
						className="modal-button-secondary-mobile"
						onClick={this.props.onClose}
						variant="contained">
						Cancel
					</Button>
				</Hidden>
            </div>
          </div>
        </Modal>
      </div>
    )
  }
}
