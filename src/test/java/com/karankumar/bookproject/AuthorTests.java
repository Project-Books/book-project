package com.karankumar.bookproject;

import com.karankumar.bookproject.backend.model.Author;
import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.Genre;
import com.karankumar.bookproject.backend.model.PredefinedShelf;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class AuthorTests {

    private static PredefinedShelfService shelfService;
    private static BookService bookService;

    private static Book testBook1;
    private static Book testBook2;

    @BeforeAll
    public static void setup(@Autowired PredefinedShelfService shelfService, @Autowired BookService bookService) {
        Author author = new Author("Steven", "Pinker");

        testBook1 = new Book("How the mind works", author);
        testBook1.setGenre(Genre.SCIENCE);
        testBook1.setNumberOfPages(565);

        testBook2 = new Book("The better angels of our nature", author);
        testBook2.setGenre(Genre.SCIENCE);
        testBook2.setNumberOfPages(605);

        Assumptions.assumeTrue(shelfService != null);
        AuthorTests.shelfService = shelfService;

        Assumptions.assumeTrue(bookService != null);
        AuthorTests.bookService = bookService;

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

        // TODO: remove
        List<PredefinedShelf> shelves = shelfService.findAll();
        PredefinedShelf toRead =
                shelves.stream()
                        .takeWhile(s -> s.getShelfName().equals(PredefinedShelf.ShelfName.TO_READ)).
                        collect(Collectors.toList())
                        .get(0);

        for (Book b : toRead.getBooks()) {
            System.out.println("Book title: " + b.getTitle() + ", Author: " + b.getAuthor());
        }
        System.out.println();
        ///

        Author newAuthor = new Author("Matthew", "Walker");
        testBook1.setAuthor(newAuthor);
        bookService.save(testBook1);

        // TODO: remove
        List<PredefinedShelf> shelves2 = shelfService.findAll();
        PredefinedShelf toRead2 =
                shelves2.stream()
                        .takeWhile(s -> s.getShelfName().equals(PredefinedShelf.ShelfName.TO_READ)).
                        collect(Collectors.toList())
                        .get(0);

        for (Book b : toRead2.getBooks()) {
            System.out.println("Book title: " + b.getTitle() + ", Author: " + b.getAuthor());
        }
        ///


        System.out.println("Auth1: " + testBook1.getAuthor());
        System.out.println("Auth2: " + testBook2.getAuthor());
        Assertions.assertNotEquals(testBook1.getAuthor(), testBook2.getAuthor());
    }
}
