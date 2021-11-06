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

import React, { Component, ReactElement } from "react";
import { Book } from "../types/Book";
import ShelfCarousel from "./ShelfCarousel";

interface IShelfState {
    readingBooks: Book[];
    toReadBooks: Book[];
    readBooks: Book[];
    didNotFinishBooks: Book[];
    searchText: string;
}

export default class ShelfView extends Component<IShelfState, IShelfState> {
    constructor(props: IShelfState) {
        super(props);
        this.state = {
            didNotFinishBooks: props.didNotFinishBooks,
            readBooks: props.readBooks,
            readingBooks: props.readingBooks,
            toReadBooks: props.toReadBooks,
            searchText: props.searchText
        };
    }

    render(): ReactElement {
        return (
            <div>
                <ShelfCarousel 
                    title="Reading"
                    books={this.state.readingBooks}
                    searchText={this.state.searchText} />
                <ShelfCarousel 
                    title="To Read" 
                    books={this.state.toReadBooks}
                    searchText={this.state.searchText} />
                <ShelfCarousel 
                    title="Read"
                    books={this.state.readBooks}
                    searchText={this.state.searchText} />
                <ShelfCarousel 
                    title="Did not finish"
                    books={this.state.didNotFinishBooks}
                    searchText={this.state.searchText} />
            </div>
        )
    }
}
