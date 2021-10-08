import React, {ReactElement} from 'react'
import './ShelfCarousel.css'
import { Icon } from '@material-ui/core';
import { Book } from '../types/Book';

function ShelfBook(props: BookProps) {
    const bookClass = 'book' + (props.img === "" ? '' : ' image');
    const titleClass = 'book-title' + (props.img === "" ? '' : ' hide');
    const imgURL =  props.img && 'url(' + props.img + ')';

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

export function ShelfCarousel(props: ShelfCarouselProps): ReactElement {
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
        for(let i = 0; i < books.length; i++) {
            elements.push(<ShelfBook  title={books[i].title} img={''} />)
        }
        return elements;
    }
}
type ShelfCarouselProps = {
    title: string;
    books: Book[];
}
