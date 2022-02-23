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
import { NavBar } from "../shared/navigation/NavBar";
import Switch from "../settings/Switch";
import Button from "@material-ui/core/Button";
import ShelfModal from "./ShelfModal";
import { Layout } from "../shared/components/Layout";
import BookList from '../shared/book-display/BookList';
import { Book } from '../shared/types/Book';
import HttpClient from '../shared/http/HttpClient';
import Endpoints from '../shared/api/endpoints';
import "./MyBooks.css";
import ShelfView from "../shared/book-display/ShelfView";


interface IState {
    showShelfModal: boolean;
    showListView: boolean;
    bookList: Book[];
    readBooks: Book[];
    didNotFinishBooks: Book[];
    toReadBooks: Book[];
    readingBooks: Book[];
    searchVal: string;
}


class MyBooks extends Component<Record<string, unknown>, IState> {
    constructor(props: Record<string, unknown>) {
        super(props);
        this.state = {
            showShelfModal: false,
            showListView: false,
            bookList: [],
            readBooks: [],
            didNotFinishBooks: [],
            toReadBooks: [],
            readingBooks: [],
            searchVal: ''
        };
        this.onAddShelf = this.onAddShelf.bind(this);
        this.onAddShelfModalClose = this.onAddShelfModalClose.bind(this);
        this.onToggleListView = this.onToggleListView.bind(this);
        this.getBooks = this.getBooks.bind(this);
        this.getDidNotFinishBooks = this.getDidNotFinishBooks.bind(this);
        this.toReadBooks = this.toReadBooks.bind(this);
        this.readingBooks = this.readingBooks.bind(this);
        this.getReadBooks = this.getReadBooks.bind(this);
    }

    componentDidMount(): void {
        this.getBooks();
        this.getReadBooks();
        this.getDidNotFinishBooks();
        this.toReadBooks();
        this.readingBooks();
        this.trackCurrentDeviceSize();
    }

    getReadBooks(): void {
        HttpClient.get(Endpoints.read).then((readBooks: Book[]) => {
            this.setState(state => ({
                readBooks: Array.isArray(readBooks) ? readBooks : state.readBooks
            }));
        }).catch((error: Record<string, string>) => {
            console.error('error: ', error);
        });
    }

    getDidNotFinishBooks(): void {
        HttpClient.get(Endpoints.didNotFinish).then((didNotFinishBooks: Book[]) => {
            this.setState(state => ({
                didNotFinishBooks: Array.isArray(didNotFinishBooks)
                    ? didNotFinishBooks : state.didNotFinishBooks
            }));
        }).catch((error: Record<string, string>) => {
            console.error('error: ', error);
        });
    }

    toReadBooks(): void {
        HttpClient.get(Endpoints.toRead).then((toReadBooks: Book[]) => {
            this.setState(state => ({
                toReadBooks: Array.isArray(toReadBooks) ? toReadBooks : state.toReadBooks
            }));
        }).catch((error: Record<string, string>) => {
            console.error('error: ', error);
        });
    }

    readingBooks(): void {
        HttpClient.get(Endpoints.reading).then((readingBooks: Book[]) => {
            this.setState(state => ({
                readingBooks: Array.isArray(readingBooks) ? readingBooks : state.readingBooks
            }));
        }).catch((error: Record<string, string>) => {
            console.error('error: ', error);
        });
    }

    getBooks(): void {
        HttpClient.get(Endpoints.books).then((response: Book[]) => {
            this.setState({
                bookList: response
            });
        })
            .catch((error: Record<string, string>) => {
                console.error('error: ', error);
            });
    }

    onAddShelf(): void {
        this.setState({
            showShelfModal: true,
        });
    }

    trackCurrentDeviceSize(): void {
        window.onresize = (): void => {
            if (window.matchMedia("(max-width: 800px)").matches) {
                this.setState({ showListView: true })
            } else {
                this.setState({ showListView: false })
            }
        }
        return
    }

    onAddShelfModalClose(): void {
        this.setState({
            showShelfModal: false,
        });
    }

    onToggleListView(): void {
        this.setState({
            showListView: !this.state.showListView
        });
    }

    render(): ReactElement {
        return (
            <Layout title="My books" btn={<div className="my-book-top-buttons">
                <Button
                    variant="contained"
                    className="tempButton"
                    color="primary"
                    disableElevation
                >
                    Add Book
            </Button>
                <Button
                    onClick={this.onAddShelf}
                    variant="contained"
                    color="primary"
                    disableElevation
                >
                    Add Shelf
            </Button>
            </div>}>
                <NavBar />
                <div>
                    {
                        this.state.showListView ? (
                            <BookList 
                                key={this.state.bookList.length + this.state.searchVal}
                                bookListData={this.state.bookList}
                                searchText={this.state.searchVal} />
                        ) :
                            <ShelfView
                                key={[
                                    ...this.state.readBooks,
                                    ...this.state.readingBooks,
                                    ...this.state.toReadBooks,
                                    ...this.state.didNotFinishBooks
                                ].length + this.state.searchVal}
                                readBooks={this.state.readBooks} 
                                toReadBooks={this.state.toReadBooks}
                                didNotFinishBooks={this.state.didNotFinishBooks}
                                readingBooks={this.state.readingBooks} 
                                searchText={this.state.searchVal} />
                    }
                </div>
                <ShelfModal
                    open={this.state.showShelfModal}
                    onClose={this.onAddShelfModalClose}
                />
                <div className="my-book-switch-container">
                    <div className="toggle-text">
                        Shelf View
                    </div>
                    <Switch onClick={this.onToggleListView} />
                    <div className="toggle-text">
                        List View
                    </div>
                </div>
            </Layout>
        );
    }
}
export default MyBooks;
