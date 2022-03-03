import React, { ReactElement } from 'react'
import { Paper } from '@material-ui/core';

const CHAR_LIMIT = 12;

type BookProps = {
    title: string;
    img: string;
}

function ShelfBook(props: BookProps): JSX.Element {
    const bookClass = 'book' + (props.img === "" ? '' : ' image');
    const displayTitle = props.title.length > CHAR_LIMIT ? 
                        (props.title.substring(0, CHAR_LIMIT) + "...") : props.title;

    return (
        <Paper className={bookClass} variant="elevation" square={false}>
            {(bookClass !== "book") && <div className="book-spine"></div>}
            {displayTitle}
        </Paper>
    )
}

export default ShelfBook;
