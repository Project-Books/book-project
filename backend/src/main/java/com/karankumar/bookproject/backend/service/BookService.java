/*
    The book project lets a user keep track of different books they would like to read, are currently
    reading, have read or did not finish.
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY
    WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
    PURPOSE.  See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with this program.
    If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.karankumar.bookproject.backend.dto.BookPatchDto;
import com.karankumar.bookproject.backend.model.Author;
import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.BookFormat;
import com.karankumar.bookproject.backend.model.BookGenre;
import com.karankumar.bookproject.backend.model.PredefinedShelf.ShelfName;
import com.karankumar.bookproject.backend.model.Publisher;
import com.karankumar.bookproject.backend.model.Shelf;
import com.karankumar.bookproject.backend.repository.BookRepository;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;

import static java.util.stream.Collectors.toSet;

@Service
@Log
@Transactional
public class BookService {

  private final AuthorService authorService;
  private final BookRepository bookRepository;
  private final PublisherService publisherService;
  private final PredefinedShelfService predefinedShelfService;

  public BookService(BookRepository bookRepository, AuthorService authorService,
      PublisherService publisherService, PredefinedShelfService predefinedShelfService) {
    this.bookRepository = bookRepository;
    this.authorService = authorService;
    this.publisherService = publisherService;
    this.predefinedShelfService = predefinedShelfService;
  }

  public Optional<Book> findById(@NonNull Long id) {
    return bookRepository.findBookById(id);
//        return bookRepository.findById(id);
  }

  public Optional<Book> save(@NonNull Book book) {
    if (bookHasAuthorAndPredefinedShelf(book)) {
      addBookToAuthor(book);
      addBookToPublisher(book);
      authorService.save(book.getAuthor());
      return Optional.of(bookRepository.save(book));
    }
    return Optional.empty();
  }

  private boolean bookHasAuthorAndPredefinedShelf(Book book) {
    return book.getAuthor() != null && book.getPredefinedShelf() != null;
  }

  private void addBookToAuthor(Book book) {
    Author author = book.getAuthor();
    Set<Book> authorBooks = author.getBooks();
    authorBooks.add(book);
    author.setBooks(authorBooks);
  }

  private void addBookToPublisher(Book book) {
    Set<Publisher> publishers = book.getPublishers();
    if (publishers != null && !publishers.isEmpty()) {
      for (Publisher publisher : publishers) {
        book.addPublisher(publisher);
        publisherService.save(publisher);
      }
    }
  }

  public Long count() {
    return bookRepository.count();
  }

  public List<Book> findAll() {
    return bookRepository.findAllBooks();
  }

  public List<Book> findAll(String filterText) {
    if (filterText == null || filterText.isEmpty()) {
      return findAll();
    }
    return bookRepository.findByTitleContainingIgnoreCase(filterText);
  }

  public void delete(@NonNull Book book) {
    bookRepository.delete(book);

    if (!bookRepository.existsById(book.getId())) {
      Author author = book.getAuthor();
      // TODO: fix method. It returns lazy initialization exception
      removeAuthorWithoutBooks(author);
    }
  }

  private void removeAuthorWithoutBooks(@NonNull Author author) {
    if (author.getBooks().isEmpty()) {
      authorService.delete(author);
    }
  }

  public void deleteAll() {
    LOGGER.log(Level.INFO, "Deleting all in books & authors. Book repository size = " +
        bookRepository.count());
    bookRepository.deleteAll();
    authorService.deleteAll();

    LOGGER.log(
        Level.INFO, "Deleted all books in books & authors. Book repository size = " +
            bookRepository.count()
    );
  }

  public String getJsonRepresentationForBooksAsString() throws JsonProcessingException {
    List<Book> books = bookRepository.findAll();

    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    ObjectWriter jsonWriter = mapper.writer().withRootName("AllBooks");

    return jsonWriter.writeValueAsString(books);
  }

  // TODO: split into findByShelfAndTitle and findShelfAndAuthor queries, and then merge result sets
  public List<Book> findByShelfAndTitleOrAuthor(Shelf shelf, String title, String authorsName) {
    throw new NotImplementedException();
//        return bookRepository.findByShelfAndTitleOrAuthor(shelf, title, authorsName);
  }

  public List<Book> findByTitleOrAuthor(String title, String authorsName) {
    throw new NotImplementedException();
//        return bookRepository.findByTitleOrAuthor(title, authorsName);
  }

  public List<Book> findAllBooksByPredefinedShelfName(ShelfName predefinedShelfName) {
    return bookRepository.findAllBooksByPredefinedShelfShelfName(predefinedShelfName);
  }

  public Book updateBook(Book book, BookPatchDto bookPatchDto) {
    updateBookMetadata(book, bookPatchDto);
    updateAuthor(book, bookPatchDto);
    updateGenres(book, bookPatchDto);
    updatePredefinedShelf(book, bookPatchDto);
    bookRepository.save(book);
    return book;
  }

  private void updateBookMetadata(Book book, BookPatchDto bookPatchDto) {
    Optional.ofNullable(bookPatchDto.getTitle())
        .ifPresent(book::setTitle);
    Optional.ofNullable(bookPatchDto.getNumberOfPages())
        .ifPresent(book::setNumberOfPages);
    Optional.ofNullable(bookPatchDto.getPagesRead())
        .ifPresent(book::setPagesRead);
    Optional.ofNullable(bookPatchDto.getBookFormat())
        .map(BookFormat::valueOf)
        .ifPresent(book::setBookFormat);
    Optional.ofNullable(bookPatchDto.getSeriesPosition())
        .ifPresent(book::setSeriesPosition);
    Optional.ofNullable(bookPatchDto.getEdition())
        .ifPresent(book::setEdition);
    Optional.ofNullable(bookPatchDto.getBookRecommendedBy())
        .ifPresent(book::setBookRecommendedBy);
    Optional.ofNullable(bookPatchDto.getIsbn())
        .ifPresent(book::setIsbn);
    Optional.ofNullable(bookPatchDto.getYearOfPublication())
        .ifPresent(book::setYearOfPublication);
    Optional.ofNullable(bookPatchDto.getBookReview())
        .ifPresent(book::setBookReview);
  }

  private void updateAuthor(Book book, BookPatchDto bookPatchDto) {
    Optional.ofNullable(bookPatchDto.getAuthor())
        .ifPresent(author -> {
          book.setAuthor(author);
          authorService.save(book.getAuthor());
        });
  }

  private void updateGenres(Book book, BookPatchDto bookPatchDto) {
    Optional.ofNullable(bookPatchDto.getBookGenres())
        .map(genres -> genres.stream().map(BookGenre::valueOf).collect(toSet()))
        .ifPresent(book::setBookGenre);
  }

  private void updatePredefinedShelf(Book book, BookPatchDto bookPatchDto) {
    Optional.ofNullable(bookPatchDto.getPredefinedShelf())
        .map(ShelfName::valueOf)
        .flatMap(predefinedShelfService::getPredefinedShelfByPredefinedShelfName)
        .ifPresent(book::setPredefinedShelf);
  }

}
