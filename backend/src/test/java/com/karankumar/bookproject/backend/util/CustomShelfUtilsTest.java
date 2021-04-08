/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2020  Karan Kumar

 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.util;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.model.Author;
import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.CustomShelf;
import com.karankumar.bookproject.backend.model.PredefinedShelf;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.CustomShelfService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IntegrationTest
@DisplayName("CustomShelfUtils should")
class CustomShelfUtilsTest {
    private final BookService bookService;
    private final CustomShelfService customShelfService;
    private final PredefinedShelfService predefinedShelfService;

    private CustomShelf customShelf1;
    private CustomShelf customShelf2;
    private CustomShelf customShelfWithNoBooks;

    private Set<Book> booksInCustomShelf1;

    @Autowired
    CustomShelfUtilsTest(BookService bookService, CustomShelfService customShelfService,
                         PredefinedShelfService predefinedShelfService) {
        this.bookService = bookService;
        this.customShelfService = customShelfService;
        this.predefinedShelfService = predefinedShelfService;
    }

    @BeforeEach
    public void setUp() {
        resetServices();

        customShelf1 = customShelfService.createCustomShelf("CustomShelf1");
        customShelf2 = customShelfService.createCustomShelf("CustomShelf2");
        customShelfWithNoBooks = customShelfService.createCustomShelf("CustomShelf3");
        saveCustomShelves(customShelf1, customShelf2, customShelfWithNoBooks);

        addBooksToCustomShelves(predefinedShelfService.findToReadShelf());
    }

    private void resetServices() {
        bookService.deleteAll();
        customShelfService.deleteAll();
    }

    private HashSet<Book> createSetOfBooks(String title1, String title2,
                                                  PredefinedShelf predefinedShelf,
                                                  CustomShelf customShelf) {
        return new HashSet<>(List.of(
                createAndSaveBook(title1, predefinedShelf, customShelf),
                createAndSaveBook(title2, predefinedShelf, customShelf)
        ));
    }

    private Book createAndSaveBook(String title, PredefinedShelf predefinedShelf,
                                          CustomShelf customShelf) {
        Book book = new Book(title, new Author("John Doe"), predefinedShelf);
        book.setCustomShelf(customShelf);
        bookService.save(book);
        return book;
    }

    private void addBooksToCustomShelves(PredefinedShelf predefinedShelf) {
        booksInCustomShelf1
                = createSetOfBooks("Title1", "Title2", predefinedShelf, customShelf1);
        customShelf1.setBooks(booksInCustomShelf1);
        customShelf2.setBooks(
                createSetOfBooks("Title3", "Title4", predefinedShelf, customShelf2)
        );
    }

    private void saveCustomShelves(CustomShelf... customShelves) {
        for (CustomShelf c : customShelves) {
            customShelfService.save(c);
        }
    }

    @Test
    void returnBooksSuccessfully() {
        Set<Book> actual = customShelfService.getBooksInCustomShelf(customShelf1.getShelfName());
        booksInCustomShelf1.forEach(book -> assertThat(actual.toString()).contains(book.toString()));
    }

    @Test
    void returnNoBooksIfNoBooksInCustomShelf() {
        // given
        String customShelfWithoutBooks = customShelfWithNoBooks.getShelfName();

        // when
        Set<Book> actual = customShelfService.getBooksInCustomShelf(customShelfWithoutBooks);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void returnNoBooksForNonExistentCustomShelf() {
        // given
        String nonExistentShelf = "InvalidShelf";

        // when
        Set<Book> actual = customShelfService.getBooksInCustomShelf(nonExistentShelf);

        // then
        assertThat(actual).isEmpty();
    }
}
