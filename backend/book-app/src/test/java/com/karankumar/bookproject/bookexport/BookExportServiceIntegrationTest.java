/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2021  Karan Kumar
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.bookexport;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.karankumar.bookproject.account.model.User;
import com.karankumar.bookproject.account.service.UserService;
import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.book.repository.PublisherRepository;
import com.karankumar.bookproject.book.repository.TagRepository;
import com.karankumar.bookproject.book.service.BookService;
import com.karankumar.bookproject.model.Author;
import com.karankumar.bookproject.model.Book;
import com.karankumar.bookproject.model.BookGenre;
import com.karankumar.bookproject.model.Publisher;
import com.karankumar.bookproject.model.Tag;
import com.karankumar.bookproject.shelf.model.PredefinedShelf;
import com.karankumar.bookproject.shelf.model.UserCreatedShelf;
import com.karankumar.bookproject.shelf.service.PredefinedShelfService;
import com.karankumar.bookproject.shelf.service.UserCreatedShelfService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@IntegrationTest
@Slf4j
class BookExportServiceIntegrationTest {

  @Autowired TagRepository tagRepository;
  @Autowired PublisherRepository publisherRepository;

  @Autowired UserService userService;
  @Autowired BookService bookService;
  @Autowired UserCreatedShelfService userCreatedShelfService;
  @Autowired PredefinedShelfService predefinedShelfService;
  @Autowired BookExportService bookExportService;

  @BeforeEach
  void setup() {
    reset();
  }

  void reset() {
    bookService.deleteAll();
    userCreatedShelfService.deleteAll();
  }

  @Test
  void shouldExportAllBookForUser() throws Exception {
    // given
    User user = userService.getCurrentUser();
    PredefinedShelf predefinedShelf = predefinedShelfService.findToReadShelf();
    UserCreatedShelf favouritesShelf = userCreatedShelfService.createCustomShelf("Favourites");
    UserCreatedShelf thrillersShelf =
        userCreatedShelfService.createCustomShelf("Classic Thrillers");
    userCreatedShelfService.save(favouritesShelf);
    userCreatedShelfService.save(thrillersShelf);

    Book book1 =
        createAndSaveBook(
            "Cloud Atlas",
            "David Mitchell",
            new Publisher("Sceptre"),
            BookGenre.FANTASY,
            singletonList(new Tag("Epic")),
            predefinedShelf,
            favouritesShelf);
    Book book2 =
        createAndSaveBook(
            "Fingersmith",
            "Sarah Waters",
            new Publisher("Virago Press"),
            BookGenre.THRILLER,
            singletonList(new Tag("Historical Thriller")),
            predefinedShelf,
            thrillersShelf);
    Book book3 =
        createAndSaveBook(
            "The Constant Gardener",
            "David Mitchell",
            new Publisher("John le Carré"),
            BookGenre.THRILLER,
            asList(new Tag("War Thriller"), new Tag("Classic")),
            predefinedShelf,
            null);
    ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();

    // when
    ExportDto booksData = bookExportService.exportBooksData(user);

    // then
    LOGGER.info(writer.writeValueAsString(booksData));
    assertSoftly(
        softly -> {
          softly.assertThat(booksData).isNotNull();
          softly.assertThat(booksData.getBooks()).hasSameElementsAs(asList(book1, book2, book3));
        });
  }

  private Book createAndSaveBook(
      String title,
      String authorName,
      Publisher publisher,
      BookGenre bookGenre,
      List<Tag> tags,
      PredefinedShelf predefinedShelf,
      UserCreatedShelf userCreatedShelf) {

    publisherRepository.save(publisher);
    tagRepository.saveAll(tags);

    Book book = new Book(title, new Author(authorName), predefinedShelf);
    book.setBookRecommendedBy("Loki");
    book.setNumberOfPages(650);
    tags.forEach(book::addTag);
    book.addPublisher(publisher);
    book.addGenre(bookGenre);
    book.setUserCreatedShelf(userCreatedShelf);
    bookService.save(book);
    return book;
  }
}
