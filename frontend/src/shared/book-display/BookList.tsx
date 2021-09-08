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
import HttpClient from '../http/HttpClient';
import Endpoints from '../api/endpoints';
import './BookList.css';

interface BookListProps {}

interface BookListState {
  bookList: Book[];
}

export default class BookList extends Component <BookListProps, BookListState> {
  constructor(props: BookListProps) {
    super(props);
    this.state = {
      bookList: []
    }
  }

  componentDidMount():void {
    this.getBooks();
  }

  getBooks():void {
    HttpClient.get(Endpoints.books).then((response: Book[]) => {
      console.log('response', response);
      this.setState({
        bookList: response
      });
    })
    .catch((error: Record<string, string>) => {
      console.error('error: ', error);
    });
  }

  render():JSX.Element {
    return (
      <div className="booklist-container">
        <div className="booklist-container-headers">
          <div className="booklist-header-title">Title</div>
          <div className="booklist-header-author">Author</div>
          <div className="booklist-header-shelf">Shelf</div>
          <div className="booklist-header-genre">Genre</div>
          <div className="booklist-header-rating">Rating</div>
        </div>
        {this.state.bookList.map(book => (
          <div className="booklist-book" key={book.title}>
            <div className="booklist-book-thumbnail-container">
              <div className="booklist-book-thumbnail">
              </div>
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
