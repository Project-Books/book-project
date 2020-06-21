package com.karankumar.bookproject;

import com.karankumar.bookproject.backend.model.Author;
import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.Genre;
import com.karankumar.bookproject.backend.model.PredefinedShelf;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class BookProjectApplicationTests {

    private static PredefinedShelfService shelfService;

    private static BookService bookService;

    private static Book testBook1;
    private static Author author;
    private static Book testBook2;

    @BeforeAll
    public static void setup(@Autowired PredefinedShelfService shelfService, @Autowired BookService bookService) {
        author = new Author("Steven", "Pinker");

        testBook1 = new Book("How the mind works", author);
        testBook1.setGenre(Genre.SCIENCE);
        testBook1.setNumberOfPages(565);

        testBook2 = new Book("The better angels of our nature", author);
        testBook2.setGenre(Genre.SCIENCE);
        testBook2.setNumberOfPages(605);

        Assumptions.assumeTrue(shelfService != null);
        BookProjectApplicationTests.shelfService = shelfService;

        Assumptions.assumeTrue(bookService != null);
        BookProjectApplicationTests.bookService = bookService;

        List<PredefinedShelf> shelves = BookProjectApplicationTests.shelfService.findAll();
        PredefinedShelf toRead =
                shelves.stream()
                        .takeWhile(s -> s.getShelfName().equals(PredefinedShelf.ShelfName.TO_READ)).
                        collect(Collectors.toList())
                        .get(0);

        testBook1.setShelf(toRead);
        testBook2.setShelf(toRead);

        BookProjectApplicationTests.bookService.save(testBook1);
        BookProjectApplicationTests.bookService.save(testBook2);
    }

    /**
     * Updating the author of a book should only affect that book, not any other book that has the same author name
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



    }
}
