package com.karankumar.bookproject.model;

import java.util.Set;

/**
 * @author karan on 06/05/2020
 */
public class Shelf {
    private Long id;
    private String name;
    private Set<Book> books;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }
}
