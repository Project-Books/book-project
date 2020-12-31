import React from "react";
import Login from "./login/Login";
import {
    BrowserRouter,
    Route,
} from "react-router-dom";
import Register from "./register/Register";

function App() {
    return (
        <BrowserRouter>
            <Route exact path='/' component={Login} />
            <Route path='/sign-in' component={Login} />
            <Route path='/sign-up' component={Register} />
        </BrowserRouter>
    )
}

export default App;