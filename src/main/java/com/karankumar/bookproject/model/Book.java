package com.karankumar.bookproject.model;

import java.util.Set;

/**
 * @author karan on 06/05/2020
 */
public class Book {

    private Long id;
    private String title;
    private float rating;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(RatingScale ratingScale) {
        switch (ratingScale) {
            case Zero:
                rating = 0;
                break;
            case ZeroPointFive:
                rating = 0.5f;
                break;
            case One:
                rating = 1;
                break;
            case OnePointFive:
                rating = 1.5f;
                break;
            case Two:
                rating = 2;
            case TwoPointFive:
                rating = 2.5f;
                break;
            case Three:
                rating = 3f;
                break;
            case ThreePointFive:
                rating = 3.5f;
                break;
            case Four:
                rating = 4;
                break;
            case FourPointFive:
                rating = 4.5f;
                break;
            case Five:
                rating = 5f;
                break;
        }
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
