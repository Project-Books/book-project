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
import com.karankumar.bookproject.backend.model.shelves.ReadShelf;
import com.karankumar.bookproject.backend.model.shelves.ReadingShelf;
import com.karankumar.bookproject.backend.model.shelves.Shelf;
import com.karankumar.bookproject.backend.model.shelves.ToReadShelf;
import com.karankumar.bookproject.backend.repository.AuthorRepository;
import com.karankumar.bookproject.backend.repository.BookRepository;
import com.karankumar.bookproject.backend.repository.ShelfRepository;
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
public class ShelfService extends BaseService<Shelf, Long> {
    private static final Logger LOGGER = Logger.getLogger(ShelfService.class.getSimpleName());

    private BookRepository bookRepository;
    private ShelfRepository shelfRepository;
    private AuthorRepository authorRepository;

    public ShelfService(BookRepository bookRepository, AuthorRepository authorRepository,
                        ShelfRepository shelfRepository) {
        this.bookRepository = bookRepository;
        this.shelfRepository = shelfRepository;

        this.authorRepository = authorRepository;
    }

    @Override
    public Shelf findById(Long id) {
        return shelfRepository.getOne(id);
    }

    @Override
    public void save(Shelf shelf) {
        if (shelf != null) {
            shelfRepository.save(shelf);
        } else {
            LOGGER.log(Level.SEVERE, "Null Shelf");
        }
    }

    public List<Shelf> findAll() {
        return shelfRepository.findAll();
    }

    @Override
    public void delete(Shelf shelf) {
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
                            .map(
                                    name -> {
                                        String[] fullName = name.split(" ");


                                        Author author = new Author();
                                        author.setFirstName(fullName[0]);
                                        author.setLastName(fullName[1]);
                                        return author;
                                    })
                            .collect(Collectors.toList()));
        }

        if (bookRepository.count() == 0) {
            Random random = new Random(0);
            List<Author> authors = authorRepository.findAll();

            bookRepository.saveAll(
                    Stream.of(
                            "Harry Potter and the Philosopher's stone",
                            "Harry Potter and the Chamber of Secrets",
                            "Harry Potter and the Prisoner of Azkaban",
                            "Harry Potter and the Goblet of Fire",
                            "Harry Potter and the Order of Phoenix",
                            "Harry Potter and the Half-Blood Prince",
                            "Harry Potter and the Deathly Hallows")
                            .map(title -> {
                                int min = 300;
                                int max = 1000;
                                int range = (max - min) + 1;
                                int pages = (int) (Math.random() * range);

                                Book book = new Book();
                                book.setTitle(title);

                                book.setAuthor(authors.get(0));

                                Genre genre = Genre.values()[random.nextInt(Genre.values().length)];

                                book.setGenre(genre);
                                book.setNumberOfPages(pages);

                                book.setDateStartedReading(LocalDate.now().minusDays(2));
                                book.setDateFinishedReading(LocalDate.now());

                                book.setRating(RatingScale.values()[random.nextInt(RatingScale.values().length)]);

                                return book;
                            }).collect(Collectors.toList()));
        }

        if (shelfRepository.count() == 0) {
            List<Book> books = bookRepository.findAll();
            Random random = new Random(0);
            shelfRepository.saveAll(
                    Stream.of("a")
                            .map(b -> {
                                int shelfNum = random.nextInt(3);
                                Shelf shelf;
                                if (shelfNum == 0) {
                                    shelf = new ToReadShelf();
                                } else if (shelfNum == 1) {
                                    shelf = new ReadingShelf();
                                } else {
                                    shelf = new ReadShelf();
                                }

                                shelf.setBooks(new HashSet<>(books));
                                return shelf;
                            }).collect(Collectors.toList()));
        }

        List<Book> books = bookRepository.findAll();
        System.out.println("Books size = " + books.size());
        List<Shelf> shelves = shelfRepository.findAll();
        System.out.println("Shelf size = " + shelves.size());

        Random random = new Random(0);

        for (Book book : books) {
            Shelf shelf = shelves.get(random.nextInt(shelves.size()));
            book.setShelf(shelf);
            System.out.println("Setting book " + book.getTitle() + " to shelf " + shelf.getName());
        }
        bookRepository.saveAll(books);
    }
}
