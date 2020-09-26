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
import com.karankumar.bookproject.backend.entity.*;
import com.karankumar.bookproject.backend.entity.BookGenre;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.AfterEach;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@IntegrationTest
class BookServiceTest {
    private static AuthorService authorService;
    private static BookService bookService;
    private static CustomShelfService customShelfService;
    private static TagService tagService;

    private Book validBook;
    private PredefinedShelf toRead;
    private Author author;

    @BeforeEach
    public void setup(@Autowired BookService goalService, @Autowired AuthorService authorService,
                      @Autowired PredefinedShelfService predefinedShelfService,
                      @Autowired CustomShelfService customShelfService,
                      @Autowired TagService tagService) {
        PredefinedShelfUtils predefinedShelfUtils = new PredefinedShelfUtils(predefinedShelfService);
        toRead = predefinedShelfUtils.findToReadShelf();

        BookServiceTest.bookService = goalService;
        goalService.deleteAll();
        BookServiceTest.authorService = authorService;
        authorService.deleteAll();
        BookServiceTest.customShelfService = customShelfService;
        customShelfService.deleteAll();
        BookServiceTest.tagService = tagService;
        tagService.deleteAll();

        author = new Author("Test First Name", "Test Last Name");
        validBook = new Book("Book Name", author, toRead);
    }

    @Test
    void whenTryingToSaveNullBookExpectNoSave() {
        bookService.save(null);
        assertThat(bookService.count()).isZero();
    }

    @Test
    void whenTryingToSaveBookWithoutAuthorExpectNoSave() {
        // when
        bookService.save(new Book("Book without author", null, toRead));

        // then
        assertThat(authorService.count()).isZero();
        assertThat(bookService.count()).isZero();
    }

    @Test
    @DisplayName("Tests book is not saved with numberOfPages > max_pages")
    void whenTryingToSaveBookWithMaxNumberPageExceededNoSave() {
        // given
        Book book = new Book("Book without author", new Author("First", "Last"), toRead);
        book.setNumberOfPages(Book.MAX_PAGES + 1);

        // when
        Exception exception  = Assertions.assertThrows(RuntimeException.class, () -> bookService.save(book));

        // then
        assertTrue(ExceptionUtils.getRootCause(exception) instanceof ConstraintViolationException);
        assertThat(bookService.count()).isZero();
    }

    /**
     * Tests book is not saved with pagesRead > max_pages
     */
    @Test
    void whenTryingToSaveBookWithPagesReadExceededNoSave() {
        // given
        Book book = new Book("Book without author", new Author("First", "Last"), toRead);
        book.setPagesRead(Book.MAX_PAGES + 1);

        // when
        Exception exception  = Assertions.assertThrows(RuntimeException.class, () -> bookService.save(book));

        // then
        assertTrue(ExceptionUtils.getRootCause(exception) instanceof ConstraintViolationException);
        assertThat(bookService.count()).isZero();
    }

    /**
     * Tests book is saved with pagesRead and numberOfPages = Max_pages
     */
    @Test
    void whenTryingToSaveBookWithPagesInLimitSave() {
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
    @DisplayName("A book without a predefined shelf should not be saved")
    void whenTryingToSaveWithoutShelfExpectNoSave() {
        // given
        Book bookWithoutShelf = new Book("Title", new Author("First", "Last"), null);

        // when
        bookService.save(bookWithoutShelf);

        // then
        assertThat(authorService.count()).isZero();
        assertThat(bookService.count()).isZero();
    }

    /**
     * Tests whether the book with author and shelf can be saved
     */
    @Test
    void whenTryingToSaveMultipleBooksExpectSave() {
        // given
        assertThat(bookService.count()).isZero();

        // when
        bookService.save(validBook);

        // then
        assertThat(bookService.count()).isOne();
        assertThat(bookService.findAll(validBook.getTitle()).size()).isOne();
        assertEquals(validBook, bookService.findAll(validBook.getTitle()).get(0));
        assertEquals(validBook.getAuthor(),
                bookService.findAll(validBook.getTitle())
                           .get(0)
                           .getAuthor());
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
        String jsonAsString = bookService.getJsonRepresentationForBooksAsString();

        // then
        JSONAssert.assertEquals(expectedJsonString, jsonAsString, JSONCompareMode.NON_EXTENSIBLE);
    }

    private Book createBookAndSetAllAttributes() {
        CustomShelf customShelf = new CustomShelf("My Shelf");
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
