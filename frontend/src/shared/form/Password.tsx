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

import React, {ReactElement} from 'react'
import { 
  FormControl, 
  FormHelperText, 
  IconButton, 
  InputAdornment, 
  InputLabel} from '@material-ui/core'
import Visibility from '@material-ui/icons/Visibility';
import VisibilityOff from '@material-ui/icons/VisibilityOff';
import {useState} from 'react';
import OutlinedInput from '@material-ui/core/OutlinedInput'

function Password(props: PasswordProps): ReactElement {
  const [showPassword, setShowPassword] = useState(false);
  function handlePasswordToggle () {
    setShowPassword(!showPassword)
  }
  
  return (
        <FormControl variant="outlined" required error = {props.isInvalid}>
          <InputLabel htmlFor="outlined-adornment-password">{props.placeholderText}</InputLabel>
          <OutlinedInput
            id="outlined-adornment-password"
            className={props.class + " rounded"}
            label={props.placeholderText}
            type={showPassword ? 'text' : 'password'}
            endAdornment={
              <InputAdornment position="end">
              <IconButton
                aria-label="toggle password visibility"
                size="small"
                onClick={handlePasswordToggle}
              >
                {showPassword ? <Visibility /> : <VisibilityOff />}
              </IconButton>
            </InputAdornment>
            }
            onChange={(event) => props.onPasswordChanged(event.target.value)}
          />
          {props.isInvalid && (
              <FormHelperText className={props.classHelper}>
                  {props.errorMessage}
              </FormHelperText>
          )}
        </FormControl>
  )
}

type PasswordProps = {
  placeholderText: string,
  class?: string,
  classHelper?: string,
  onPasswordChanged: (password: string) => void,
  isInvalid: boolean,
  errorMessage: string
}

export default Password
