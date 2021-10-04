import React, {useState} from 'react'
import SearchIcon from "@material-ui/icons/Search";
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
    <div className="search-container">
      <h1 className="search-page-title">Search </h1>
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
  )
}
