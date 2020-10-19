package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.entity.RatingScale;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.book.BookProgress;
import com.karankumar.bookproject.backend.entity.book.BookProgressBuilder;
import com.karankumar.bookproject.backend.entity.book.BookProgressId;
import com.karankumar.bookproject.backend.entity.enums.State;
import com.karankumar.bookproject.backend.repository.BookRepository;
import com.karankumar.bookproject.backend.repository.ProgressRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is used to handle progress of user's books.
 * @author hassan
 */
@Service
@Log
public class BookProgressService {

    private final ProgressRepository progressRepository;
    private final BookRepository bookRepository;

    @Autowired
    public BookProgressService(BookRepository bookRepository, ProgressRepository progressRepository) {
        this.bookRepository = bookRepository;
        this.progressRepository = progressRepository;
    }

    /**
     * Add a book to (to be read) state.
     * @param bookId book id
     * @param userId user id
     * @return book progress
     */
    public BookProgress addToMyToBeReadState(Long bookId, Long userId) {
        return progressRepository.save(new BookProgressBuilder()
                .withId(new BookProgressId(bookId, userId))
                .withState(State.TO_BE_READ)
                .build());
    }

    /**
     * Start reading the book.
     * @param bookId book id
     * @param userId user id
     * @return book progress
     * @throws Exception exception
     */
    public BookProgress startReading(Long bookId, Long userId) throws Exception {
        if (progressRepository.findById(new BookProgressId(bookId, userId)).isPresent()) {
            BookProgress bookProgress = progressRepository.findById(new BookProgressId(bookId, userId)).get();
            if (bookProgress.getState() == State.TO_BE_READ) {
                return progressRepository.save(new BookProgressBuilder()
                        .withId(new BookProgressId(bookId, userId))
                        .withState(State.IN_PROGRESS)
                        .withPagesRead(0, getBook(bookId))
                        .withStartDate(LocalDate.now())
                        .build());
            } else throw new Exception("You can't start reading a book that isn't in to be read");
        }
        return null;
    }

    /**
     * Update my book progress
     * @param bookId book id
     * @param userId user id
     * @param pagesRead number of pages read
     * @return book progress
     * @throws Exception exception
     */
    public BookProgress updateMyBookProgress(Long bookId, Long userId, Integer pagesRead) throws Exception {
        if (progressRepository.findById(new BookProgressId(bookId, userId)).isPresent()) {
            BookProgress bookProgress = progressRepository.findById(new BookProgressId(bookId, userId)).get();
            if (bookProgress.getState() == State.IN_PROGRESS) {
                return progressRepository.save(new BookProgressBuilder()
                        .withId(new BookProgressId(bookId, userId))
                        .withState(State.IN_PROGRESS)
                        .withPagesRead(pagesRead, getBook(bookId))
                        .withStartDate(bookProgress.getDateStartedReading())
                        .build());
            } else throw new Exception("You can't update your progress unless you're reading the book");
        }
        return null;

    }

    /**
     * If we want to give a rate or review after e finish reading.
     * @param bookId book id
     * @param userId user id
     * @param rating rating
     * @param bookReview review
     * @return book progress itself
     * @throws Exception If book isn't there i n DB.
     */
    public BookProgress doOnAfterfinishReading(Long bookId, Long userId, RatingScale rating, String bookReview) throws Exception {
        if (progressRepository.findById(new BookProgressId(bookId, userId)).isPresent()) {
            BookProgress bookProgress = progressRepository.findById(new BookProgressId(bookId, userId)).get();
            if (bookProgress.getState() == State.FINISHED) {
                return progressRepository.save(new BookProgressBuilder()
                        .withId(new BookProgressId(bookId, userId))
                        .withState(State.FINISHED)
                        .withPagesRead(bookProgress.getPagesRead(), getBook(bookId))
                        .withStartDate(bookProgress.getDateStartedReading())
                        .withEndDate(bookProgress.getDateFinishedReading())
                        .withRating(rating)
                        .withBookReview(bookReview)
                        .build());
            } else throw new Exception("You can't rate or review unless you finish the book!");
        }
        return null;
    }

    /**
     * double check If book is there in database by book id.
     * @param bookId book id
     * @return book if there or null
     */
    public Book getBook(Long bookId){
        return bookRepository.findById(bookId).orElse(null);
    }

    /**
     * Getting all my books progress.
     * @param userId User Id
     * @return List of my progress in each book.
     */
    public List<BookProgress> getMyBooksProgress(Long userId){
        return progressRepository.findAll().stream().filter(bookProgress -> bookProgress.getId().getUserId().equals(userId)).collect(Collectors.toList());

    }

}
