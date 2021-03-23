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
import { url } from 'inspector';

function ShelfBook(props: BookProps) {
    const bookClass = 'book' + (props.img === "" ? '' : ' image');
    const titleClass = 'book-title' + (props.img === "" ? '' : ' hide');
    const imgURL = 'url(' + props.img + ')';

    return (
        <div className={bookClass} style={{ backgroundImage: imgURL }}>
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
            <p className="book-title add-new">Add new book</p>
        </div>
    )
}

export function ShelfCarousel(props: ShelfCarouselProps) {
    return (
        <div className="container">
            <div className="shelf-container">
                <span className="shelf-title">{props.title}</span>
                <span className="view-all">View all</span>
                <div className="clear" />

                <div className="books-and-shelf">
                    <div className="book-wrap">
                        <ShelfBook 
                            title="Harry Potter"
                            img="https://inliterature.net/wp-content/uploads/2014/04/harry-potter-1-709x1024.jpg" />
                        <ShelfBook title="Harry Potter" img="" />
                        <ShelfBook title="How Not to Die" img="" />
                        <ShelfBook 
                            title="Little Fires Everywhere" 
                            img="https://winterbroadhurst.files.wordpress.com/2019/05/little-fires.jpg" 
                        />
                        <ShelfBook title="Start With Why" img="" />
                        <ShelfBook title="Unbroken" img="" />
                        <AddBook />
                        <div className="clear" />
                    </div>
                    <div className="shelf"></div>
                </div>
            </div>
        </div>
    )
}

type ShelfCarouselProps = {
    title: string;
}