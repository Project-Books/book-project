/*
    The book project lets a user keep track of different books they would like to read, are currently
    reading, have read or did not finish.
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY
    WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
    PURPOSE.  See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with this program.
    If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.Tag;
import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.BookGenre;	
import com.karankumar.bookproject.backend.entity.CustomShelf;	
import com.karankumar.bookproject.backend.entity.PredefinedShelf;	
import com.karankumar.bookproject.backend.entity.RatingScale;
import org.apache.commons.io.FileUtils;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.api.SoftAssertions;
import org.json.JSONException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@IntegrationTest
class BookServiceTest {
    private final AuthorService authorService;
    private final BookService bookService;
    private final CustomShelfService customShelfService;
    private final TagService tagService;
    private final PredefinedShelfService predefinedShelfService;

    private Book validBook;
    private PredefinedShelf toRead;
    private Author author;

    @Autowired
    BookServiceTest(AuthorService authorService, BookService bookService, CustomShelfService customShelfService, TagService tagService, PredefinedShelfService predefinedShelfService) {
        this.authorService = authorService;
        this.bookService = bookService;
        this.customShelfService = customShelfService;
        this.tagService = tagService;
        this.predefinedShelfService = predefinedShelfService;
    }

    @BeforeEach
    public void setup() {
        resetServices();
        resetAuthorAndBook();
    }

    private void resetAuthorAndBook() {
        toRead = predefinedShelfService.findByPredefinedShelfNameAndLoggedInUser(PredefinedShelf.ShelfName.TO_READ);
        author = new Author("Test First Name", "Test Last Name");
        validBook = new Book("Book Name", author, toRead);
    }

    private void resetServices() {
        bookService.deleteAll();
        authorService.deleteAll();
        customShelfService.deleteAll();
        tagService.deleteAll();
    }

    @Test
    void nullBookNotSaved() {
        bookService.save(null);
        assertThat(bookService.count()).isZero();
    }

    @Test
    void bookWithoutAuthorNotSaved() {
        SoftAssertions softly = new SoftAssertions();

        // when
        bookService.save(new Book("Book without author", null, toRead));

        // then
        softly.assertThat(authorService.count()).isZero();
        softly.assertThat(bookService.count()).isZero();
        softly.assertAll();
    }

    @Test
    @DisplayName("Tests book with numberOfPages > max_pages is not saved")
    void bookWithMaxNumberOfPagesExceededNotSaved() {
        SoftAssertions softly = new SoftAssertions();

        // given
        Book book = new Book("Book without author", new Author("First", "Last"), toRead);
        book.setNumberOfPages(Book.MAX_PAGES + 1);

        // when and then
        softly.assertThatThrownBy(() -> bookService.save(book))
              .isInstanceOf(TransactionSystemException.class);
        softly.assertThat(bookService.count()).isZero();
        softly.assertAll();
    }

    @Test
    void bookOutsidePageLimitNotSaved() {
        SoftAssertions softly = new SoftAssertions();

        // given
        Book book = new Book("Book without author", new Author("First", "Last"), toRead);
        book.setPagesRead(Book.MAX_PAGES + 1);

        // when and then
        softly.assertThatThrownBy(() -> bookService.save(book))
              .isInstanceOf(TransactionSystemException.class);
        softly.assertThat(bookService.count()).isZero();
        softly.assertAll();
    }

    @Test
    void bookWithinPageLimitIsSaved() {
        // given
        Book book = new Book("Book without author", new Author("First", "Last"), toRead);
        book.setPagesRead(Book.MAX_PAGES);
        book.setNumberOfPages(Book.MAX_PAGES);

        // when
        bookService.save(book);

        // then
        assertThat(bookService.count()).isOne();
    }

    @Test
    void bookWithoutPredefinedShelfNotSaved() {
        SoftAssertions softly = new SoftAssertions();

        // given
        Book bookWithoutShelf = new Book("Title", new Author("First", "Last"), null);

        // when
        bookService.save(bookWithoutShelf);

        // then
        softly.assertThat(authorService.count()).isZero();
        softly.assertThat(bookService.count()).isZero();
        softly.assertAll();
    }

    @Test
    @DisplayName("Book with author and predefined shelf can be saved")
    void validBookSaved() {
        SoftAssertions softly = new SoftAssertions();

        // given
        assertThat(bookService.count()).isZero();

        // when
        bookService.save(validBook);

        // then
        List<Book> filteredByTitle = bookService.findAll(validBook.getTitle());

        softly.assertThat(bookService.count()).isOne();
        softly.assertThat(bookService.findAll(validBook.getTitle()).size()).isOne();
        softly.assertThat(filteredByTitle).first().isEqualTo(validBook);
        softly.assertAll();
    }

    @Test
    void allBooksReturnedWhenFilterIsEmpty() {
        assertEquals(bookService.findAll(), bookService.findAll(""));
    }

    @Test
    void allBooksReturnedWhenFilterIsNull() {
        assertEquals(bookService.findAll(), bookService.findAll(null));
    }

    @Test
    void shouldCreateJsonRepresentationForBooks() throws IOException, JSONException {
        // given
        Book anotherValidBook = createBookAndSetAllAttributes();
        bookService.save(validBook);
        bookService.save(anotherValidBook);

        String expectedJsonString = FileUtils.readFileToString(
                new File("src/test/resources/exportedBooksSample.json"), "UTF8");

        // when
        String actualJsonString = bookService.getJsonRepresentationForBooksAsString();

        // then
        JSONAssert.assertEquals(expectedJsonString, actualJsonString, JSONCompareMode.NON_EXTENSIBLE);
    }

    @Test
    @Transactional
    void findSavedBook() {
        Book bookToSave = new Book("Book Name To Save", author, toRead);
        bookService.save(bookToSave);
        assertEquals(bookService.findById(bookToSave.getId()),bookToSave);
    }

    private Book createBookAndSetAllAttributes() {
        CustomShelf customShelf = customShelfService.createCustomShelf("My Shelf");
        customShelfService.save(customShelf);

        Tag tag1 = new Tag("book");
        Tag tag2 = new Tag("adventure");
        tagService.save(tag1);
        tagService.save(tag2);

        Book book = new Book("Another Book Name", author, toRead);
        book.setNumberOfPages(420);
        book.setPagesRead(42);
        book.setBookGenre(BookGenre.ADVENTURE);
        book.setSeriesPosition(3);
        book.setEdition(2);
        book.setBookRecommendedBy("Peter Parker");
        book.setIsbn("9780151010264");
        book.setCustomShelf(customShelf);
        book.setTags(Set.of(tag1, tag2));
        book.setRating(RatingScale.EIGHT);
        book.setDateStartedReading(LocalDate.of(2020, 7, 5));
        book.setDateFinishedReading(LocalDate.of(2020, 9, 5));
        book.setBookReview("Very good.");
        return book;
    }

    @AfterEach
    void deleteBooks() {
        bookService.deleteAll();
    }
}
