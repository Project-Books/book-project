/*
The book project lets a user keep track of different books they would like to read, are currently
reading, have read or did not finish.
Copyright (C) 2021  Karan Kumar

This program is free software: you can redistribute it and/or modify it under the terms of the
GNU General Public License as published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
PURPOSE.  See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with this program.
If not, see <https://www.gnu.org/licenses/>.
*/

import React, { Component } from 'react'
import { Book } from '../types/Book';
import './BookList.css';

const CHAR_LIMIT = 40;

export interface BookListProps {
  bookListData: Book[];
}

export default class BookList extends Component <BookListProps> {

  render():JSX.Element {
    return (
      <div className="booklist-container">
        <div className="booklist-container-headers booklist-book">
          <div className="booklist-book-thumbnail"></div>
          <div className="booklist-book-title">Title</div>
          <div className="booklist-book-author">Author</div>
          <div className="booklist-book-shelf">Shelf</div>
          <div className="booklist-book-genre">Genre</div>
          <div className="booklist-book-rating">Rating</div>
        </div>
        {this.props.bookListData.map(book => (
          <div className="booklist-book" key={book.title}>
            <div className="booklist-book-thumbnail">
              {book.title.length > CHAR_LIMIT ?
                book.title.substring(0, CHAR_LIMIT) + "..." : book.title}
            </div>
            <div className="booklist-book-title">{book.title}</div>
            <div className="booklist-book-author">{book.author.fullName}</div>
            <div className="booklist-book-shelf">{book.predefinedShelf.shelfName}</div>
            <div className="booklist-book-genre">{book.bookGenre}</div>
            <div className="booklist-book-rating">{book.rating}</div>
          </div>
        ))}
      </div>
    )
  }
}
