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
import { useQuery, gql } from "@apollo/client";
import "../components/SearchResults.css";

interface ISearchResultProps {
  query: string;
}

// interface IBookDetails {
//   fullName: string;
//   title: string;
//   id: string;
// }

const FIND_BY_TITLE = gql`
  query getByTitleCase($title: String!) {
    findByTitleIgnoreCase(title: $title) {
      id
      title
      authors {
        fullName
      }
    }
  }
`;

export default function SearchResults(props: ISearchResultProps): JSX.Element {
  const { data, loading, error } = useQuery(FIND_BY_TITLE, {
    variables: { title: props.query },
  });

  if (loading) {
    return <p>Loading...</p>;
  }
  if (error) {
    return <p>error{error.message}</p>;
  }

  const bookData = Object.values(data);

  return (
    <main className="query-result-container">
      <div className="query-result-book-image-container">
        {/* Placeholder image should be replaced
         with thumbnails from db once we have them available */}
        <img
          className="query-result-book-image"
          src="https://images-na.ssl-images-amazon.com/images/I/A1xkFZX5k-L.jpg"
          alt="Stephen Hawking on cover of his book Brief History of Time"
        />
      </div>

      {/* Todo: fix 'any' and replace with appropriate type */}
      {bookData.map((bookDetail: any) => (
        <div className="query-results-book" key={bookDetail.id}>
          <div className="query-result-book-title">
            <div>{bookDetail.title}</div>
            {bookDetail.authors.map((author: any) => (
              <div className="query-result-book-author" key={bookDetail.id}>
                <div>{author.fullName}</div>
              </div>
            ))}
          </div>
        </div>
      ))}
    </main>
  );
}
