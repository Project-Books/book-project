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
import {
    BrowserRouter,
    Route,
} from "react-router-dom";
import Register from "./register/Register";
import MyBooks from "./my-books/MyBooks";
import Settings from "./settings/Settings";
import Goal from "./goal/Goal";
import Stats from "./statistics/Stats";

function App() {
    return (
        <BrowserRouter>
            <Route exact path='/' component={Login} />
            <Route path='/sign-in' component={Login} />
            <Route path='/sign-up' component={Register} />
            <Route path='/my-books' component={MyBooks} />
            <Route path='/goal' component={Goal} />
            <Route path='/settings' component={Settings} />
            <Route path='/stats' component={Stats} />
        </BrowserRouter>
    )
}

export default App;