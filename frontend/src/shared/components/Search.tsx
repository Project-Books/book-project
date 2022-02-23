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

import React, { useState } from "react";
import SearchIcon from "@material-ui/icons/Search";
import SearchResults from "../components/SearchResults";
import { Layout } from "../components/Layout";
import "./Search.css";

export default function Search(): JSX.Element {
  const [searchTerm, setSearchTerm] = useState("");
  const [searchQuery, setSearchQuery] = useState("");

  function handleChange(e: React.ChangeEvent<HTMLInputElement>) {
    e.preventDefault();
    setSearchTerm(e.target.value);
  }

  function onSearchBooks(e: React.SyntheticEvent) {
    e.preventDefault();
    setSearchQuery(searchTerm);
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
        <SearchResults query={searchQuery} />
      </div>
    </Layout>
  );
}
