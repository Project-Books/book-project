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
import { NavBar } from '../shared/navigation/NavBar';
import ColoredSwitch from '../settings/Switch';
import { withTheme,MuiThemeProvider} from '@material-ui/core/styles';
import CssBaseline from '@material-ui/core/CssBaseline';
import './Settings.css'

interface ISettingsProps {
    theme: any;
    toggleTheme: () => void;
}

 function Settings(props: ISettingsProps): JSX.Element {
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
              <ColoredSwitch
                checked={props.theme.palette.type === 'dark'}
                onClick={props.toggleTheme}
                inputProps={{ 'aria-label': 'checkbox with default color' }}
                style={{color:'black'}}
                className="switch"
              />
            </div>
          </div>
        </MuiThemeProvider>
      </React.Fragment>
    )
 }

export default withTheme(Settings);
