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

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

/**
 * A {@code Book} object represents a single book with its corresponding metadata, such as an Author, genre and rating
 */
@Entity
public class Book extends BaseEntity {

    @NotNull
    @NotEmpty
    private String title;

    private int numberOfPages;

    private Genre genre;

    // For books that have been read
    private RatingScale rating;
    private LocalDate dateStartedReading;
    private LocalDate dateFinishedReading;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "ID")
    private Author author;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Shelf> shelves;

    public Book() {
    }

    public Book(String title, Author author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public RatingScale getRating() {
        return rating;
    }

    public void setRating(RatingScale rating) {
        this.rating = rating;
    }

    public Set<Shelf> getShelves() {
        return shelves;
    }

    public void setShelves(Set<Shelf> shelves) {
        this.shelves = shelves;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public LocalDate getDateStartedReading() {
        return dateStartedReading;
    }

    public void setDateStartedReading(LocalDate dateStartedReading) {
        this.dateStartedReading = dateStartedReading;
    }

    public LocalDate getDateFinishedReading() {
        return dateFinishedReading;
    }

    public void setDateFinishedReading(LocalDate dateFinishedReading) {
        this.dateFinishedReading = dateFinishedReading;
    }
}
