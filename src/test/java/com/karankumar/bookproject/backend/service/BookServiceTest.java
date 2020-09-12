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
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;

@IntegrationTest
public class BookServiceTest {

    private static AuthorService authorService;
    private static BookService bookService;

    private Book validBook;
    private PredefinedShelf toRead;
    private final Integer MAX_PAGES = 23_000;

    @BeforeEach
    public void setup(@Autowired BookService goalService, @Autowired AuthorService authorService,
                      @Autowired PredefinedShelfService predefinedShelfService) {
        PredefinedShelfUtils predefinedShelfUtils = new PredefinedShelfUtils(predefinedShelfService);
        toRead = predefinedShelfUtils.findToReadShelf();

        BookServiceTest.bookService = goalService;
        goalService.deleteAll();
        BookServiceTest.authorService = authorService;
        authorService.deleteAll();

        Author author = new Author("Test First Name", "Test Last Name");
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
     * Tests book is not saved with numberOfPages > max_pages
     */
    @Test
    public void whenTryingToSaveBookWithMaxNumberPageExceedNoSave() {
        Book book = new Book("Book without author", new Author("First", "Last"), toRead);
        book.setNumberOfPages(MAX_PAGES + 1);
        Exception exception  = Assertions.assertThrows(RuntimeException.class, () -> bookService.save(book));
        Assertions.assertTrue(ExceptionUtils.getRootCause(exception) instanceof ConstraintViolationException);
        Assertions.assertEquals(0, bookService.count());
    }

    /**
     * Tests book is not saved with pagesRead > max_pages
     */
    @Test
    public void whenTryingToSaveBookWithPagesReadExceedNoSave() {
        Book book = new Book("Book without author", new Author("First", "Last"), toRead);
        book.setPagesRead(MAX_PAGES + 1);
        Exception exception  = Assertions.assertThrows(RuntimeException.class, () -> bookService.save(book));
        Assertions.assertTrue(ExceptionUtils.getRootCause(exception) instanceof ConstraintViolationException);
        Assertions.assertEquals(0, bookService.count());
    }

    /**
     * Tests book is saved with pagesRead and numberOfPages = Max_pages
     */
    @Test
    public void whenTryingToSaveBookWithPagesInLimitSave() {
        Book book = new Book("Book without author", new Author("First", "Last"), toRead);
        book.setPagesRead(MAX_PAGES);
        book.setNumberOfPages(MAX_PAGES);
        bookService.save(book);
        Assertions.assertEquals(1, bookService.count());
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

    @AfterEach
    void deleteBooks() {
        bookService.deleteAll();
    }
}
