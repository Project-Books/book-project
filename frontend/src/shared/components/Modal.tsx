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
import React, {useEffect, useRef, useCallback} from 'react'
import '../components/Modal.css';
import EmailAddress from '../form/EmailAddress';

function Modal = ({ onClose, children}) {
  const Modal = useRef(null);
  
  return (
    <div className="modal-container">
      <div className="modal-body" >
        <div className="modal-text">
          Forgot Your Password
        </div>
        <div className="modal-text">
          We'll email you a link to reset your password
        </div>
      </div>
    </div>
  )
}
