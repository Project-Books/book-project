import { type } from "os";
import React, { Component, ReactElement } from "react";
import Endpoints from "../api/endpoints";
import HttpClient from "../http/HttpClient";
import { Book } from "../types/Book";
import {ShelfCarousel} from "./ShelfCarousel";

interface IShelfState {
    readingBooks: Book[],
    toReadBooks: Book[],
    readBooks: Book[],
    unFinishedBooks: Book[]
}

export default class ShelfView extends Component<Record<string, unknown>, IShelfState> {
    constructor(props: Record<string, unknown>) {
        super(props);
        this.state = {
            unFinishedBooks: [],
            readBooks: [],
            readingBooks: [],
            toReadBooks: []
        };
    }

    componentDidMount() {
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
        HttpClient.get(Endpoints.didNotFinish).then((unFinishedBooks: Book[]) => {
            this.setState({
                unFinishedBooks: unFinishedBooks
            })
        });
    }

    render(): ReactElement {
        return (
            <div>
                <ShelfCarousel title="Reading" books={this.state.readingBooks} />
                <ShelfCarousel title="To Read" books={this.state.toReadBooks} />
                <ShelfCarousel title="Read" books={this.state.readBooks} />
                <ShelfCarousel title="Did not finish" books={this.state.unFinishedBooks} />
            </div>
        )
    }
}
