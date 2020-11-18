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
            List<? extends GoodreadsBookImport> goodreadsBookImports) {
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

        if (Objects.nonNull(goodreadsBookImport.getRating())) {
            Optional<RatingScale> ratingScale =
                    RatingScale.of(goodreadsBookImport.getRating() * 2);
            ratingScale.ifPresent(book::setRating);
        }

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
}
