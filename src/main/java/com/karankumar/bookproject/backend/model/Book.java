package com.karankumar.bookproject.backend.model;

import java.util.Set;

/**
 * @author karan on 06/05/2020
 */
public class Book {

    private Long id;

    private String title;

    private RatingScale rating;
    private String favouriteQuote;
    private Set<Author> authors;
    private Set<Shelf> shelves;

    private enum RatingScale {
        Zero,
        ZeroPointFive,
        One,
        OnePointFive,
        Two,
        TwoPointFive,
        Three,
        ThreePointFive,
        Four,
        FourPointFive,
        Five,
    }

    public Book() {
    }

    public Book(String title, Set<Author> authors) {
        this.title = title;
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RatingScale getRating() {
        return rating;
    }

    public void setRating(RatingScale rating) {
        this.rating = rating;
    }

    public String getFavouriteQuote() {
        return favouriteQuote;
    }

    public void setFavouriteQuote(String favouriteQuote) {
        this.favouriteQuote = favouriteQuote;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Set<Shelf> getShelves() {
        return shelves;
    }

    public void setShelves(Set<Shelf> shelves) {
        this.shelves = shelves;
    }
}
