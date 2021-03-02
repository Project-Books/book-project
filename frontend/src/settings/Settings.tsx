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

import React, {Component} from 'react'
import { NavBar } from '../shared/navigation/NavBar';
import Switch , { SwitchClassKey, SwitchProps } from '@material-ui/core/Switch';
import {grey} from '@material-ui/core/colors';
import { makeStyles, withTheme,MuiThemeProvider} from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import './Settings.css'


interface ISettingsProps {
    theme: any;
    toggleTheme: () => void;
}

interface Styles extends Partial<Record<SwitchClassKey, string>> {
  focusVisible?: string;
}

interface Props extends SwitchProps {
  classes: Styles;
}

const blackSwitch = makeStyles({
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
})(Switch);


 function Settings(props: ISettingsProps):JSX.Element {
    return (
      <React.Fragment>
        <NavBar />
        <MuiThemeProvider theme={props.theme}>
          <CssBaseline />
          <div className="settings-header">
            Settings
          </div>
          <div className="switch-container">
            <div className="toggle-text">
                Enable dark mode
            </div>
            <div className="settings-toggle">
              <Switch
                checked={props.theme.palette.type === 'dark'}
                onClick={props.toggleTheme}
                inputProps={{ 'aria-label': 'checkbox with default color' }}
                style={{color:'black'}}
                className="switch"
                classes={{
                  root: props.classes.root,
                  switchBase: props.classes.switchBase,
                  thumb: props.classes.thumb,
                  track: props.classes.track,
                  checked: props.classes.checked,
                }}
              />
            </div>
          </div>
        </MuiThemeProvider>
      </React.Fragment>
    )
 }

export default withTheme(Settings);