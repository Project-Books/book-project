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

import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.Shelf;
import com.karankumar.bookproject.backend.entity.account.User;
import com.karankumar.bookproject.backend.repository.AuthorRepository;
import com.karankumar.bookproject.backend.repository.BookRepository;
import com.karankumar.bookproject.backend.repository.PredefinedShelfRepository;
import com.karankumar.bookproject.backend.repository.TagRepository;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.karankumar.bookproject.backend.util.PredefinedShelfUtils.getBooksInPredefinedShelves;
import static com.karankumar.bookproject.backend.util.PredefinedShelfUtils.getPredefinedShelfName;
import static com.karankumar.bookproject.backend.util.ShelfUtils.isAllBooksShelf;
import static com.karankumar.bookproject.backend.util.TestData.generateAuthors;
import static com.karankumar.bookproject.backend.util.TestData.generateBooks;
import static com.karankumar.bookproject.backend.util.TestData.generateListOfTags;
import static com.karankumar.bookproject.backend.util.TestData.setPredefinedShelfForBooks;

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

    public void save(@NonNull PredefinedShelf shelf) {
        predefinedShelfRepository.save(shelf);
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

    public List<String> getPredefinedShelfNamesAsStrings() {
        return findAllForLoggedInUser().stream()
                .map(Shelf::getShelfName)
                .collect(Collectors.toList());
    }

    public PredefinedShelf getPredefinedShelfByNameAsString(String shelfName) {
        return findAllForLoggedInUser()
                .stream()
                .filter(shelf ->
                        shelf.getShelfName().toString().equals(shelfName))
                .collect(Collectors.toList())
                .get(0); // there should only be one
    }

	/**
	 * Fetches all of the books in the chosen predefined shelf
	 */
	public Set<Book> getBooksInChosenPredefinedShelf(String chosenShelf) {
		Set<Book> books;
		if (isAllBooksShelf(chosenShelf)) {
			return getBooksInAllPredefinedShelves();
		}

		PredefinedShelf.ShelfName predefinedShelfName = getPredefinedShelfName(chosenShelf);
		PredefinedShelf predefinedShelf =
				findByPredefinedShelfNameAndLoggedInUser(predefinedShelfName);
		if (predefinedShelf == null) {
			books = new HashSet<>();
		} else {
			books = predefinedShelf.getBooks();
		}
		return books;
	}

    public Set<Book> getBooksInAllPredefinedShelves() {
        return getBooksInPredefinedShelves(findAllForLoggedInUser());
    }
}
