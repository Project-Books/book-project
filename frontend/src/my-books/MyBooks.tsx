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

import React, { Component, ReactElement } from 'react'
import { NavBar } from '../shared/navigation/NavBar'
import { ShelfCarousel } from '../shared/book-display/ShelfCarousel'
import './MyBooks.css'
import Button from '@material-ui/core/Button';
import ShelfModal from './myBookModal/ShelfModal';
import Hidden from "@material-ui/core/Hidden";
import MenuIcon from '@material-ui/icons/Menu';

interface IState {
    showShelfModal: boolean,
}

class MyBooks extends Component<Record<string, unknown>, IState> {
    constructor(props: Record<string, unknown>) {
        super(props)
        this.state = {
        showShelfModal: false,
        }
        this.onAddShelf = this.onAddShelf.bind(this)
        this.onAddShelfModalClose = this.onAddShelfModalClose.bind(this)
    }

    onAddShelf(): void {
        this.setState({
            showShelfModal: true
        })
    }
    
    onAddShelfModalClose(): void {
        this.setState({
            showShelfModal: false,
        })
    }
    render(): ReactElement {
        return (
            <div>
                <NavBar />

                
                <Hidden smDown implementation="css">
                <div className="my-book-top">
                    <h1>My books</h1>
                    <div className="my-book-top-buttons">
                    <Button
                    variant="contained"
                    color="primary">
                        Add Book
                    </Button>
                    <Button
                     onClick={this.onAddShelf}
                    variant="contained"
                    color="primary">
                        Add Shelf
                    </Button>
                    </div>  
                    </div> 
                </Hidden>

                <Hidden mdUp implementation="css">
                    <MenuIcon className="my-book-top" />                              
                </Hidden>
         
                <div>
                    <ShelfCarousel title="Reading" />
                    <ShelfCarousel title="To Read" />
                    <ShelfCarousel title="Read" />
                    <ShelfCarousel title="Did not finish" />
                </div>
                <ShelfModal 
                    open={this.state.showShelfModal} 
                    onClose={this.onAddShelfModalClose}
                />
            </div>
        )
    }
}

export default MyBooks;
