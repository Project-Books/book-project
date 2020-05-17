package com.karankumar.bookproject.backend.model;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.LinkedList;
import java.util.List;

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

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    private List<Book> books = new LinkedList<>();

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

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
