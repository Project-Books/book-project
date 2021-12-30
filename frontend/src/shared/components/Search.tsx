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
  authors: string
}

const FIND_BY_TITLE = gql`
  query getByTitleCase($title: String!) {
    findByTitleIgnoreCase(title:$title) {
        id
        title
        authors {
          fullName
        }
    }
  }
`;

export default function Search(): JSX.Element {
  const [searchTerm, setSearchTerm] = useState('');
  const { data, loading, error } = useQuery(FIND_BY_TITLE, {
    variables: { title: 'A Brief History of Time' },
  });

  function handleChange(e: any) {
    e.preventDefault();
    setSearchTerm(e.target.value);
  }

  function onSearchBooks(e:any) {
    e.preventDefault();
    if (loading)  {
      return <div>Loading</div>;
    }
    if (error) {
      return <div>error{error.message}</div>;
    }

    return (
      <div>
       {data.book.map((book: Title) => (
        <div key={book.id}>
        <p>{book.title}</p>
        <p>{book.authors}</p>
        </div>
       ))};
      </div>
    )
  }
  return (
    <Layout title="Search">
      <form className="search-container">
        <label
          htmlFor="book-search"
          className="visually-hidden">
          Search for a book title, author or ISBN #
        </label>
        <input
          type="search"
          className="search-input"
          id="search"
          placeholder="Search for a book title, author, or ISBN #"
          value={searchTerm}
          onChange={handleChange}
        />
        <button className="search-button" onClick={onSearchBooks}>
          <SearchIcon className="search-icon" />
        </button>
      </form>
    </Layout>
  )
}
