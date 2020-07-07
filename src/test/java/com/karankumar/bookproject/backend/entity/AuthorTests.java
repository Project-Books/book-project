/*
    The book project lets a user keep track of different books they've read, are currently reading or would like to read
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
    warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with this program.
    If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.entity;

import com.karankumar.bookproject.backend.service.AuthorService;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

@SpringBootTest
class AuthorTests {

    private static PredefinedShelfService shelfService;
    private static BookService bookService;

    private static Book testBook1;
    private static Book testBook2;
    private static AuthorService authorService;

    @BeforeAll
    public static void setup(@Autowired PredefinedShelfService shelfService,
                             @Autowired BookService bookService,
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
        PredefinedShelf toRead = shelves.stream()
                .takeWhile(s -> s.getShelfName().equals(PredefinedShelf.ShelfName.TO_READ))
                .collect(Collectors.toList())
                .get(0);

        testBook1.setShelf(toRead);
        testBook2.setShelf(toRead);

        AuthorTests.bookService.save(testBook1);
        AuthorTests.bookService.save(testBook2);
    }

    /**
     * Updating the author of a book should only affect that book, not any other book that originally had the
     * same author name
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
