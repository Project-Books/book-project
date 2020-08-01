package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.tags.IntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@IntegrationTest
public class BookServiceTest {

    private static AuthorService authorService;
    private static BookService goalService;

    private Book bookWithoutShelf;
    private Book bookWithoutAuthor;
    private Book validBook;

    @BeforeEach
    public void setup(@Autowired BookService goalService, @Autowired AuthorService authorService,
                      @Autowired PredefinedShelfService shelfService) {

        List<PredefinedShelf> shelves = shelfService.findAll();
        PredefinedShelf toRead = shelves.stream()
                .takeWhile(s -> s.getPredefinedShelfName().equals(PredefinedShelf.ShelfName.TO_READ))
                .collect(Collectors.toList())
                .get(0);

        BookServiceTest.goalService = goalService;
        goalService.deleteAll();
        BookServiceTest.authorService = authorService;
        authorService.deleteAll();

        Author author = new Author("Test First Name", "Test Last Name");
        bookWithoutShelf = new Book("Test Title", author);
        bookWithoutAuthor = new Book("Book without Author", null);
        validBook = new Book("Book Name", author);
        validBook.setShelf(toRead);
    }

    /**
     * Tests whether the null book is  can be saved
     */
    @Test
    public void whenTryingToSaveNullBookExpectNoSave() {
        goalService.save(null);
        Assertions.assertEquals(0, goalService.count());
    }

    /**
     * Tests whether the book without author can be saved
     */
    @Test
    public void whenTryingToWithoutAuthorBookExpectNoSave() {
        goalService.save(bookWithoutAuthor);
        Assertions.assertEquals(0, authorService.count());
        Assertions.assertEquals(0, goalService.count());
    }

    /**
     * Tests whether the book without shelf can be saved
     */
    @Test
    public void whenTryingToSaveWithoutShelfBookExpectNoSave() {
        goalService.save(bookWithoutShelf);
        Assertions.assertEquals(0, authorService.count());
        Assertions.assertEquals(0, goalService.count());
    }

    /**
     * Tests whether the book with author and shelf can be saved
     */
    @Test
    public void whenTryingToSaveMultipleBooksExpectSave() {
        Assertions.assertEquals(0, goalService.count());
        goalService.save(validBook);
        Assertions.assertEquals(1, goalService.count());
        Assertions.assertEquals(1, goalService.findAll(validBook.getTitle()).size());
        Assertions.assertEquals(validBook, goalService.findAll(validBook.getTitle()).get(0));
        Assertions.assertEquals(validBook.getAuthor(), goalService.findAll(validBook.getTitle()).get(0).getAuthor());
    }
}
