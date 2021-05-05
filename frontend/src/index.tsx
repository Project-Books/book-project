/*
the book project lets a user keep track of different books they would like to read, are currently
reading, have read or did not finish.
copyright (c) 2020  karan kumar

this program is free software: you can redistribute it and/or modify it under the terms of the
gnu general public license as published by the free software foundation, either version 3 of the
license, or (at your option) any later version.

this program is distributed in the hope that it will be useful, but without any
warranty; without even the implied warranty of merchantability or fitness for a particular
purpose.  see the gnu general public license for more details.

you should have received a copy of the gnu general public license along with this program.
if not, see <https://www.gnu.org/licenses/>.
*/

import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';

ReactDOM.render(
  <React.StrictMode>
      <App />
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
