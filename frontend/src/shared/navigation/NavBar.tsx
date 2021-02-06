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
import * as routes from '../routes'
import { Link } from 'react-router-dom'

function NavItem(props: NavItemProps) {
    return (
        <Link to={props.goTo}>
            <button>
                <div className="nav-item">
                    {props.icon}
                    <span className="nav-item-text">{props.itemText}</span>
                </div>
            </button>
        </Link>
    )
}

type NavItemProps = {
    icon: any;
    itemText: string;
    goTo: string;
}

export function NavBar(): JSX.Element {
    return (
        <div className="nav-bar">
            <div className="nav-top">
              <Link to={routes.HOME}>
                <img src={logo} alt="Logo" id="nav-bar-logo" />
              </Link>
            </div>
            <div className="nav-links" id="nav-links-top">
              <NavItem icon={<MenuBook />} itemText={"My books"} goTo={routes.MY_BOOKS} />
              <NavItem icon={<TrackChanges />} itemText={"Goal"} goTo={routes.GOAL} />
              <NavItem icon={<TrendingUp />} itemText={"Statistics"} goTo={routes.STATS} />
            </div>
            <div className="nav-links">
              <NavItem icon={<Settings />} itemText={"Settings"} goTo={routes.SETTINGS} />
              <NavItem icon={<ExitToApp />} itemText={"Log out"} goTo={routes.SIGN_IN} />
            </div>
        </div>
    )
}
