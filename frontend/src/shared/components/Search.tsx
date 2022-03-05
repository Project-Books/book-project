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

import React, { useState, useEffect } from "react";
import SearchIcon from "@material-ui/icons/Search";
import SearchResults, { IQueryResult } from "../components/SearchResults";
import { useLazyQuery, gql } from "@apollo/client";
import { Layout } from "../components/Layout";
import "./Search.css";

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

export default function Search(): JSX.Element {
  const [searchTerm, setSearchTerm] = useState("");
  const [getQueryResults, { data, loading, error }] =
    useLazyQuery(FIND_BY_TITLE);
  const [reformattedQueryData, setReformattedQueryData] = useState<
    IQueryResult[]
  >([]);

  useEffect(() => {
    if (!data) {
      return;
    }
    const duplicateDataCount = 8;
    const booksToRender: IQueryResult[] = Array(duplicateDataCount).fill(
      data.findByTitleIgnoreCase
    );
    setReformattedQueryData(booksToRender);
  }, [data]);

  if (loading) {
    return <p>Loading...</p>;
  }
  if (error) {
    return <p>error{error.message}</p>;
  }

  function handleChange(e: React.ChangeEvent<HTMLInputElement>) {
    e.preventDefault();
    setSearchTerm(e.target.value);
  }

  function onSearchBooks(e: React.SyntheticEvent) {
    e.preventDefault();
    getQueryResults({ variables: { title: searchTerm } });
  }

  return (
    <Layout title="Search">
      <form className="search-container">
        <label htmlFor="book-search" className="visually-hidden">
          Search for a book title, author or ISBN #
        </label>
        <input
          type="search"
          className="search-input"
          id="search"
          placeholder="Search for book title, author or ISBN 13"
          value={searchTerm}
          onChange={handleChange}
        />
        <button className="search-button" onClick={onSearchBooks}>
          <SearchIcon className="search-icon" />
        </button>
      </form>
      <div>
        <SearchResults query={reformattedQueryData} />
      </div>
    </Layout>
  );
}
