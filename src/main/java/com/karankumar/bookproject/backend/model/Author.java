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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * A {@code Author} object represents a single Author with its corresponding metadata, such as a first name, last name
 * and a list of books
 */
@Entity
public class Author extends BaseEntity {

    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;

//    @ManyToMany
//    private List<Book> books;

//    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
//    private List<Book> books = new LinkedList<>();

    public Author() {
    }

//    public Author(String firstName, String lastName, List<Book> books) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.books = books;
//    }

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

//    public List<Book> getBooks() {
//        return books;
//    }

//    public void setBooks(List<Book> books) {
//        this.books = books;
//    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
