package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.utils.PredefinedShelfUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class BookServiceTest {

    private static AuthorService authorService;
    private static BookService bookService;

    private Book validBook;
    private PredefinedShelf toRead;

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
        Assertions.assertEquals(validBook.getAuthor(), bookService.findAll(validBook.getTitle()).get(0).getAuthor());
    }

    @Test
    public void allBooksReturnedWhenFilterIsEmpty() {
        Assertions.assertEquals(bookService.findAll(), bookService.findAll(""));
    }

    @Test
    public void allBooksReturnedWhenFilterIsNull() {
        Assertions.assertEquals(bookService.findAll(), bookService.findAll(null));
    }
}
