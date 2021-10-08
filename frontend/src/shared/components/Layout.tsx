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
import { NavBar } from '../navigation/NavBar'
import ArrowBackIcon from '@material-ui/icons/ArrowBack';
import { useHistory } from "react-router-dom";
import './Layout.css'

export function Layout(props: LayoutProps): JSX.Element {
    const format = (props.centered ? "centered" : "");
    const history = useHistory();

    function handleClickToGoBack() {
        history.goBack();
    }

    return (
        <div className="layoutContainer">
            <div className="navBar">
                <NavBar />
            </div>
            <div className="pageContent">
                {props.showBackArrow === true &&
                    <div className="back-icon-button-container" onClick={handleClickToGoBack}>
                        <div className="arrow-back">
                            <ArrowBackIcon />
                        </div>
                        Back
                    </div>
                }
                <div className={format}>
                    <div className="pageHeader">
                        <h1 className="pageTitle">{props.title}</h1>
                        {props.btn}
                    </div>
                    {props.children}
                </div>
            </div>
        </div>
    )
}

type LayoutProps = {
    btn?: JSX.Element | JSX.Element[];
    title: string;
    centered?: boolean;
    children?: JSX.Element | JSX.Element[];
    showBackArrow?: boolean;
}