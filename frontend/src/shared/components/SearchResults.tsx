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

interface ISearchResultProps {
  query: string;
}

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
    return <p>Loading</p>;
  }
  if (error) {
    return <p>error{error.message}</p>;
  }
  if (data) {
    console.log(data);
  }
  return <div className="query-results-container"></div>;
}
