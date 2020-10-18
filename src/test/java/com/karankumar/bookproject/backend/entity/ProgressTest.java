package com.karankumar.bookproject.backend.entity;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.account.Role;
import com.karankumar.bookproject.backend.entity.account.User;
import com.karankumar.bookproject.backend.entity.book.Book;
import com.karankumar.bookproject.backend.entity.book.BookProgress;
import com.karankumar.bookproject.backend.entity.enums.State;
import com.karankumar.bookproject.backend.service.BookProgressService;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
class ProgressTest {

    private final BookService bookService;

    private final PredefinedShelfService predefinedShelfService;

    private final BookProgressService bookProgressService;

    private static Book testBook1;
    private static PredefinedShelf toRead;
    private static User user;

    @Autowired
    ProgressTest(BookService bookService, PredefinedShelfService predefinedShelfService, BookProgressService bookProgressService) {
        this.bookService = bookService;
        this.predefinedShelfService = predefinedShelfService;
        this.bookProgressService = bookProgressService;
    }


    @BeforeEach
    public void setUp() {
        toRead = predefinedShelfService.findToReadShelf();
        testBook1 = createBook("How the mind works", toRead);
        user = new User(new Random().nextLong(), "hassan", "hassan.elseoudy@gmail.com", "sj%kfn%ui%4#3#78t43uw3n@%", true, Collections.singleton(new Role("role")));
        resetBookService();
        saveBooks();
    }

    private void saveBooks() {
        bookService.save(testBook1);
    }

    private void resetBookService() {
        bookService.deleteAll();
    }

    private static Book createBook(String title, PredefinedShelf shelf) {
        Author author = new Author("Hassan", "Elseoudy");
        return new Book(title, author, shelf);
    }

    @Test
    @DisplayName("Checking Adding to My List")
    @Order(1)
    void addToMyToBeReadList() {
        bookProgressService.addToMyToBeReadState(testBook1.getId(), user.getId());
        List<BookProgress> myProgress = bookProgressService.getMyBooksProgress(user.getId());
        Assertions.assertAll("Assert valid insertion in my to be read",
                () -> assertEquals(1, myProgress.size()),
                () -> assertEquals(testBook1.getId(), myProgress.get(0).getId().getBookId()),
                () -> assertEquals(user.getId(), myProgress.get(0).getId().getUserId()),
                () -> assertEquals(State.TO_BE_READ, myProgress.get(0).getState()),
                () -> assertNull(myProgress.get(0).getBookReview()),
                () -> assertNull(myProgress.get(0).getDateFinishedReading()),
                () -> assertNull(myProgress.get(0).getDateStartedReading()),
                () -> assertNull(myProgress.get(0).getPagesRead()),
                () -> assertNull(myProgress.get(0).getRating())
        );
    }

    @Test
    @DisplayName("Checking Start Reading")
    @Order(2)
    void startReadingTest() throws Exception {
        bookProgressService.addToMyToBeReadState(testBook1.getId(), user.getId());
        bookProgressService.startReading(testBook1.getId(), user.getId());
        List<BookProgress> myProgress = bookProgressService.getMyBooksProgress(user.getId());
        Assertions.assertAll("Assert valid insertion in start reading list",
                () -> assertEquals(1, myProgress.size()),
                () -> assertEquals(testBook1.getId(), myProgress.get(0).getId().getBookId()),
                () -> assertEquals(user.getId(), myProgress.get(0).getId().getUserId()),
                () -> assertEquals(State.IN_PROGRESS, myProgress.get(0).getState()),
                () -> assertEquals(0, myProgress.get(0).getPagesRead()),
                () -> assertNotNull(myProgress.get(0).getDateStartedReading()),
                () -> assertNull(myProgress.get(0).getBookReview()),
                () -> assertNull(myProgress.get(0).getDateFinishedReading()),
                () -> assertNull(myProgress.get(0).getRating())
        );
    }

