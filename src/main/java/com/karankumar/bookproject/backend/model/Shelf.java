/*
    The book project lets a user keep track of different books they've read, are currently reading or would like to read
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.karankumar.bookproject.backend.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * Represents a shelf (or a list) of books (e.g. books in a 'to read' shelf)
 */

@Entity
public class Shelf extends BaseEntity {
    @NotNull
    @NotEmpty
    private String name;

//    @ManyToMany(fetch = FetchType.EAGER)
//    private List<Book> books;

//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "shelves")
//    private List<Book> books;

//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "shelves")
//    private Set<Book> books;

//    @ManyToMany(fetch = FetchType.EAGER)
//    private Set<Book> books;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "shelves")
    private Set<Book> books;

    public Shelf() {
    }

    public Shelf(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public List<Book> getBooks() {
//        return books;
//    }
//
//    public void setBooks(List<Book> books) {
//        this.books = books;
//    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }
}
