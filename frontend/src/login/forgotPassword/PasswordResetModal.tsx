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
import Modal, {IModalProps} from '../../shared/components/Modal'
import Button from '@material-ui/core/Button';
import './ForgotPasswordModal.css';

export default class PasswordResetModal extends Component<IModalProps> {
 
  render() : JSX.Element {
    return (
      <div>
        <Modal open={this.props.open} onClose={this.props.onClose}>
          <div className="modal-container">
              <div className="modal-content">
                <div className="modal-title">
                  Forgot your password?
                </div>
                <div className="modal-desc">
                Success!If there&apos;s an account associated with that email,
                we&apos;ve sent a link to reset your password.
                </div>
              </div>
              <div className="password-form-spacer" />
                <Button
                  className="close-reset-modal-button"
                  variant="contained"
                  onClick={this.props.onClose}
                  color="primary">
                  Done
                </Button>
           </div>
        </Modal>
      </div>
    )
  }
}
