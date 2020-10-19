package com.karankumar.bookproject.backend.entity;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.account.Role;
import com.karankumar.bookproject.backend.entity.account.User;
import com.karankumar.bookproject.backend.entity.book.BookProgress;
import com.karankumar.bookproject.backend.entity.book.BookProgressBuilder;
import com.karankumar.bookproject.backend.entity.book.BookProgressId;
import com.karankumar.bookproject.backend.entity.enums.State;
import com.karankumar.bookproject.backend.repository.ProgressRepository;
import com.karankumar.bookproject.backend.service.BookProgressService;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@IntegrationTest
class ProgressTest {

    private final BookService bookService;

    private final PredefinedShelfService predefinedShelfService;

    private final BookProgressService bookProgressService;

    private final ProgressRepository progressRepository;

    private static Book testBook1;
    private static PredefinedShelf toRead;
    private static User user;
    private static BookProgressId id;

    @Autowired
    ProgressTest(BookService bookService, PredefinedShelfService predefinedShelfService,
                 BookProgressService bookProgressService, ProgressRepository progressRepository) {
        this.bookService = bookService;
        this.predefinedShelfService = predefinedShelfService;
        this.bookProgressService = bookProgressService;
        this.progressRepository = progressRepository;
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
        resetBookService();
        saveBooks();
        id = new BookProgressId(testBook1.getId(), user.getId());
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
        bookProgressService.addToMyToBeReadState(id);
        List<BookProgress> myProgress = bookProgressService.getMyBooksProgress(user.getId());
        assertSoftly(
                softly -> {
                    softly.assertThat(myProgress.size()).isEqualTo(1);
                    softly.assertThat(myProgress.get(0).getId().getBookId())
                          .isEqualTo(testBook1.getId());
                    softly.assertThat(myProgress.get(0).getId().getUserId())
                          .isEqualTo(user.getId());
                    softly.assertThat(myProgress.get(0).getState()).isEqualTo(State.TO_BE_READ);
                    softly.assertThat(myProgress.get(0).getBookReview()).isNull();
                    softly.assertThat(myProgress.get(0).getDateStartedReading()).isNull();
                    softly.assertThat(myProgress.get(0).getDateFinishedReading()).isNull();
                    softly.assertThat(myProgress.get(0).getPagesRead()).isNull();
                    softly.assertThat(myProgress.get(0).getRating()).isNull();
                }
        );
    }

    @Test
    @DisplayName("[Valid] Progress Service should startReading()")
    void startReadingTest() throws Exception {
        bookProgressService.addToMyToBeReadState(id);
        bookProgressService.startReading(id);
        List<BookProgress> myProgress = bookProgressService.getMyBooksProgress(user.getId());

        assertSoftly(
                softly -> {
                    softly.assertThat(myProgress.size()).isEqualTo(1);
                    softly.assertThat(myProgress.get(0).getId().getBookId())
                          .isEqualTo(testBook1.getId());
                    softly.assertThat(myProgress.get(0).getId().getUserId())
                          .isEqualTo(user.getId());
                    softly.assertThat(myProgress.get(0).getState()).isEqualTo(State.IN_PROGRESS);
                    softly.assertThat(myProgress.get(0).getPagesRead()).isEqualTo(0);
                    softly.assertThat(myProgress.get(0).getDateStartedReading()).isNotNull();
                    softly.assertThat(myProgress.get(0).getDateFinishedReading()).isNull();
                    softly.assertThat(myProgress.get(0).getBookReview()).isNull();
                    softly.assertThat(myProgress.get(0).getRating()).isNull();
                }
        );

    }

