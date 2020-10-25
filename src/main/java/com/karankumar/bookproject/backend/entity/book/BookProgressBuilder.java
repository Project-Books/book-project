/**
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2020  Karan Kumar
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.entity.book;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.RatingScale;
import com.karankumar.bookproject.backend.entity.enums.State;

import java.time.LocalDate;

public class BookProgressBuilder {

    private final BookProgress bookProgress;

    public BookProgressBuilder() {
        this.bookProgress = new BookProgress();
    }

    public BookProgressBuilder withRating(RatingScale rating) {
        if (bookProgress.getState() == State.FINISHED) {
            bookProgress.setRating(rating);
        }

        return this;
    }

    public BookProgressBuilder withStartDate(LocalDate date) {
        bookProgress.setDateStartedReading(date);
        return this;
    }

    public BookProgressBuilder withEndDate(LocalDate date) {
        bookProgress.setDateFinishedReading(date);
        return this;
    }

    public BookProgressBuilder withBookReview(String bookReview) {
        if (bookProgress.getState() == State.FINISHED) {
            bookProgress.setBookReview(bookReview);
        }
        return this;
    }

    public BookProgressBuilder withPagesRead(Integer pagesRead, Book book)  {
        if (pagesRead > book.getNumberOfPages()) { // Updated request is more than book's pages.
            throw new IndexOutOfBoundsException("Updated pages is more than book's pages");
        } else if (pagesRead.equals(book.getNumberOfPages())) { // I finished the book
            bookProgress.setState(State.FINISHED);
            bookProgress.setPagesRead(pagesRead);
            bookProgress.setDateFinishedReading(LocalDate.now());
        } else { // I'm still reading the book
            bookProgress.setPagesRead(pagesRead);
        }
        return this;
    }

    public BookProgressBuilder withState(State state) {
        bookProgress.setState(state);
        return this;
    }

    public BookProgressBuilder withId(BookProgressId id) {
        bookProgress.setId(id);
        return this;
    }

    public BookProgress build() {
        return this.bookProgress;
    }

}
