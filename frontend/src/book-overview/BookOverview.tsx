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

import React, { Component } from "react";
import HttpClient from "../shared/http/HttpClient";
import { Book } from "../shared/types/Book";
import ArrowBackIcon from "@material-ui/icons/ArrowBack";
import { NavBar } from "../shared/navigation/NavBar";
import "./BookOverview.css";
import "../shared/components/Layout.css";
import { Create } from "@material-ui/icons";
import { History } from 'history';

interface Props {
  history: History;
  match: {
    params: {
      id: number;
    };
  };
}

interface IState {
  book: Book;
}

class BookOverview extends Component<Props, IState> {
  constructor(props: Props) {
    super(props);
    this.state = {
      book: {
        id: 0,
        title: "",
        img: "",
        predefinedShelf: {
          shelfName: "",
        },
        author: {
          fullName: "",
        },
        bookGenre: [],
        numberOfPages: 0,
        rating: 0,
      },
    };
    this.handleClickToGoBack = this.handleClickToGoBack.bind(this);
  }

  componentDidMount(): void {
    this.getBook();
  }

  handleClickToGoBack(): void {
    this.props.history.goBack();
  }

  async getBook(): Promise<void> {
    if (this.props.match) {
      await fetch(
        "http://localhost:3000/api/books/" + this.props?.match?.params?.id,
        {
          headers: HttpClient.getHeaders(),
        }
      )
        .then((res) => res.json())
        .then((data) => {
          this.setState({
            book: data,
          });
        })
        .catch((error: Record<string, string>) => {
          console.error("error: ", error);
        });
    }
  }

  render(): JSX.Element {
    return (
      <div className="layoutContainer">
        <div className="navBar">
          <NavBar />
        </div>
        <div className="pageContent">
          <div
            className="back-icon-button-container"
            onClick={this.handleClickToGoBack}
          >
            <div className="arrow-back">
              <ArrowBackIcon />
            </div>
            Back
          </div>
          <div className="row justify-content-center mt-4">
            <div className="col-8">
              <img
                className="book-image"
                // eslint-disable-next-line max-len
                src="https://inliterature.net/wp-content/uploads/2014/04/harry-potter-1-709x1024.jpg"
                alt="book image"
              />
              <h1 className="pageTitle bold">{this.state.book.title}</h1>
              <h5 className="authorName">{this.state.book.author.fullName}</h5>
              <p>{this.state.book.rating}</p>
              <p>
                <span className="shelfName">Shelf: </span>
                {this.state.book.predefinedShelf.shelfName}{" "}
                <Create className="pencil-icon" />
              </p>
            </div>
          </div>
          <div className="row book-details justify-content-center">
            <div className="col-8">
              <h5 className="bold">Book details</h5>
              <div className="row">
                <div className="col-2">
                  <span className="bold">Summary:</span>
                </div>
                <div className="col-10">No summary</div>
              </div>
              <div className="row">
                <div className="col-2">
                  <span className="bold">Genre(s):</span>
                </div>
                <div className="col-10">{this.state.book.bookGenre}</div>
              </div>
              <div className="row">
                <div className="col-2">
                  <span className="bold">Page count:</span>
                </div>
                <div className="col-10">
                  {this.state.book.numberOfPages} pages
                </div>
              </div>
              <div className="row">
                <div className="col-2">
                  <span className="bold">My review:</span>
                </div>
                <div className="col-10">
                  You have not submitted a review for this book!
                </div>
              </div>
              <div className="row">
                <div className="col-2"></div>
                <div className="col-10">
                  <a className="submit-review">Submit a review</a>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}

export default BookOverview;
