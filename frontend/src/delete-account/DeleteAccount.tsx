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

import React from 'react'
import {Layout} from '../shared/components/Layout';
import Button from '@material-ui/core/Button';
import PasswordInput from '../shared/form/Password';

export default function DeleteAccount():JSX.Element {
    return (
      <Layout title="Delete Account" centered={true}>
        <div className="delete-account-body">
          <h2>Warning: this action is irreversible.</h2>
          <p className="delete-account-text">If you&apos;d like to first
            export your data, please do so below. This gives you a chance to 
            save your data in case you&apos;d ever like to create an accountpage again!
          </p>
          <Button 
            className="modal-button-primary"
            variant="contained"
            color="primary">
            Export account data
          </Button>
          <p className="delete-account-text">If you&apos;re sure you want to 
            delete your account, confirm deletion by entering your password below.
          </p>
          <PasswordInput
            fieldName="Confirm Password"
            onPasswordChanged={() => null}
            isInvalid={false}
            errorMessage=""
          />
          <Button
            className="modal-button-primary"
            variant="contained"
            color="secondary">
            Delete my account
          </Button>
        </div>
        <Button
          className="modal-button-primary"
          variant="contained"
          color="default">
          Cancel
        </Button>
      </Layout>
    )
}
