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

import com.karankumar.bookproject.backend.entity.book.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.account.User;
import com.karankumar.bookproject.backend.repository.AuthorRepository;
import com.karankumar.bookproject.backend.repository.BookRepository;
import com.karankumar.bookproject.backend.repository.PredefinedShelfRepository;
import com.karankumar.bookproject.backend.repository.TagRepository;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.karankumar.bookproject.backend.utils.TestData.generateAuthors;
import static com.karankumar.bookproject.backend.utils.TestData.generateBooks;
import static com.karankumar.bookproject.backend.utils.TestData.generateListOfTags;
import static com.karankumar.bookproject.backend.utils.TestData.setPredefinedShelfForBooks;

@Service
@Log
public class PredefinedShelfService {

    private final BookRepository bookRepository;
    private final PredefinedShelfRepository predefinedShelfRepository;
    private final AuthorRepository authorRepository;
    private final TagRepository tagRepository;
    private final UserService userService;

    public PredefinedShelfService(BookRepository bookRepository, AuthorRepository authorRepository,
            PredefinedShelfRepository shelfRepository, TagRepository tagRepository, UserService userService) {
        this.bookRepository = bookRepository;
        this.predefinedShelfRepository = shelfRepository;

        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
        this.userService = userService;
    }

    public PredefinedShelf findById(Long id) {
        return predefinedShelfRepository.getOne(id);
    }

    public void save(PredefinedShelf shelf) {
        if (shelf != null) {
            LOGGER.log(Level.INFO, "Saving shelf: " + shelf);
            predefinedShelfRepository.save(shelf);
        } else {
            LOGGER.log(Level.SEVERE, "Null Shelf");
        }
    }

    public List<PredefinedShelf> findAllForLoggedInUser() {
        return predefinedShelfRepository.findAllByUser(userService.getCurrentUser());
    }

    public PredefinedShelf findToReadShelf() {
        return findByPredefinedShelfNameAndLoggedInUser(PredefinedShelf.ShelfName.TO_READ);
    }

    public PredefinedShelf findReadingShelf() {
        return findByPredefinedShelfNameAndLoggedInUser(PredefinedShelf.ShelfName.READING);
    }

    public PredefinedShelf findReadShelf() {
        return findByPredefinedShelfNameAndLoggedInUser(PredefinedShelf.ShelfName.READ);
    }

    public PredefinedShelf findDidNotFinishShelf() {
        return findByPredefinedShelfNameAndLoggedInUser(PredefinedShelf.ShelfName.DID_NOT_FINISH);
    }

    public PredefinedShelf findByPredefinedShelfNameAndLoggedInUser(PredefinedShelf.ShelfName shelfName) {
        return predefinedShelfRepository.findByPredefinedShelfNameAndUser(shelfName, userService.getCurrentUser());
    }

    public Long count() {
        return predefinedShelfRepository.count();
    }

    @PostConstruct
    public void populateTestData() {
        if (authorRepository.count() == 0) {
            populateAuthorRepository();
        }

        if (tagRepository.count() == 0) {
            populateTagRepository();
        }

        for (User user : userService.findAll()) {
            if (predefinedShelfRepository.countAllByUser(user) == 0) {
                List<PredefinedShelf> predefinedShelves = populateShelfRepository(user);
                populateBookRepository(predefinedShelves);
            }
        }

        setShelfForEveryBookInBookRepository();
    }

    private void populateAuthorRepository() {
        authorRepository.saveAll(generateAuthors());
    }

    private void populateTagRepository() {
        tagRepository.saveAll(generateListOfTags());
    }

    private List<PredefinedShelf> populateShelfRepository(User user) {
         return predefinedShelfRepository.saveAll(createPredefinedShelves(user));
    }

    private void populateBookRepository(List<PredefinedShelf> predefinedShelves) {
        bookRepository.saveAll(
                generateBooks(
                        authorRepository.findAll(),
                        tagRepository.findAll(),
                        predefinedShelves
                )
        );
    }

    private void setShelfForEveryBookInBookRepository() {
        List<PredefinedShelf> shelves = predefinedShelfRepository.findAll();
        List<Book> books = setPredefinedShelfForBooks(bookRepository.findAll(), shelves);
        bookRepository.saveAll(books);
    }


    private List<PredefinedShelf> createPredefinedShelves(User user) {
        return Stream.of(PredefinedShelf.ShelfName.values())
                .map(shelfName -> new PredefinedShelf(shelfName, user))
                .collect(Collectors.toList());
    }

}
