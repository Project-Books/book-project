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

package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.model.Author;
import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.PredefinedShelf;
import com.karankumar.bookproject.backend.model.Publisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@IntegrationTest
@DisplayName("PublisherService should")
class PublisherServiceTest {

    private final PublisherService publisherService;
    private final BookService bookService;
    private final PredefinedShelfService predefinedShelfService;

    @Autowired
    PublisherServiceTest(PublisherService publisherService, BookService bookService,
                         PredefinedShelfService predefinedShelfService) {
        this.publisherService = publisherService;
        this.bookService = bookService;
        this.predefinedShelfService = predefinedShelfService;
    }

    @BeforeEach
    public void setUp() {
        resetServices();
    }

    void resetServices() {
        bookService.deleteAll();
        publisherService.deleteAll();
    }

    @Test
    void saveValidPublisher() {
        // given
        Publisher publisher = new Publisher("Test SavePublisher ");

        // when
        publisherService.save(publisher);

        // then
        assertThat(publisherService.findById(publisher.getId())).isPresent();
    }

    @Test
    void notSaveAPublisherWithEmptyName() {
        // given
        Long initialCount = publisherService.count();

        // when
        publisherService.save(new Publisher(""));

        // then
        assertThat(publisherService.count()).isEqualTo(initialCount);
    }

    @Test
    @DisplayName("throw exception on an attempt to save a null Publisher")
    void throwExceptionWhenSavingANullPublisher() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> publisherService.save(null));
    }

    @Test
    @DisplayName("throw exception on an attempt to delete a null Publisher")
    void throwExceptionWhenDeletingANullPublisher() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> publisherService.delete(null));
    }

    @Test
    @Transactional
    @DisplayName("Delete a Publisher")
    void deleteExistingPublisher() {
        // given
        Publisher publisher = new Publisher("Test DeletePublisher");
        publisherService.save(publisher);
        Long publisherId = publisher.getId();

        // when
        publisherService.delete(publisher);

        // then
        Optional<Publisher> deletedPublisher = publisherService.findById(publisherId);
        assertThat(deletedPublisher).isEmpty();
    }

    @Test
    void findAllSavedPublishers() {
        // given
        Publisher publisher1 = new Publisher("Test SavePublisher1");
        Publisher publisher2 = new Publisher("Test SavePublisher2");
        publisherService.save(publisher1);
        publisherService.save(publisher2);

        // when
        List<Publisher> allPublishers = publisherService.findAll();

        // then
        assertThat(allPublishers).contains(publisher1, publisher2);
    }

    @Test
    @DisplayName("Throw exception when saving a new Publisher with existing name")
    void throwExceptionWhileSavingDuplicatePublisher() {
        // given
        Publisher publisher1 = new Publisher("Test DuplicatePublisher");
        Publisher publisher2 = new Publisher("Test DuplicatePublisher");

        // when
        publisherService.save(publisher1);

        // then
        assertThatThrownBy(() -> publisherService.save(publisher2)).isInstanceOf(
                DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("throw exception on attempt to add a null book to a publisher")
    void throwExceptionForNullBookInAddBookToPublisher() {
        // given
        Publisher publisher = new Publisher("Test");

        // when and then
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> publisherService.addBookToPublisher(null, publisher));
    }

    @Test
    @Disabled
    // TODO: fix failing test. This does not retrieve the correct books
    void addBookToPublisher() {
        // given
        PredefinedShelf shelf = predefinedShelfService.findToReadShelf();
        Book book = new Book("Title", new Author("First Last"), shelf);
        bookService.save(book);

        Publisher publisher = new Publisher("Publisher");
        Long initialCount = publisherService.count();

        // when
        publisherService.addBookToPublisher(book, publisher);
        Optional<Publisher> foundPublisher = publisherService.findById(publisher.getId());

        // then
        assertSoftly(softly -> {
            softly.assertThat(publisherService.count()).isEqualTo(initialCount + 1);
            softly.assertThat(foundPublisher).isPresent();
            softly.assertThat(foundPublisher.get().getBooks()).contains(book);
        });
    }

    @Test
    @DisplayName("throw exception on attempt to add a book to a null publisher")
    void throwExceptionForNullPublisherInAddBookToPublisher() {
        // given
        Book book = new Book("Title", new Author("a b"), null);

        // when and then
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> publisherService.addBookToPublisher(book, null));
    }
}
