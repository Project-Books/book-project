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

import React, {useState} from 'react'
import SearchIcon from "@material-ui/icons/Search";
import { Layout } from '../components/Layout';
import './Search.css';

export default function Search(): JSX.Element {
  const [searchTerm, setSearchTerm] = useState('');

  function handleChange(e: any) {
    e.preventDefault();
    setSearchTerm(e.target.value);
  }

  // Create function that calls findByTitle/passes in search query
  // Who is parent component
  // Should I create another method in HttpClient for this?
  return (
    <Layout title="Search">
    <div className="search-container">
      <div className="search-bar">
        <input
          type="text"
          className="search-input"
          placeholder="Search for a book title, author, or ISBN #"
          value={searchTerm}
          onChange={handleChange}
          onFocus={e => e.target.placeholder = ''}
        />
        <div className="search-icon-container">
          <SearchIcon className="search-icon" />
        </div>
      </div>
    </div>
    </Layout>
  )
}