    @Test
    @DisplayName("[Valid] Progress Service should updateMyBookProgress()")
    void updateMyBookProgress() throws Exception {
        bookProgressService.addToMyToBeReadState(id);
        bookProgressService.startReading(id);
        bookProgressService.updateMyBookProgress(id, 10);
        List<BookProgress> myProgress = bookProgressService.getMyBooksProgress(user.getId());

        assertSoftly(
                softly -> {
                    softly.assertThat(myProgress.size()).isEqualTo(1);
                    softly.assertThat(myProgress.get(0).getId().getBookId())
                          .isEqualTo(testBook1.getId());
                    softly.assertThat(myProgress.get(0).getId().getUserId())
                          .isEqualTo(user.getId());
                    softly.assertThat(myProgress.get(0).getState()).isEqualTo(State.IN_PROGRESS);
                    softly.assertThat(myProgress.get(0).getPagesRead()).isEqualTo(10);
                    softly.assertThat(myProgress.get(0).getDateStartedReading()).isNotNull();
                    softly.assertThat(myProgress.get(0).getDateFinishedReading()).isNull();
                    softly.assertThat(myProgress.get(0).getBookReview()).isNull();
                    softly.assertThat(myProgress.get(0).getRating()).isNull();
                }
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

        assertSoftly(
                softly -> {
                    softly.assertThat(myProgress.size()).isEqualTo(1);
                    softly.assertThat(myProgress.get(0).getId().getBookId())
                          .isEqualTo(testBook1.getId());
                    softly.assertThat(myProgress.get(0).getId().getUserId())
                          .isEqualTo(user.getId());
                    softly.assertThat(myProgress.get(0).getState()).isEqualTo(State.FINISHED);
                    softly.assertThat(myProgress.get(0).getPagesRead()).isEqualTo(100);
                    softly.assertThat(myProgress.get(0).getDateStartedReading()).isNotNull();
                    softly.assertThat(myProgress.get(0).getDateFinishedReading()).isNotNull();
                    softly.assertThat(myProgress.get(0).getBookReview()).isNull();
                    softly.assertThat(myProgress.get(0).getRating()).isNull();
                }
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
        assertSoftly(
                softly -> {
                    softly.assertThat(myProgress.size()).isEqualTo(1);
                    softly.assertThat(myProgress.get(0).getId().getBookId())
                          .isEqualTo(testBook1.getId());
                    softly.assertThat(myProgress.get(0).getId().getUserId())
                          .isEqualTo(user.getId());
                    softly.assertThat(myProgress.get(0).getState()).isEqualTo(State.FINISHED);
                    softly.assertThat(myProgress.get(0).getPagesRead()).isEqualTo(100);
                    softly.assertThat(myProgress.get(0).getDateStartedReading()).isNotNull();
                    softly.assertThat(myProgress.get(0).getDateFinishedReading()).isNotNull();
                    softly.assertThat(myProgress.get(0).getBookReview()).isEqualTo("v good");
                    softly.assertThat(myProgress.get(0).getRating())
                          .isEqualTo(RatingScale.EIGHT_POINT_FIVE);
                }
        );
    }

    @Test
    @DisplayName("[Wrong Id] Progress Service shouldn't getMyBookProgressById()")
    void shouldntGetNonExistedIdTestWithExceptionThrown() {
        assertThatThrownBy(() -> bookProgressService.getMyBookProgressById(id))
                .hasMessage("You don't have any progress on this book");
    }

    @Test
    @DisplayName("[Wrong State] Progress Service shouldn't startReading()")
    void shouldntStartReadingUnlessInToBeReadTestWithExceptionThrown() {
        progressRepository.save(new BookProgressBuilder()
                .withId(id)
                .build());
        assertThatThrownBy(() -> bookProgressService.startReading(id))
                .hasMessage("You can't start reading a book with your current state");
    }

    @Test
    @DisplayName("[Wrong State] Progress Service shouldn't updateMyBookProgress()")
    void shouldntUpdateUnlessStartedTestWithExceptionThrown() {
        bookProgressService.addToMyToBeReadState(id);
        assertThatThrownBy(() -> bookProgressService.updateMyBookProgress(id, 50))
                .hasMessage("You can't update unless you're reading the book");
    }

    @Test
    @DisplayName("[Wrong State] Progress Service shouldn't updateMyBookProgress()")
    void shouldntUpdateWithGreaterThanBookPagesTestWithExceptionThrown() throws Exception {
        bookProgressService.addToMyToBeReadState(id);
        bookProgressService.startReading(id);
        assertThatThrownBy(() -> bookProgressService.updateMyBookProgress(id, 101))
                .hasMessage("Updated pages is more than book's pages");
    }

    @Test
    @DisplayName("[Wrong State] Progress Service shouldn't updateMyBookProgress()")
    void shouldntRateUnlessFinishTestWithExceptionThrown() throws Exception {
        bookProgressService.addToMyToBeReadState(id);
        bookProgressService.startReading(id);
        bookProgressService.updateMyBookProgress(id, 10);
        assertThatThrownBy(() -> bookProgressService
                .doOnAfterfinishReading(id, RatingScale.EIGHT_POINT_FIVE,
                        "nt bad"))
                .hasMessage("You can't rate or review unless you finish the book!");

    }

    @Test
    @DisplayName("[Not Valid] Progress Service shouldn't getBook()")
    void shouldntGetNonExistedBookTestWithExceptionThrown() {
        assertThatThrownBy(() -> bookProgressService.getBook(new Random().nextLong()))
                .hasMessage("Book Not found In the database");
    }

}
