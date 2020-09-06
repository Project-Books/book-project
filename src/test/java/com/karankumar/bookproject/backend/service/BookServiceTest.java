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
import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.CustomShelf;
import com.karankumar.bookproject.backend.entity.Tag;
import com.karankumar.bookproject.backend.entity.RatingScale;
import com.karankumar.bookproject.backend.entity.Genre;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;

@IntegrationTest
public class BookServiceTest {

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
    public void whenTryingToSaveNullBookExpectNoSave() {
        bookService.save(null);
        Assertions.assertEquals(0, bookService.count());
    }

    @Test
    public void whenTryingToSaveBookWithoutAuthorExpectNoSave() {
        bookService.save(new Book("Book without author", null, toRead));
        Assertions.assertEquals(0, authorService.count());
        Assertions.assertEquals(0, bookService.count());
    }

    /**
     * Tests whether the book without shelf can be saved
     */
    @Test
    public void whenTryingToSaveWithoutShelfExpectNoSave() {
        Book bookWithoutShelf = new Book("Title", new Author("First", "Last"), null);
        bookService.save(bookWithoutShelf);
        Assertions.assertEquals(0, authorService.count());
        Assertions.assertEquals(0, bookService.count());
    }

    /**
     * Tests whether the book with author and shelf can be saved
     */
    @Test
    public void whenTryingToSaveMultipleBooksExpectSave() {
        Assertions.assertEquals(0, bookService.count());
        bookService.save(validBook);
        Assertions.assertEquals(1, bookService.count());
        Assertions.assertEquals(1, bookService.findAll(validBook.getTitle()).size());
        Assertions.assertEquals(validBook, bookService.findAll(validBook.getTitle()).get(0));
        Assertions.assertEquals(validBook.getAuthor(),
                bookService.findAll(validBook.getTitle())
                           .get(0)
                           .getAuthor());
    }

    @Test
    public void allBooksReturnedWhenFilterIsEmpty() {
        Assertions.assertEquals(bookService.findAll(), bookService.findAll(""));
    }

    @Test
    public void allBooksReturnedWhenFilterIsNull() {
        Assertions.assertEquals(bookService.findAll(), bookService.findAll(null));
    }

    @Test
    public void shouldCreateJsonRepresentationForBooks() throws IOException, JSONException {
        // given
        Book anotherValidBook = createBookAndSetAllAttributes();
        bookService.save(validBook);
        bookService.save(anotherValidBook);

        String expectedJsonString = FileUtils.readFileToString(
                new File("src/test/resources/allBooksJson.json"), "UTF8");

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
        book.setGenre(Genre.ADVENTURE);
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
}
