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

import React, { ReactElement } from 'react'
import './ShelfCarousel.css'
import { Icon } from '@material-ui/core';
import { Book } from '../types/Book';

function ShelfBook(props: BookProps): JSX.Element {
    const bookClass = 'book' + (props.img === "" ? '' : ' image');
    const titleClass = 'book-title' + (props.img === "" ? '' : ' hide');
    const imgURL = props.img && 'url(' + props.img + ')';

    return (
        <div className={bookClass} style={{ backgroundImage: imgURL }}>
            {(bookClass !== "book") && <div className="book-spine"></div>}
            <p className={titleClass}>{props.title}</p>
        </div>
    )
}

type BookProps = {
    title: string;
    img: string;
}

function AddBook() {
    return (
        <div className="book add-new">
            <Icon className="icon">add</Icon>
            <p className="book-title add-new">Add book</p>
        </div>
    )
}

export function ShelfCarousel(props: ShelfCarouselProps): JSX.Element {
    return (
        <div className="shelf-container">
            <span className="shelf-title">{props.title}</span>
            <span className="view-all">View all</span>
            <div className="clear" />
            <div className="books-and-shelf">
                <div className="book-wrap">
                    {
                        renderShelfBook(props.books)
                    }
                    <AddBook />
                    <div className="clear" />
                </div>
                <div className="shelf"></div>
            </div>
        </div>
    )

    function renderShelfBook(books: Book[]): ReactElement[] {
        const elements = Array<ReactElement>();
        for (let i = 0; i < books.length; i++) {
            elements.push(<ShelfBook key={i} title={books[i].title} img={''} />)
        }
        return elements;
    }
}
type ShelfCarouselProps = {
    title: string;
    books: Book[];
}
