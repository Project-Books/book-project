/*
   The book project lets a user keep track of different books they would like to read, are currently
   reading, have read or did not finish.
   Copyright (C) 2020  Karan Kumar

   This program is free software: you can redistribute it and/or modify it under the terms of the
   GNU General Public License as published by the Free Software Foundation, either version 3 of the
   License, or (at your option) any later version.

   This program is distributed in the hope that it will be useful, but WITHOUT ANY
   WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
   PURPOSE.  See the GNU General Public License for more details.

   You should have received a copy of the GNU General Public License along with this program.
   If not, see <https://www.gnu.org/licenses/>.
*/

package com.karankumar.bookproject.book.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;

import com.karankumar.bookproject.ExcludeFromJacocoGeneratedReport;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@JsonIgnoreProperties(value = {"id", "books"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NamedEntityGraph(name = "Author.books", attributeNodes = @NamedAttributeNode("books"))
@ExcludeFromJacocoGeneratedReport
public class Author {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  private Long id;

  @NotBlank private String fullName;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "author")
  @Setter
  @ToString.Exclude
  private Set<Book> books = new HashSet<>();

  public Author(String fullName) {
    this.fullName = fullName;
  }

  @Override
  public String toString() {
    return fullName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Author author = (Author) o;
    return fullName.equals(author.fullName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fullName);
  }
}
