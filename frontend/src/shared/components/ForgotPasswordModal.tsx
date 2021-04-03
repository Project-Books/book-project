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
import Modal, {ISimpleModalProps} from './Modal'
import Email from '../form/EmailAddress';
import './ForgotPasswordModal.css';

export default class ForgotPasswordModal extends Component<ISimpleModalProps> {
  constructor(props: ISimpleModalProps) {
    super(props)
    this.onEmailChanged = this.onEmailChanged.bind(this);
  }

  state = {
    isInvalid: false,
    errorMessage: '',
    emailEntered: ''
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
        <Modal open={this.props.open}>
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
              <div className="email-form">
                <Email 
                  onChange={this.onEmailChanged} 
                  isInvalid={this.state.isInvalid} 
                  errorMessage={this.state.errorMessage} 
                />
            </div>
          </div>
          </div>
        </Modal>
      </div>
    )
  }
}
