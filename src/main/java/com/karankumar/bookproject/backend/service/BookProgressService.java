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

package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.RatingScale;
import com.karankumar.bookproject.backend.entity.book.BookProgress;
import com.karankumar.bookproject.backend.entity.book.BookProgressBuilder;
import com.karankumar.bookproject.backend.entity.book.BookProgressId;
import com.karankumar.bookproject.backend.entity.enums.State;
import com.karankumar.bookproject.backend.repository.BookRepository;
import com.karankumar.bookproject.backend.repository.ProgressRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.NotSupportedException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Log
public class BookProgressService {

    private final ProgressRepository progressRepository;
    private final BookRepository bookRepository;

    @Autowired
    public BookProgressService(BookRepository bookRepository,
                               ProgressRepository progressRepository) {
        this.bookRepository = bookRepository;
        this.progressRepository = progressRepository;
    }

    /**
     * Add a book to (to be read) state.
     *
     * @param id id
     * @return book progress
     */
    public BookProgress addToMyToBeReadState(BookProgressId id) {
        return progressRepository.save(new BookProgressBuilder()
                .withId(id)
                .withState(State.TO_BE_READ)
                .build());
    }

    /**
     * Start reading the book.
     *
     * @param id id
     * @return book progress
     * @throws Exception exception
     */
    public BookProgress startReading(BookProgressId id) throws Exception {
        BookProgress bookProgress = getMyBookProgressById(id);
        if (bookProgress.getState() == State.TO_BE_READ) {
            return progressRepository.save(new BookProgressBuilder()
                    .withId(id)
                    .withState(State.IN_PROGRESS)
                    .withPagesRead(0, getBook(id.getBookId()))
                    .withStartDate(LocalDate.now())
                    .build());
        } else {
            throw new NotSupportedException("You can't start reading a book with your current state");
        }
    }

    /**
     * Update my book progress
     *
     * @param id        id
     * @param pagesRead number of pages read
     * @return book progress
     * @throws Exception exception
     */
    public BookProgress updateMyBookProgress(BookProgressId id,
                                             Integer pagesRead) throws Exception {
        BookProgress bookProgress = getMyBookProgressById(id);
        if (bookProgress.getState() == State.IN_PROGRESS) {
            return progressRepository.save(new BookProgressBuilder()
                    .withId(id)
                    .withState(State.IN_PROGRESS)
                    .withPagesRead(pagesRead, getBook(id.getBookId()))
                    .withStartDate(bookProgress.getDateStartedReading())
                    .build());
        } else {
            throw new NotSupportedException("You can't update unless you're reading the book");
        }
    }

    /**
     * If we want to give a rate or review after e finish reading.
     *
     * @param id         id
     * @param rating     rating
     * @param bookReview review
     * @return book progress itself
     * @throws Exception If book isn't there i n DB.
     */
    public BookProgress doOnAfterfinishReading(BookProgressId id, RatingScale rating,
                                               String bookReview) throws Exception {
        BookProgress bookProgress = getMyBookProgressById(id);
        if (bookProgress.getState() == State.FINISHED) {
            return progressRepository.save(new BookProgressBuilder()
                    .withId(id)
                    .withState(State.FINISHED)
                    .withPagesRead(bookProgress.getPagesRead(), getBook(id.getBookId()))
                    .withStartDate(bookProgress.getDateStartedReading())
                    .withEndDate(bookProgress.getDateFinishedReading())
                    .withRating(rating)
                    .withBookReview(bookReview)
                    .build());
        } else {
            throw new NotSupportedException("You can't rate or review unless you finish the book!");
        }
    }

    /**
     * double check If book is there in database by book id.
     *
     * @param bookId book id
     * @return book if there or null
     */
    public Book getBook(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(()
                ->
                new NoSuchElementException("Book Not found In the database"));
    }

    /**
     * Getting all my books progress.
     *
     * @param userId User Id
     * @return List of my progress in each book.
     */
    public List<BookProgress> getMyBooksProgress(Long userId) {
        return progressRepository.findAll().stream()
                                 .filter(bookProgress ->
                                         bookProgress.getId().getUserId()
                                                     .equals(userId))
                                 .collect(Collectors.toList());

    }

    /**
     * get my book progress
     *
     * @param id book progress id
     * @return Book progress
     */
    public BookProgress getMyBookProgressById(BookProgressId id) {
        return progressRepository.findById(id)
                                 .orElseThrow(() ->
                                         new NoSuchElementException("You don't have any progress on this book"));
    }

}
