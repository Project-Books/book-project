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

    public BookProgressBuilder withPagesRead(Integer pagesRead, Book book) throws Exception {
        if (pagesRead > book.getNumberOfPages()){ // Updated request is more than book's pages.
            throw new Exception("Updated pages is more than book's pages");
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