    @Test
    @DisplayName("update my progress")
    @Order(3)
    void updateMyBookProgress() throws Exception {
        bookProgressService.addToMyToBeReadState(testBook1.getId(), user.getId());
        bookProgressService.startReading(testBook1.getId(), user.getId());
        bookProgressService.updateMyBookProgress(testBook1.getId(), user.getId(), 10);
        List<BookProgress> myProgress = bookProgressService.getMyBooksProgress(user.getId());
        Assertions.assertAll("Assert valid insertion in start reading list",
                () -> assertEquals(1, myProgress.size()),
                () -> assertEquals(testBook1.getId(), myProgress.get(0).getId().getBookId()),
                () -> assertEquals(user.getId(), myProgress.get(0).getId().getUserId()),
                () -> assertEquals(State.IN_PROGRESS, myProgress.get(0).getState()),
                () -> assertEquals(10, myProgress.get(0).getPagesRead()),
                () -> assertNotNull(myProgress.get(0).getDateStartedReading()),
                () -> assertNull(myProgress.get(0).getBookReview()),
                () -> assertNull(myProgress.get(0).getDateFinishedReading()),
                () -> assertNull(myProgress.get(0).getRating())
        );
    }

    @Test
    @DisplayName("completing my book")
    @Order(4)
    void completingMyBook() throws Exception {
        bookProgressService.addToMyToBeReadState(testBook1.getId(), user.getId());
        bookProgressService.startReading(testBook1.getId(), user.getId());
        bookProgressService.updateMyBookProgress(testBook1.getId(), user.getId(), 10);
        bookProgressService.updateMyBookProgress(testBook1.getId(), user.getId(), 100);
        List<BookProgress> myProgress = bookProgressService.getMyBooksProgress(user.getId());
        Assertions.assertAll("Assert valid insertion in start reading list",
                () -> assertEquals(1, myProgress.size()),
                () -> assertEquals(testBook1.getId(), myProgress.get(0).getId().getBookId()),
                () -> assertEquals(user.getId(), myProgress.get(0).getId().getUserId()),
                () -> assertEquals(State.FINISHED, myProgress.get(0).getState()),
                () -> assertEquals(100, myProgress.get(0).getPagesRead()),
                () -> assertNotNull(myProgress.get(0).getDateStartedReading()),
                () -> assertNotNull(myProgress.get(0).getDateFinishedReading()),
                () -> assertNull(myProgress.get(0).getBookReview()),
                () -> assertNull(myProgress.get(0).getRating())
        );
    }

    @Test
    @DisplayName("completing my book")
    @Order(5)
    void doAfterFinishingBook() throws Exception {
        bookProgressService.addToMyToBeReadState(testBook1.getId(), user.getId());
        bookProgressService.startReading(testBook1.getId(), user.getId());
        bookProgressService.updateMyBookProgress(testBook1.getId(), user.getId(), 10);
        bookProgressService.updateMyBookProgress(testBook1.getId(), user.getId(), 100);
        bookProgressService.doOnAfterfinishReading(testBook1.getId(), user.getId(), RatingScale.EIGHT_POINT_FIVE, "v good");
        List<BookProgress> myProgress = bookProgressService.getMyBooksProgress(user.getId());
        Assertions.assertAll("Assert valid insertion in start reading list",
                () -> assertEquals(1, myProgress.size()),
                () -> assertEquals(testBook1.getId(), myProgress.get(0).getId().getBookId()),
                () -> assertEquals(user.getId(), myProgress.get(0).getId().getUserId()),
                () -> assertEquals(State.FINISHED, myProgress.get(0).getState()),
                () -> assertEquals(100, myProgress.get(0).getPagesRead()),
                () -> assertEquals("v good", myProgress.get(0).getBookReview()),
                () -> assertEquals(RatingScale.EIGHT_POINT_FIVE, myProgress.get(0).getRating()),
                () -> assertNotNull(myProgress.get(0).getDateStartedReading()),
                () -> assertNotNull(myProgress.get(0).getDateFinishedReading())
        );
    }

}
