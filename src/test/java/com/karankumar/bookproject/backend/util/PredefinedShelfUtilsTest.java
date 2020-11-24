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
import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName;
import com.karankumar.bookproject.backend.repository.BookRepository;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;

import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.DID_NOT_FINISH;
import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.READ;
import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.READING;
import static com.karankumar.bookproject.backend.entity.PredefinedShelf.ShelfName.TO_READ;
import static com.karankumar.bookproject.backend.util.PredefinedShelfUtils.getBooksInPredefinedShelves;
import static com.karankumar.bookproject.backend.util.PredefinedShelfUtils.getPredefinedShelfName;
import static com.karankumar.bookproject.backend.util.PredefinedShelfUtils.isPredefinedShelf;
import static com.karankumar.bookproject.backend.util.ShelfUtils.ALL_BOOKS_SHELF;
import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.api.SoftAssertions;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@IntegrationTest
@DisplayName("PredefinedShelfUtils should")
class PredefinedShelfUtilsTest {
    private final BookRepository bookRepository;
    private final PredefinedShelfService predefinedShelfService;

    private PredefinedShelf toReadShelf;
    private PredefinedShelf readShelf;
    private PredefinedShelf didNotFinishShelf;

    private Book book1;
    private Book book2;
    private Book book3;
    private Book book4;

    private static final Author NO_AUTHOR = null;

    private List<String> PREDEFINED_SHELVES;
    private static final List<String> INVALID_SHELVES =
            List.of("Too read", "Readin", "Do not finish", "Shelf");
    private static final String ERROR_MESSAGE =
            "Shelf with name ''{0}'' does not match any predefined shelf";

    @Autowired
    PredefinedShelfUtilsTest(BookRepository bookRepository,
                             PredefinedShelfService predefinedShelfService) {
        this.bookRepository = bookRepository;
        this.predefinedShelfService = predefinedShelfService;
    }

    @BeforeEach
    public void setUp() {
        findPredefinedShelves(predefinedShelfService);

        resetBookRepository();
        createAndSaveBooks();

        PREDEFINED_SHELVES = predefinedShelfService.getPredefinedShelfNamesAsStrings();

        setBooksInPredefinedShelves();
    }

    private void findPredefinedShelves(PredefinedShelfService predefinedShelfService) {
        toReadShelf = predefinedShelfService.findToReadShelf();
        readShelf =  predefinedShelfService.findReadShelf();
        didNotFinishShelf = predefinedShelfService.findDidNotFinishShelf();
    }

    private void resetBookRepository() {
        bookRepository.deleteAll();
    }

    private void setBooksInPredefinedShelves() {
        toReadShelf.setBooks(Set.of(book1, book2));
        readShelf.setBooks(Set.of(book3));
        didNotFinishShelf.setBooks(Set.of(book4));
    }

    private void createAndSaveBooks() {
        book1 = bookRepository.save(new Book("someTitle", NO_AUTHOR, toReadShelf));
        book2 = bookRepository.save(new Book("someTitle2", NO_AUTHOR, toReadShelf));
        book3 = bookRepository.save(new Book("someOtherTitle", NO_AUTHOR, readShelf));
        book4 = bookRepository.save(new Book("yetAnotherTitle", NO_AUTHOR, didNotFinishShelf));
    }

    @Test
    void getAllPredefinedShelfNamesFromDatabase() {
        // given
        List<String> expectedShelfNames = List.of(
                TO_READ.toString(),
                READING.toString(),
                READ.toString(),
                DID_NOT_FINISH.toString()
        );

        // when
        List<String> shelfNames = predefinedShelfService.getPredefinedShelfNamesAsStrings();

        // then
        assertThat(shelfNames).isEqualTo(expectedShelfNames);
    }

    @Test
    void getBooksInOneChosenShelf() {
        // given
        Set<Book> expectedBooks = Set.of(book1, book2);
        String shelf = TO_READ.toString();

        // when
        Set<Book> actualBooks = predefinedShelfService.getBooksInChosenPredefinedShelf(shelf);

        // then
        assertThat(actualBooks).isEqualTo(expectedBooks);
    }

    @Test
    void getAllBooksWhenChosenShelfIsAllShelves() {
        // given
        Set<Book> expectedBooks = Set.of(book1, book2, book3, book4);

        // when
        Set<Book> actualBooks =
                predefinedShelfService.getBooksInChosenPredefinedShelf(ALL_BOOKS_SHELF);

        // then
        assertThat(actualBooks).isEqualTo(expectedBooks);
    }

    @Test
    void getAllBooksInChosenShelves() {
        // given
        List<PredefinedShelf> predefinedShelves = List.of(toReadShelf, readShelf);
        Set<Book> expectedBooks = Set.of(book1, book2, book3);

        // when
        Set<Book> actualBooks = getBooksInPredefinedShelves(predefinedShelves);

        // then
        assertSoftly(softly -> {
            softly.assertThat(actualBooks).hasSize(expectedBooks.size());
            softly.assertThat(actualBooks).containsAll(expectedBooks);
        });
    }

    @Test
    void determineIsPredefinedShelfCorrectlyForValidShelfName() {
        SoftAssertions softly = new SoftAssertions();

        PREDEFINED_SHELVES.forEach(shelfName -> softly.assertThat(isPredefinedShelf(shelfName))
                                                      .as(MessageFormat
                                                              .format(ERROR_MESSAGE, shelfName))
                                                      .isTrue());

        softly.assertAll();
    }

    @Test
    void determineIsPredefinedShelfCorrectlyForLowerCase() {
        SoftAssertions softly = new SoftAssertions();

        PREDEFINED_SHELVES.stream()
                          .map(String::toLowerCase)
                          .forEach(shelfName ->
                                  softly.assertThat(isPredefinedShelf(shelfName))
                                        .as(MessageFormat.format(ERROR_MESSAGE, shelfName))
                                        .isTrue());

        softly.assertAll();
    }

    @Test
    void determineIsPredefinedShelfCorrectlyForUpperCase() {
        SoftAssertions softly = new SoftAssertions();

        PREDEFINED_SHELVES.stream()
                          .map(String::toUpperCase)
                          .forEach(shelfName ->
                                  softly.assertThat(isPredefinedShelf(shelfName))
                                        .as(MessageFormat.format(ERROR_MESSAGE, shelfName))
                                        .isTrue());

        softly.assertAll();
    }

    @Test
    void determineIsPredefinedShelfCorrectlyForInvalidShelfName() {
        SoftAssertions softly = new SoftAssertions();

        INVALID_SHELVES.forEach(shelfName -> softly.assertThat(isPredefinedShelf(shelfName))
                                                   .as(MessageFormat
                                                           .format(ERROR_MESSAGE, shelfName))
                                                   .isFalse());

        softly.assertAll();
    }

    @ParameterizedTest
    @ValueSource(strings = {"To read", "Reading", "Read", "Did not finish"})
    void returnCorrectShelfByPredefinedShelfName(String shelfName) {
        System.out.println("Shelf = " + shelfName);
        PredefinedShelf.ShelfName expectedShelf = null;
        switch (shelfName) {
            case "To read":
                expectedShelf = TO_READ;
                break;
            case "Reading":
                expectedShelf = READING;
                break;
            case "Read":
                expectedShelf = READ;
                break;
            case "Did not finish":
                expectedShelf = DID_NOT_FINISH;
        }
        PredefinedShelf.ShelfName actualShelf =
                getPredefinedShelfName(shelfName);
        assertThat(actualShelf).isEqualTo(expectedShelf);
    }
}
