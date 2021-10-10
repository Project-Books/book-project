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
import Switch, { SwitchProps } from '@material-ui/core/Switch';
import { makeStyles} from '@material-ui/core/styles';
import { grey } from '@material-ui/core/colors';

const useStyles = makeStyles({
  root: {
    color: grey[900]
  },
  switchBase: {
    color: grey[900],
    '&$checked': {
      color: grey[900],
    },
    '&$checked + $track': {
      backgroundColor: grey[800],
    },
  },
  checked: {},
  track: {},
  thumb: {},
})

function ColoredSwitch(props: SwitchProps): JSX.Element {
  const classes = useStyles()
  return (
    <div>
      <Switch 
        classes={{
          root: classes.root,
          switchBase: classes.switchBase,
          thumb: classes.thumb,
          track: classes.track,
          checked: classes.checked,
        }}
        {...props}
      />
    </div>
  )
}
export default ColoredSwitch;
