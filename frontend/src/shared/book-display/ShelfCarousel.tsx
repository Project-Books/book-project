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
import './ShelfCarousel.css'
import { Icon } from '@material-ui/core';

function ShelfBook(props: BookProps): JSX.Element {
    const bookClass = 'book' + (props.img === "" ? '' : ' image');
    const titleClass = 'book-title' + (props.img === "" ? '' : ' hide');
    const imgURL = props.img && 'url(' + props.img + ')';

    return (
        <div className={bookClass} style={{ backgroundImage: imgURL }}>
            {(bookClass!=="book")&&<div className="book-spine"></div>}
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

const SHELF_BOOKS: JSX.Element[] = [
    <ShelfBook
        key={0}
        title="Harry Potter"
        img="https://inliterature.net/wp-content/uploads/2014/04/harry-potter-1-709x1024.jpg" />,
    <ShelfBook key={1} title="Harry Potter and the Chamber of Secrets" img="" />,
    <ShelfBook key={2} title="How Not to Die" img="" />,
    <ShelfBook
        key={3}
        title="Little Fires Everywhere"
        img="https://winterbroadhurst.files.wordpress.com/2019/05/little-fires.jpg"
    />,
    <ShelfBook key={4} title="Start With Why" img="" />,
    <ShelfBook key={5} title="Unbroken" img="" />,
    <ShelfBook key={6} title="Unbroken" img="" />,
    <ShelfBook key={7} title="Unbroken" img="" />
]

export function ShelfCarousel(props: ShelfCarouselProps) {
    return (
        <div className="shelf-container">
            <span className="shelf-title">{props.title}</span>
            <span className="view-all">View all</span>
            <div className="clear" />

            <div className="books-and-shelf">
                <div className="book-wrap">
                    {
                        SHELF_BOOKS.map((shelfBook, index) => index < 6 ? shelfBook : null)
                    }
                    <AddBook />
                    <div className="clear" />
                </div>
                <div className="shelf"></div>
            </div>
        </div>
    )
}

type ShelfCarouselProps = {
    title: string;
}
