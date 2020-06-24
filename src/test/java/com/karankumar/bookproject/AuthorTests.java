package com.karankumar.bookproject;

import com.karankumar.bookproject.backend.model.Author;
import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.PredefinedShelf;
import com.karankumar.bookproject.backend.service.AuthorService;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class AuthorTests {

    private static PredefinedShelfService shelfService;
    private static BookService bookService;

    private static Book testBook1;
    private static Book testBook2;
    private static AuthorService authorService;

    @BeforeAll
    public static void setup(@Autowired PredefinedShelfService shelfService, @Autowired BookService bookService,
                             @Autowired AuthorService authorService) {
        Author author = new Author("Steven", "Pinker");

        testBook1 = new Book("How the mind works", author);
        testBook2 = new Book("The better angels of our nature", author);

        Assumptions.assumeTrue(shelfService != null && bookService != null);
        AuthorTests.shelfService = shelfService;
        AuthorTests.bookService = bookService;
        AuthorTests.authorService = authorService;

        bookService.deleteAll(); // reset

        List<PredefinedShelf> shelves = AuthorTests.shelfService.findAll();
        PredefinedShelf toRead =
                shelves.stream()
                        .takeWhile(s -> s.getShelfName().equals(PredefinedShelf.ShelfName.TO_READ)).
                        collect(Collectors.toList())
                        .get(0);

        testBook1.setShelf(toRead);
        testBook2.setShelf(toRead);

        AuthorTests.bookService.save(testBook1);
        AuthorTests.bookService.save(testBook2);
    }

    /**
     * Updating the author of a book should only affect that book, not any other book that originally had the same author name
     */
    @Test
    public void updateAuthorAffectsOneRow() {
        Assumptions.assumeTrue(shelfService != null);

        Author newAuthor = new Author("Matthew", "Walker");
        testBook1.setAuthor(newAuthor);
        bookService.save(testBook1);

        Assertions.assertNotEquals(testBook1.getAuthor(), testBook2.getAuthor());
    }

    @Test
    public void orphanAuthorsRemoved() {
        Assumptions.assumeTrue(bookService != null);
        bookService.deleteAll(); // reset

		Author orphan = new Author("Jostein", "Gardner");
		Book book = new Book("Sophie's World", orphan);
        bookService.delete(book);

        boolean idFound;
        try {
            Assertions.assertNull(authorService.findById(orphan.getId()));
            idFound = true;
        } catch (InvalidDataAccessApiUsageException e) {
        	// If this exception is thrown, then the authorService could not find the id
            idFound = false;
        }

        Assertions.assertFalse(idFound);
        Assertions.assertTrue(authorService.findAll().isEmpty());
    }
}
