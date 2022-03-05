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

import React from "react";
import booksNotFoundImage from "../../images/book-not-found.png";
import "../components/SearchResults.css";

interface ISearchResultProps {
  query: IQueryResult[];
}

export interface IQueryResult {
  id: string;
  title: string;
  authors: {
    fullName: string;
  }[];
}

export default function SearchResults(props: ISearchResultProps): JSX.Element {
  console.log(props);
  const { query } = props;

  return (
    <main className="query-result-container">
      {query ? (
        query.map((bookDetail: IQueryResult) => (
          <div className="query-result-book" key={bookDetail.id}>
            <img
              className="query-result-book-image"
              src="https://images-na.ssl-images-amazon.com/images/I/A1xkFZX5k-L.jpg"
              alt="Stephen Hawking on cover of his book Brief History of Time"
            />
            <div className="query-result-book-title">
              <p>{bookDetail.title}</p>
            </div>
            {bookDetail.authors.map((author) => (
              <div className="query-result-book-author" key={author.fullName}>
                <p>{author.fullName}</p>
              </div>
            ))}
          </div>
        ))
      ) : (
        <div className="results-not-found-container">
          <img
            className="no-books-found-image"
            src={booksNotFoundImage}
            alt="Question mark shown along with empty pages shown to signify no books found"
          />
        </div>
      )}
    </main>
  );
}
