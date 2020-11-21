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

package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.dto.GoodreadsBookImport;
import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.CustomShelf;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.RatingScale;
import com.karankumar.bookproject.backend.util.PredefinedShelfUtils;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Log
public class ImportService {
    private final BookService bookService;
    private final PredefinedShelfService predefinedShelfService;
    private final CustomShelfService customShelfService;

    private static final int GOODREADS_SCALE_FACTOR = 2;

    public ImportService(BookService bookService,
                         PredefinedShelfService predefinedShelfService,
                         CustomShelfService customShelfService) {
        this.bookService = bookService;
        this.predefinedShelfService = predefinedShelfService;
        this.customShelfService = customShelfService;
    }

    /**
     * Imports the books which are in Goodreads format
     * @param goodreadsBookImports the books to import
     * @return the list of books saved successfully
     */
    public List<Book> importGoodreadsBooks(
            Collection<? extends GoodreadsBookImport> goodreadsBookImports) {
        List<Book> books = toBooks(goodreadsBookImports);
        List<Book> savedBooks = books.stream()
                                     .map(bookService::save)
                                     .filter(Optional::isPresent)
                                     .map(Optional::get)
                                     .collect(Collectors.toList());
        savedBooks.forEach(b -> LOGGER.info("Book: " + b + " saved successfully"));
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
        Optional<Author> author = toAuthor(goodreadsBookImport.getAuthor());
        if (author.isEmpty()) {
            LOGGER.severe("Author is null");
            return Optional.empty();
        }

        Optional<PredefinedShelf> predefinedShelf =
                toPredefinedShelf(goodreadsBookImport.getBookshelves(),
                        goodreadsBookImport.getDateRead());
        if (predefinedShelf.isEmpty()) {
            LOGGER.severe("Predefined shelf is null");
            return Optional.empty();
        }

        Book book = new Book(goodreadsBookImport.getTitle(), author.get(), predefinedShelf.get());

        Optional<CustomShelf> customShelf = toCustomShelf(goodreadsBookImport.getBookshelves());
        customShelf.ifPresent(book::setCustomShelf);

        Optional<RatingScale> ratingScale =
                toRatingScale(goodreadsBookImport.getRating(), GOODREADS_SCALE_FACTOR);
        ratingScale.ifPresent(book::setRating);

        return Optional.of(book);
    }

    private Optional<Author> toAuthor(String name) {
        if (StringUtils.isBlank(name)) {
            return Optional.empty();
        }
        String[] authorNames = name.split(" ");
        return Optional
                .of(new Author(authorNames[0], authorNames.length > 1 ? authorNames[1] : null));
    }

    private Optional<PredefinedShelf> toPredefinedShelf(String shelves, LocalDate dateRead) {
        if (Objects.nonNull(dateRead)) {
            return Optional.of(predefinedShelfService.findToReadShelf());
        }
        if (StringUtils.isBlank(shelves)) {
            return Optional.empty();
        }
        String[] shelvesArray = shelves.split(",");
        return Arrays.stream(shelvesArray)
                     .filter(PredefinedShelfUtils::isPredefinedShelf)
                     .findFirst()
                     .map(predefinedShelfService::getPredefinedShelfByNameAsString);
    }

    private Optional<CustomShelf> toCustomShelf(String shelves) {
        if (StringUtils.isBlank(shelves)) {
            return Optional.empty();
        }
        String[] shelvesArray = shelves.split(",");
        return Arrays.stream(shelvesArray)
                     .filter(Predicate.not(PredefinedShelfUtils::isPredefinedShelf))
                     .findFirst()
                     .map(customShelfService::findOrCreate);
    }

    private Optional<RatingScale> toRatingScale(Double ratingValue, double scaleFactor) {
        if (Objects.isNull(ratingValue)) {
            return Optional.of(RatingScale.NO_RATING);
        }
        return RatingScale.of(ratingValue * scaleFactor);
    }
}
