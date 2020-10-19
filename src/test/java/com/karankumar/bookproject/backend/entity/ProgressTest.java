package com.karankumar.bookproject.backend.entity;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.account.Role;
import com.karankumar.bookproject.backend.entity.account.User;
import com.karankumar.bookproject.backend.entity.book.BookProgress;
import com.karankumar.bookproject.backend.entity.book.BookProgressId;
import com.karankumar.bookproject.backend.entity.enums.State;
import com.karankumar.bookproject.backend.service.BookProgressService;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IntegrationTest
class ProgressTest {

    private final BookService bookService;

    private final PredefinedShelfService predefinedShelfService;

    private final BookProgressService bookProgressService;

    private static Book testBook1;
    private static PredefinedShelf toRead;
    private static User user;
    private static BookProgressId id;

    @Autowired
    ProgressTest(BookService bookService, PredefinedShelfService predefinedShelfService,
                 BookProgressService bookProgressService) {
        this.bookService = bookService;
        this.predefinedShelfService = predefinedShelfService;
        this.bookProgressService = bookProgressService;
    }

    @BeforeEach
    public void setUp() {
        toRead = predefinedShelfService.findToReadShelf();
        testBook1 = createBook("How the mind works", toRead);
        user = new User(new Random().nextLong(), "hassan",
                "hassan.elseoudy@gmail.com",
                "sj%kfn%ui%4#3#78t43uw3n@%",
                true,
                Collections.singleton(new Role("role")));
        id = new BookProgressId(testBook1.getId(), user.getId());
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
    @DisplayName("[Valid] Progress Service should addToMyToBeReadState()")
    void addToMyToBeReadList() {
        System.out.println(id.getBookId());
        bookProgressService.addToMyToBeReadState(id);
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
    @DisplayName("[Valid] Progress Service should startReading()")
    void startReadingTest() throws Exception {
        bookProgressService.addToMyToBeReadState(id);
        bookProgressService.startReading(id);
        List<BookProgress> myProgress = bookProgressService.getMyBooksProgress(user.getId());
        Assertions.assertAll("Assert valid insertion in IN_PROGRESS list",
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
    @DisplayName("[Wrong Id] Progress Service shouldn't startReading()")
    void shouldntStartReadingTestWithExceptionThrown() {
        bookProgressService.addToMyToBeReadState(id);
        Exception exception =
                assertThrows(Exception.class, () -> bookProgressService.startReading(id));

        String expectedMessage = "You don't have any progress on this book";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    @DisplayName("[Valid] Progress Service should updateMyBookProgress()")
    void updateMyBookProgress() throws Exception {
        bookProgressService.addToMyToBeReadState(id);
        bookProgressService.startReading(id);
        bookProgressService.updateMyBookProgress(id, 10);
        List<BookProgress> myProgress = bookProgressService.getMyBooksProgress(user.getId());
        Assertions.assertAll("Assert valid update in IN_PROGRESS state",
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
    @DisplayName("[Valid] Progress Service should updateMyBookProgress() with FINISHED")
    void completingMyBook() throws Exception {
        bookProgressService.addToMyToBeReadState(id);
        bookProgressService.startReading(id);
        bookProgressService.updateMyBookProgress(id, 10);
        bookProgressService.updateMyBookProgress(id, 100);
        List<BookProgress> myProgress = bookProgressService.getMyBooksProgress(user.getId());
        Assertions.assertAll("Assert valid insertion in FINISHED state",
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
    @DisplayName("[Valid] Progress Service should doOnAfterfinishReading()")
    void doAfterFinishingBook() throws Exception {
        bookProgressService.addToMyToBeReadState(id);
        bookProgressService.startReading(id);
        bookProgressService.updateMyBookProgress(id, 10);
        bookProgressService.updateMyBookProgress(id, 100);
        bookProgressService.doOnAfterfinishReading(id, RatingScale.EIGHT_POINT_FIVE, "v good");
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
