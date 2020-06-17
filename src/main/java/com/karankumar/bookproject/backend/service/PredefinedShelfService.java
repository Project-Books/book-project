/*
    The book project lets a user keep track of different books they've read, are currently reading or would like to read
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.model.*;
import com.karankumar.bookproject.backend.repository.AuthorRepository;
import com.karankumar.bookproject.backend.repository.BookRepository;
import com.karankumar.bookproject.backend.repository.PredefinedShelfRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A Spring service that acts as the gateway to the {@code ShelfRepository} -- to use the {@code ShelfRepository},
 * a consumer should go via this {@code ShelfService}
 */
@Service
public class PredefinedShelfService extends BaseService<PredefinedShelf, Long> {
    private static final Logger LOGGER = Logger.getLogger(PredefinedShelfService.class.getSimpleName());

    private BookRepository bookRepository;
    private PredefinedShelfRepository shelfRepository;
    private AuthorRepository authorRepository;

    public PredefinedShelfService(BookRepository bookRepository, AuthorRepository authorRepository,
                                  PredefinedShelfRepository shelfRepository) {
        this.bookRepository = bookRepository;
        this.shelfRepository = shelfRepository;

        this.authorRepository = authorRepository;
    }

    @Override
    public PredefinedShelf findById(Long id) {
        return shelfRepository.getOne(id);
    }

    @Override
    public void save(PredefinedShelf shelf) {
        if (shelf != null) {
            shelfRepository.save(shelf);
        } else {
            LOGGER.log(Level.SEVERE, "Null Shelf");
        }
    }

    public List<PredefinedShelf> findAll() {
        return shelfRepository.findAll();
    }

    @Override
    public void delete(PredefinedShelf shelf) {
        shelfRepository.delete(shelf);
    }

    @PostConstruct
    public void populateTestData() {
        if (authorRepository.count() == 0) {
            authorRepository.saveAll(
                    Stream.of(
                            "J.K. Rowling",
                            "Neil Gaiman",
                            "J.R.R Tolkien",
                            "Roald Dahl",
                            "Robert Galbraith",
                            "Dan Brown")
                            .map(name -> {
                                String[] fullName = name.split(" ");
                                return new Author(fullName[0], fullName[1]);
                            })
                            .collect(Collectors.toList()));
        }

        if (bookRepository.count() == 0) {
            Random random = new Random(0);
            List<Author> authors = authorRepository.findAll();

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
                            "Harry Potter and the Deathly Hallows")
                            .map(title -> {
                                int min = 300;
                                int max = 1000;
                                int range = (max - min) + 1;
                                int pages = (int) (Math.random() * range);

                                Author author = authors.get(random.nextInt(authors.size()));
                                Book book = new Book(title, author);
                                Genre genre = Genre.values()[random.nextInt(Genre.values().length)];
                                book.setGenre(genre);
                                book.setNumberOfPages(pages);

                                return book;
                            }).collect(Collectors.toList()));
        }

        if (shelfRepository.count() == 0) {
            List<Book> books = bookRepository.findAll();
            shelfRepository.saveAll(
                    Stream.of(PredefinedShelf.ShelfName.values())
                            .map(b -> {
                                PredefinedShelf shelf = new PredefinedShelf(b);
                                shelf.setBooks(new HashSet<>(books));
                                return shelf;
                            }).collect(Collectors.toList()));
        }

        List<Book> books = bookRepository.findAll();
        List<PredefinedShelf> shelves = shelfRepository.findAll();

        Random random = new Random(0);

        for (Book book : books) {
            PredefinedShelf shelf = shelves.get(random.nextInt(shelves.size()));
            book.setShelf(shelf);
            switch (shelf.shelfName) {
                case TO_READ:
                    book.setDateStartedReading(null);
                    book.setDateFinishedReading(null);
                    book.setRating(RatingScale.NO_RATING);
                    break;
                case READING:
                    book.setDateStartedReading(LocalDate.now().minusDays(2));
                    book.setDateFinishedReading(null);
                    book.setRating(RatingScale.NO_RATING);
                    break;
                case READ:
                    book.setRating(RatingScale.values()[random.nextInt(RatingScale.values().length)]);
                    book.setDateStartedReading(LocalDate.now().minusDays(2));
                    book.setDateFinishedReading(LocalDate.now());
                    break;
            }
        }
        bookRepository.saveAll(books);
    }
}
