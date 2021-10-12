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
import Endpoints from "../api/endpoints";
import HttpClient from "../http/HttpClient";
import { Book } from "../types/Book";
import { ShelfCarousel } from "./ShelfCarousel";

interface IShelfState {
    readingBooks: Book[],
    toReadBooks: Book[],
    readBooks: Book[],
    didNotFinishBooks: Book[]
}


const SHELF_BOOKS: Book[] = [
    {
        title: "Harry Potter",
        img: "https://inliterature.net/wp-content/uploads/2014/04/harry-potter-1-709x1024.jpg",
        author: {
            fullName: ''
        },
        predefinedShelf: {
            shelfName: ''
        },
        bookGenre: [],
        rating: 0,
    },
    {
        title: "How Not to Die",
        img: "",
        author: {
            fullName: ''
        },
        predefinedShelf: {
            shelfName: ''
        },
        bookGenre: [],
        rating: 0,
    },
    {
        title: "Harry Potter and the Chamber of Secrets",
        img: "",
        author: {
            fullName: ''
        },
        predefinedShelf: {
            shelfName: ''
        },
        bookGenre: [],
        rating: 0,
    },
    {
        title: "Start With Why",
        img: "",
        author: {
            fullName: ''
        },
        predefinedShelf: {
            shelfName: ''
        },
        bookGenre: [],
        rating: 0,
    },
    {
        title: "Little Fires Everywhere",
        img: "https://winterbroadhurst.files.wordpress.com/2019/05/little-fires.jpg",
        author: {
            fullName: ''
        },
        predefinedShelf: {
            shelfName: ''
        },
        bookGenre: [],
        rating: 0,
    },
    {
        title: "Unbroken",
        img: "",
        author: {
            fullName: ''
        },
        predefinedShelf: {
            shelfName: ''
        },
        bookGenre: [],
        rating: 0,
    },
    {
        title: "Unbroken",
        img: "",
        author: {
            fullName: ''
        },
        predefinedShelf: {
            shelfName: ''
        },
        bookGenre: [],
        rating: 0,
    },
    {
        title: "Unbroken",
        img: "",
        author: {
            fullName: ''
        },
        predefinedShelf: {
            shelfName: ''
        },
        bookGenre: [],
        rating: 0,
    },
]



export default class ShelfView extends Component<Record<string, unknown>, IShelfState> {
    constructor(props: Record<string, unknown>) {
        super(props);
        this.state = {
            didNotFinishBooks: [...SHELF_BOOKS],
            readBooks: [...SHELF_BOOKS],
            readingBooks: [...SHELF_BOOKS],
            toReadBooks: [...SHELF_BOOKS]
        };
    }

    componentDidMount(): void {
        HttpClient.get(Endpoints.read).then((readBooks: Book[]) => {
            this.setState({
                readBooks: readBooks
            })
        });
        HttpClient.get(Endpoints.reading).then((readingBooks: Book[]) => {
            this.setState({
                readingBooks: readingBooks
            })
        });
        HttpClient.get(Endpoints.toRead).then((toReadBooks: Book[]) => {
            this.setState({
                toReadBooks: toReadBooks
            })
        });
        HttpClient.get(Endpoints.didNotFinish).then((didNotFinishBooks: Book[]) => {
            this.setState({
                didNotFinishBooks: didNotFinishBooks
            })
        });
    }

    render(): ReactElement {
        return (
            <div>
                <ShelfCarousel title="Reading" books={this.state.readingBooks} />
                <ShelfCarousel title="To Read" books={this.state.toReadBooks} />
                <ShelfCarousel title="Read" books={this.state.readBooks} />
                <ShelfCarousel title="Did not finish" books={this.state.didNotFinishBooks} />
            </div>
        )
    }
}
