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

import Login from "./login/Login";
import {useState} from 'react';
import React from 'react';
import { ThemeProvider } from '@material-ui/core/styles';
import {
    BrowserRouter,
    Route,
} from "react-router-dom";
import Register from "./register/Register";
import MyBooks from "./my-books/MyBooks";
import Settings from "./settings/Settings";
import Goal from "./goal/Goal";
import Stats from "./statistics/Stats";
import { theme as lightTheme, darkTheme} from './shared/theme';
import * as routes from "./shared/routes"

function App(): JSX.Element {
    const [theme, setTheme] = useState(lightTheme);

    function toggleTheme(): void {
        theme === lightTheme ? setTheme(darkTheme) : setTheme(lightTheme)
    } 
    return (
        <ThemeProvider theme={theme}>
          <BrowserRouter>
              <Route exact path={routes.HOME} component={Login} />
              <Route path={routes.SIGN_IN} component={Login} />
              <Route path={routes.SIGN_UP} component={Register} />
              <Route path={routes.MY_BOOKS} component={MyBooks} />
              <Route path={routes.GOAL} component={Goal} />
              <Route 
                path={routes.SETTINGS} 
                render={() => 
                <Settings  theme={theme} toggleTheme={toggleTheme} />} 
              />
              <Route path={routes.STATS} component={Stats} />
          </BrowserRouter>
        </ThemeProvider>
    )
}

export default App;