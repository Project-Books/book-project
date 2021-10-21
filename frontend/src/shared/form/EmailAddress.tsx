/*
The book project lets a user keep track of different books they would like to read, are currently
reading, have read or did not finish.
Copyright (C) 2021  Karan Kumar

This program is free software: you can redistribute it and/or modify it under the terms of the
GNU General Public License as published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.
If not, see <https://www.gnu.org/licenses/>.
*/

import React, {ReactElement} from 'react'
import TextField from '@material-ui/core/TextField'
import {Typography} from "@material-ui/core";

function EmailAddress(props: EmailProps): ReactElement {
  const {
    onChange,
    isInvalid,
  } = props;

  return (
    <div className="rounded">
      <TextField
        id="standard-basic"
        className={props.class}
        label="Email address"
        variant="outlined"
        required
        autoFocus
        error={isInvalid}
        onChange={(event) => onChange(event.target.value)}
        helperText={isInvalid && <Typography
            className={props.classHelper}>
          {props.errorMessage}
        </Typography>}
    />
    </div>
  )
}

type EmailProps = {
  class?: string,
  classHelper?: string,
  isInvalid: boolean,
  onChange: (text: string) => void,
  errorMessage: string
};

export default EmailAddress;
