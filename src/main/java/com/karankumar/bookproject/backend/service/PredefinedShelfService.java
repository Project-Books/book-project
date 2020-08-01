/*
 * The book project lets a user keep track of different books they've read, are currently reading or would like to read
 * Copyright (C) 2020 Karan Kumar
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.Genre;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.RatingScale;
import com.karankumar.bookproject.backend.entity.Tag;
import com.karankumar.bookproject.backend.repository.AuthorRepository;
import com.karankumar.bookproject.backend.repository.BookRepository;
import com.karankumar.bookproject.backend.repository.PredefinedShelfRepository;
import com.karankumar.bookproject.backend.repository.TagRepository;


import lombok.extern.java.Log;
import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log
public class PredefinedShelfService extends BaseService<PredefinedShelf, Long> {

    private BookRepository bookRepository;
    private PredefinedShelfRepository shelfRepository;
    private AuthorRepository authorRepository;
    private TagRepository tagRepository;

    public PredefinedShelfService(BookRepository bookRepository, AuthorRepository authorRepository,
            PredefinedShelfRepository shelfRepository, TagRepository tagRepository) {
        this.bookRepository = bookRepository;
        this.shelfRepository = shelfRepository;

        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public PredefinedShelf findById(Long id) {
        return shelfRepository.getOne(id);
    }

    @Override
    public void save(PredefinedShelf shelf) {
        if (shelf != null) {
            LOGGER.log(Level.INFO, "Saving shelf: " + shelf);
            shelfRepository.save(shelf);
        } else {
            LOGGER.log(Level.SEVERE, "Null Shelf");
        }
    }

    public List<PredefinedShelf> findAll() {
        return shelfRepository.findAll();
    }

    public List<PredefinedShelf> findAll(PredefinedShelf.ShelfName shelfName) {
        if (shelfName == null) {
            return shelfRepository.findAll();
        } else {
            return shelfRepository.findByPredefinedShelfName(shelfName);
        }
    }

    @Override
    public void delete(PredefinedShelf shelf) {
        shelfRepository.delete(shelf);
    }

    @Override
    public void deleteAll() {
        // Don't want to delete the predefined shelves
        LOGGER.log(Level.INFO, "deleteAll() should not be called");
    }

    @PostConstruct
    public void populateTestData() {
        if (authorRepository.count() == 0) {
            populateAuthorRepository();
        }

        if (tagRepository.count() == 0) {
            populateTagRepository();
        }
        if (bookRepository.count() == 0) {
            populateBookRepository();
        }

        if (shelfRepository.count() == 0) {
            populateShelfRepository();
        }

        setShelfForAllBooks();
    }

    private void populateAuthorRepository() {
        authorRepository.saveAll(
                Stream.of(
                        "J.K. Rowling",
                        "Neil Gaiman",
                        "J.R.R Tolkien",
                        "Roald Dahl",
                        "Robert Galbraith",
                        "Dan Brown"
                )
                        .map(name -> {
                            String[] fullName = name.split(" ");
                            return new Author(fullName[0], fullName[1]);
                        })
                        .collect(Collectors.toList())
        );
    }

    private void populateBookRepository() {
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        List<Author> authors = authorRepository.findAll();
        List<Tag> tags = tagRepository.findAll();

        bookRepository.saveAll(
                Stream.of(
                        "Harry Potter and the Philosopher's stone",
                        "Stardust",
                        "Harry Potter and the Chamber of Secrets",
                        "Harry Potter and the Prisoner of Azkaban",
                        "Origin",
                        "Harry Potter and the Goblet of Fire",
                        "Harry Potter and the Order of Phoenix",
                        "Matilda",
                        "Harry Potter and the Half-Blood Prince",
                        "The Hobbit",
                        "Harry Potter and the Deathly Hallows"
                )
                      .map(title -> {
                          int min = 300;
                          int max = 1000;
                          int pages = (threadLocalRandom.nextInt(min, max + 1));
                          int pagesRead = (threadLocalRandom.nextInt(min, max + 1));
                          int series = (threadLocalRandom.nextInt(1, 10 + 1));
                          Author author = authors.get(threadLocalRandom.nextInt(authors.size()));
                          Book book = new Book(title, author);
                          Genre genre = Genre.values()[threadLocalRandom.nextInt(Genre.values().length)];
                          List<String> recommendedBy =
                                  Arrays.asList("John", "Thomas", "Christina", "Luke", "Sally");
                          String recommender = recommendedBy.get(threadLocalRandom.nextInt(recommendedBy.size()));
                          Tag tag = tags.get(threadLocalRandom.nextInt(tags.size()));
                          book.setTags(new HashSet<>(Collections.singletonList(tag)));
                          book.setGenre(genre);
                          book.setSeriesPosition(series);
                          book.setBookReview("Must Read Book. Really Enjoyed it");
                          book.setNumberOfPages(pages);
                          book.setPagesRead(pagesRead);
                          book.setBookRecommendedBy(recommender);
                          return book;
                      }).collect(Collectors.toList()));
    }

    private void populateShelfRepository() {
        List<Book> books = bookRepository.findAll();
        shelfRepository.saveAll(
                Stream.of(PredefinedShelf.ShelfName.values())
                        .map(book -> {
                            PredefinedShelf shelf = new PredefinedShelf(book);
                            shelf.setBooks(new HashSet<>(books));
                            return shelf;
                        }).collect(Collectors.toList())
        );
    }

    private void populateTagRepository() {
        tagRepository.saveAll(
                Stream.of(
                        "Adventure",
                        "Interesting",
                        "Tolkien",
                        "Pokemon"
                ).map(Tag::new).collect(Collectors.toList())
        );
    }

    /**
     * Assigns a shelf to every book in the book repository
     */
    private void setShelfForAllBooks() {
        List<Book> books = bookRepository.findAll();
        List<PredefinedShelf> shelves = shelfRepository.findAll();

        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

        for (Book book : books) {
            PredefinedShelf shelf = shelves.get(threadLocalRandom.nextInt(shelves.size()));
            PredefinedShelf.ShelfName predefinedShelfName = shelf.getPredefinedShelfName();
            book.setShelf(shelf);
            switch (predefinedShelfName) {
                case TO_READ:
                    book.setDateStartedReading(null);
                    book.setDateFinishedReading(null);
                    book.setRating(RatingScale.NO_RATING);
                    break;
                case READING:
                case DID_NOT_FINISH:
                    book.setDateStartedReading(LocalDate.now().minusDays(2));
                    book.setDateFinishedReading(null);
                    book.setRating(RatingScale.NO_RATING);
                    break;
                case READ:
                    book.setRating(RatingScale.values()[threadLocalRandom.nextInt(RatingScale.values().length)]);
                    book.setDateStartedReading(LocalDate.now().minusDays(2));
                    book.setDateFinishedReading(LocalDate.now());
                    break;
                default:
            }
        }
        bookRepository.saveAll(books);
    }
}
