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
import './NavBar.css'
import { ExitToApp, MenuBook, Settings, TrackChanges, TrendingUp } from '@material-ui/icons'
import logo from '../media/logo/logo-two-lines-white@1x.png'

function NavItem(props: NavItemProps) {
    return (
        <button>
            <div className="nav-item">
                {props.icon}
                <span className="nav-item-text">{props.itemText}</span>
            </div>
        </button>
    )
}

type NavItemProps = {
    icon: any;
    itemText: string;
}

export function NavBar() {
    return (
        <div className="nav-bar">
            <img src={logo} alt="Logo" id="nav-bar-logo" />

            <NavItem icon={<MenuBook />} itemText={"My books"}/>
            <NavItem icon={<TrackChanges />} itemText={"Goal"}/>
            <NavItem icon={<TrendingUp />} itemText={"Statistics"}/>

            <div id="gap"></div>

            <NavItem icon={<Settings />} itemText={"Settings"}/>
            <NavItem icon={<ExitToApp />} itemText={"Log out"}/>
        </div>
    )
}
