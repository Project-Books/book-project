/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish. Copyright (C) 2020 Karan Kumar This program is free
 * software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version. This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received
 * a copy of the GNU General Public License along with this program. If not, see
 * <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.utils;

import static com.karankumar.bookproject.backend.utils.ShelfUtils.ALL_BOOKS_SHELF;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.repository.BookRepository;
import com.karankumar.bookproject.backend.repository.PredefinedShelfRepository;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;


@IntegrationTest
public class PredefinedShelfUtilsTest {

    @Autowired
    private PredefinedShelfRepository shelfRepository;

    @Autowired
    private BookRepository bookRepository;

    private static PredefinedShelfUtils predefinedShelfUtils;

    private static PredefinedShelf toReadShelf;
    private static PredefinedShelf readShelf;
    private static PredefinedShelf didNotFinishShelf;
    private static final Author NO_AUTHOR = null;

    private static Book book1 = new Book("someTitle", NO_AUTHOR, toReadShelf);
    private static Book book2 = new Book("someTitle2", NO_AUTHOR, toReadShelf);
    private static Book book3 = new Book("someOtherTitle", NO_AUTHOR, readShelf);
    private static Book book4 = new Book("yetAnotherTitle", NO_AUTHOR, didNotFinishShelf);

    private static final List<String> PREDEFINED_SHELVES = List.of("To read", "Reading", "Read", "Did not finish");
    private static final List<String> INVALID_SHELVES = List.of("Too read", "Readin", "Do not finish", "Shelf");
    private static final String ERROR_MSG = "Shelf with name ''{0}'' does not match any predefined shelf";

    @BeforeAll
    public static void setupBeforeAll(@Autowired PredefinedShelfService predefinedShelfService) {
        predefinedShelfUtils = new PredefinedShelfUtils(predefinedShelfService);
    }

    @BeforeEach
    void setup() {
        bookRepository.deleteAll();
        shelfRepository.deleteAll();

        toReadShelf = shelfRepository.save(new PredefinedShelf(PredefinedShelf.ShelfName.TO_READ));
        PredefinedShelf readingShelf = shelfRepository.save(new PredefinedShelf(PredefinedShelf.ShelfName.READING));
        readShelf = shelfRepository.save(new PredefinedShelf(PredefinedShelf.ShelfName.READ));
        didNotFinishShelf = shelfRepository.save(new PredefinedShelf(PredefinedShelf.ShelfName.DID_NOT_FINISH));

        book1 = bookRepository.save(new Book("someTitle", NO_AUTHOR, toReadShelf));
        book2 = bookRepository.save(new Book("someTitle2", NO_AUTHOR, toReadShelf));
        book3 = bookRepository.save(new Book("someOtherTitle", NO_AUTHOR, readShelf));
        book4 = bookRepository.save(new Book("yetAnotherTitle", NO_AUTHOR, didNotFinishShelf));

        toReadShelf.setBooks(Set.of(book1, book2));
        readingShelf.setBooks(Set.of());
        readShelf.setBooks(Set.of(book3));
        didNotFinishShelf.setBooks(Set.of(book4));
    }

    @Test
    void shouldGetAllPredefinedShelfNamesFromDatabase() {
        // given
        List<String> expectedShelfNames = List.of(
                PredefinedShelf.ShelfName.TO_READ.toString(),
                PredefinedShelf.ShelfName.READING.toString(),
                PredefinedShelf.ShelfName.READ.toString(),
                PredefinedShelf.ShelfName.DID_NOT_FINISH.toString()
        );

        // when
        List<String> shelfNames = predefinedShelfUtils.getPredefinedShelfNamesAsStrings();

        // then
        Assertions.assertEquals(expectedShelfNames, shelfNames);
    }

    @Test
    void shouldGetBooksInOneChosenShelf() {
        // given
        Set<Book> expectedBooks = Set.of(book1, book2);

        // when
        Set<Book> actualBooks = predefinedShelfUtils.getBooksInChosenPredefinedShelf("To read");

        // then
        Assertions.assertEquals(expectedBooks, actualBooks);
    }

    @Test
    void shouldGetAllBooksWhenChosenShelfIsAllShelves() {
        // given
        Set<Book> expectedBooks = Set.of(book1, book2, book3, book4);

        // when
        Set<Book> actualBooks = predefinedShelfUtils.getBooksInChosenPredefinedShelf(ALL_BOOKS_SHELF);

        // then
        Assertions.assertEquals(expectedBooks, actualBooks);
    }

    @Test
    void shouldGetAllBooksInChosenShelves() {
        // given
        List<PredefinedShelf> predefinedShelves = List.of(toReadShelf, readShelf);
        Set<Book> expectedBooks = Set.of(book1, book2, book3);

        // when
        Set<Book> actualBooks = predefinedShelfUtils.getBooksInPredefinedShelves(predefinedShelves);

        // then
        Assertions.assertEquals(expectedBooks.size(), actualBooks.size());
        Assertions.assertTrue(actualBooks.containsAll(expectedBooks));
    }

    @Test
    void testAllPredefinedShelves() {
        SoftAssertions softly = new SoftAssertions();

        PREDEFINED_SHELVES.stream().forEach(shelfName -> {
            String errorMsg = MessageFormat.format(ERROR_MSG, shelfName);
            softly.assertThat(PredefinedShelfUtils.isPredefinedShelf(shelfName)).as(errorMsg).isTrue();
        });

        softly.assertAll();
    }

    @Test
    void testAllPredefinedShelvesLowerCase() {
        SoftAssertions softly = new SoftAssertions();

        PREDEFINED_SHELVES.stream().map(String::toLowerCase).forEach(shelfName -> {
            String errorMsg = MessageFormat.format(ERROR_MSG, shelfName);
            softly.assertThat(PredefinedShelfUtils.isPredefinedShelf(shelfName)).as(errorMsg).isTrue();
        });

        softly.assertAll();
    }

    @Test
    void testAllPredefinedShelvesUpperCase() {
        SoftAssertions softly = new SoftAssertions();

        PREDEFINED_SHELVES.stream().map(String::toUpperCase).forEach(shelfName -> {
            String errorMsg = MessageFormat.format(ERROR_MSG, shelfName);
            softly.assertThat(PredefinedShelfUtils.isPredefinedShelf(shelfName)).as(errorMsg).isTrue();
        });

        softly.assertAll();
    }

    @Test
    void testInvalidShelves() {
        SoftAssertions softly = new SoftAssertions();

        INVALID_SHELVES.stream().forEach(shelfName -> {
            String errorMsg = MessageFormat.format(ERROR_MSG, shelfName);
            softly.assertThat(PredefinedShelfUtils.isPredefinedShelf(shelfName)).as(errorMsg).isFalse();
        });

        softly.assertAll();
    }
}