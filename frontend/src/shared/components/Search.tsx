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

import React, { useState } from 'react'
import SearchIcon from "@material-ui/icons/Search";
import { Layout } from '../components/Layout';
import {
  useQuery,
  gql,
} from "@apollo/client";
import './Search.css';

type Title =  {
  id: number,
  title: string,
}

export default function Search(): JSX.Element {
  const [searchTerm, setSearchTerm] = useState('');

  function handleChange(e: any) {
    e.preventDefault();
    setSearchTerm(e.target.value);
  }

  const FIND_BY_TITLE = gql`
   query getByTitleCase($title: String!) {
     findByTitleIgnoreCase(title:$title) {
        title
        id
     }
   }
`;

  function searchBooks() {
    const { data, loading, error } = useQuery(FIND_BY_TITLE, {
      variables: { title: 'A Brief History of Time' },
    });
    console.log('data', data)
    if (loading)  {
      return <div>Loading</div>;
    }
    if (error) {
      return <div>error{error.message}</div>;
    }
    return data.book.map((book: Title) => (
      <div key={book.id}>
      <p>{JSON.stringify(book.title)}</p>
      </div>
    ));
  }

searchBooks();
  return (
    <Layout title="Search">
      <div className="search-container">
        <div className="search-bar">
          <form>
            <input
              type="string"
              className="search-input"
              id="search"
              placeholder="Search for a book title, author, or ISBN #"
              value={searchTerm}
              onChange={handleChange}
            />
          </form>
          <div className="search-icon-container">
            <button className="search-button">
             <SearchIcon className="search-icon" onClick={searchBooks()} />
            </button>
          </div>
        </div>
      </div>
    </Layout>
  )
}

