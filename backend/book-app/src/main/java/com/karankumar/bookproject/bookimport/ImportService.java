/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2020  Karan Kumar
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.bookimport;

import com.karankumar.bookproject.book.model.Author;
import com.karankumar.bookproject.book.model.Book;
import com.karankumar.bookproject.shelf.model.UserCreatedShelf;
import com.karankumar.bookproject.shelf.model.PredefinedShelf;
import com.karankumar.bookproject.book.model.RatingScale;
import com.karankumar.bookproject.book.service.BookService;
import com.karankumar.bookproject.shelf.service.PredefinedShelfService;
import com.karankumar.bookproject.shelf.service.UserCreatedShelfService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Slf4j
class ImportService {
  private static final double GOODREADS_RATING_SCALE_FACTOR = 2;

  private final BookService bookService;
  private final PredefinedShelfService predefinedShelfService;
  private final UserCreatedShelfService userCreatedShelfService;

  public ImportService(
      BookService bookService,
      PredefinedShelfService predefinedShelfService,
      UserCreatedShelfService userCreatedShelfService) {
    this.bookService = bookService;
    this.predefinedShelfService = predefinedShelfService;
    this.userCreatedShelfService = userCreatedShelfService;
  }

  /**
   * Imports the books which are in Goodreads format
   *
   * @param goodreadsBookImports the books to import
   * @return the list of books saved successfully
   */
  public List<Book> importGoodreadsBooks(
      Collection<? extends GoodreadsBookImport> goodreadsBookImports) {
    if (CollectionUtils.isEmpty(goodreadsBookImports)) {
      LOGGER.info("Goodreads imports is empty");
      return Collections.emptyList();
    }

    List<Book> books = toBooks(goodreadsBookImports);

    List<Book> savedBooks =
        books.stream()
            .map(bookService::save)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    savedBooks.forEach(b -> LOGGER.info("Book: {} saved successfully", b));

    return savedBooks;
  }

  private List<Book> toBooks(Collection<? extends GoodreadsBookImport> goodreadsBookImports) {
    return goodreadsBookImports.stream()
        .map(this::toBook)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
  }

  private Optional<Book> toBook(GoodreadsBookImport goodreadsBookImport) {
    if (StringUtils.isBlank(goodreadsBookImport.getTitle())) {
      LOGGER.error("Title is blank for import: {}", goodreadsBookImport);
      return Optional.empty();
    }

    Optional<Author> author = toAuthor(goodreadsBookImport.getAuthor());
    if (author.isEmpty()) {
      LOGGER.error("Author is null for import: {}", goodreadsBookImport);
      return Optional.empty();
    }

    Optional<PredefinedShelf> predefinedShelf =
        toPredefinedShelf(
            goodreadsBookImport.getBookshelves(),
            goodreadsBookImport.getDateRead(),
            GoodreadsBookImport::toPredefinedShelfName);
    if (predefinedShelf.isEmpty()) {
      LOGGER.error("Predefined shelf is null for import: {}", goodreadsBookImport);
      return Optional.empty();
    }

    Book book = new Book(goodreadsBookImport.getTitle(), author.get(), predefinedShelf.get());

    Optional<UserCreatedShelf> customShelf =
        toCustomShelf(
            goodreadsBookImport.getBookshelves(), GoodreadsBookImport::toPredefinedShelfName);
    customShelf.ifPresent(book::setUserCreatedShelf);

    Optional<RatingScale> ratingScale =
        toRatingScale(goodreadsBookImport.getRating(), GOODREADS_RATING_SCALE_FACTOR);
    ratingScale.ifPresent(book::setRating);

    return Optional.of(book);
  }

  private Optional<Author> toAuthor(String name) {
    if (StringUtils.isBlank(name)) {
      return Optional.empty();
    }

    return Optional.of(new Author(name));
  }

  private Optional<PredefinedShelf> toPredefinedShelf(
      String shelves,
      LocalDate dateRead,
      Function<String, Optional<PredefinedShelf.ShelfName>> predefinedShelfNameMapper) {
    if (Objects.nonNull(dateRead)) {
      return Optional.of(predefinedShelfService.findReadShelf());
    }
    if (StringUtils.isBlank(shelves)) {
      return Optional.empty();
    }
    String[] shelvesArray = shelves.trim().split(",");

    return Arrays.stream(shelvesArray)
        .map(predefinedShelfNameMapper)
        .filter(Optional::isPresent)
        .findFirst()
        .map(Optional::get)
        .map(predefinedShelfService::findByPredefinedShelfNameAndLoggedInUser)
        .map(Optional::get);
  }

  private Optional<UserCreatedShelf> toCustomShelf(
      String shelves,
      Function<String, Optional<PredefinedShelf.ShelfName>> predefinedShelfNameMapper) {
    if (StringUtils.isBlank(shelves)) {
      return Optional.empty();
    }
    String[] shelvesArray = shelves.trim().split(",");

    Predicate<String> isNotPredefinedShelf =
        s -> predefinedShelfNameMapper.andThen(Optional::isEmpty).apply(s);

    return Arrays.stream(shelvesArray)
        .filter(isNotPredefinedShelf)
        .findFirst()
        .map(String::trim)
        .map(userCreatedShelfService::findOrCreate);
  }

  private Optional<RatingScale> toRatingScale(Double ratingValue, double scaleFactor) {
    if (Objects.isNull(ratingValue)) {
      return Optional.of(RatingScale.NO_RATING);
    }
    return RatingScale.of(ratingValue * scaleFactor);
  }
}
