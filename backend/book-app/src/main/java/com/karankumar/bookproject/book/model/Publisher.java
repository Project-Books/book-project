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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotBlank;

import com.karankumar.bookproject.ExcludeFromJacocoGeneratedReport;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(
    name = "publisher",
    uniqueConstraints = {@UniqueConstraint(name = "publisher_name_unique", columnNames = "name")})
@Getter
@Setter
@JsonIgnoreProperties(value = {"id", "books"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@NamedEntityGraph(name = "Publisher.books", attributeNodes = @NamedAttributeNode("books"))
@ExcludeFromJacocoGeneratedReport
public class Publisher {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  private Long id;

  @NotBlank
  @Column(unique = true)
  private String name;

  @ManyToMany(fetch = FetchType.LAZY, mappedBy = "publishers")
  @ToString.Exclude
  private Set<Book> books = new HashSet<>();

  public Publisher(@NotBlank String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return this.name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Publisher publisher = (Publisher) o;
    return name.equals(publisher.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
