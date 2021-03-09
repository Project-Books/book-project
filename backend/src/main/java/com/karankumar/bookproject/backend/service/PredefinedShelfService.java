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

import com.karankumar.bookproject.backend.model.Book;
import com.karankumar.bookproject.backend.model.PredefinedShelf;
import com.karankumar.bookproject.backend.model.PredefinedShelf.ShelfName;
import com.karankumar.bookproject.backend.model.Shelf;
import com.karankumar.bookproject.backend.model.account.User;
import com.karankumar.bookproject.backend.repository.AuthorRepository;
import com.karankumar.bookproject.backend.repository.BookRepository;
import com.karankumar.bookproject.backend.repository.PredefinedShelfRepository;
import com.karankumar.bookproject.backend.repository.PublisherRepository;
import com.karankumar.bookproject.backend.repository.TagRepository;
import lombok.NonNull;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.karankumar.bookproject.backend.model.PredefinedShelf.ShelfName.DID_NOT_FINISH;
import static com.karankumar.bookproject.backend.model.PredefinedShelf.ShelfName.READ;
import static com.karankumar.bookproject.backend.model.PredefinedShelf.ShelfName.READING;
import static com.karankumar.bookproject.backend.model.PredefinedShelf.ShelfName.TO_READ;
import static com.karankumar.bookproject.backend.util.PredefinedShelfUtils.getBooksInPredefinedShelves;
import static com.karankumar.bookproject.backend.util.PredefinedShelfUtils.getPredefinedShelfName;
import static com.karankumar.bookproject.backend.util.ShelfUtils.isAllBooksShelf;
import static com.karankumar.bookproject.backend.util.TestData.generateAuthors;
import static com.karankumar.bookproject.backend.util.TestData.generateBooks;
import static com.karankumar.bookproject.backend.util.TestData.generateListOfTags;
import static com.karankumar.bookproject.backend.util.TestData.setPredefinedShelfForBooks;
import static com.karankumar.bookproject.backend.util.TestData.generatePublishers;

@Service
@Log
public class PredefinedShelfService {

    private final BookRepository bookRepository;
    private final PredefinedShelfRepository predefinedShelfRepository;
    private final AuthorRepository authorRepository;
    private final TagRepository tagRepository;
    private final UserService userService;
    private final PublisherRepository publisherRepository;

    public PredefinedShelfService(BookRepository bookRepository, AuthorRepository authorRepository,
                                  PredefinedShelfRepository shelfRepository,
                                  TagRepository tagRepository,
                                  UserService userService,
                                  PublisherRepository publisherRepository) {
        this.bookRepository = bookRepository;
        this.predefinedShelfRepository = shelfRepository;

        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
        this.userService = userService;
        this.publisherRepository=publisherRepository;
    }

    public Optional<PredefinedShelf> findById(Long id) {
        return predefinedShelfRepository.findById(id);
    }

    public void save(@NonNull PredefinedShelf shelf) {
        predefinedShelfRepository.save(shelf);
    }

    public List<PredefinedShelf> findAllForLoggedInUser() {
        return predefinedShelfRepository.findAllByUser(userService.getCurrentUser());
    }

    public PredefinedShelf findToReadShelf() {
        return findByPredefinedShelfNameAndLoggedInUser(TO_READ).orElse(null);
    }

    public PredefinedShelf findReadingShelf() {
        return findByPredefinedShelfNameAndLoggedInUser(READING).orElse(null);
    }

    public PredefinedShelf findReadShelf() {
        return findByPredefinedShelfNameAndLoggedInUser(READ).orElse(null);
    }

    public PredefinedShelf findDidNotFinishShelf() {
        return findByPredefinedShelfNameAndLoggedInUser(DID_NOT_FINISH).orElse(null);
    }

    public Optional<PredefinedShelf> findByPredefinedShelfNameAndLoggedInUser(PredefinedShelf.ShelfName shelfName) {
        return predefinedShelfRepository.findByPredefinedShelfNameAndUser(
                shelfName,
                userService.getCurrentUser()
        );
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

        if (publisherRepository.count() == 0){
            populatePublisherRepository();
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

    private void populatePublisherRepository () {
        publisherRepository.saveAll(generatePublishers());
    }

    private List<PredefinedShelf> populateShelfRepository(User user) {
         return predefinedShelfRepository.saveAll(createPredefinedShelves(user));
    }

    private void populateBookRepository(List<PredefinedShelf> predefinedShelves) {
        bookRepository.saveAll(
                generateBooks(
                        authorRepository.findAll(),
                        tagRepository.findAll(),
                        predefinedShelves,
                        publisherRepository.findAll()
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

    public Optional<PredefinedShelf> getPredefinedShelfByNameAsString(String shelfName) {
    	List<PredefinedShelf> shelfFound = findAllForLoggedInUser()
                .stream()
                .filter(shelf -> shelf.getShelfName().equals(shelfName))
                .collect(Collectors.toList());

        if (shelfFound.isEmpty()) {
        	return Optional.empty();
        }

        return Optional.of(shelfFound.get(0)); // there should only be one
    }

	/**
	 * Fetches all of the books in the chosen predefined shelf or all of the predefined shelves
	 */
	public Set<Book> getBooksInChosenPredefinedShelf(String chosenShelf) {
		if (isAllBooksShelf(chosenShelf)) {
			return getBooksInAllPredefinedShelves();
		}

		Optional<ShelfName> predefinedShelfName = getPredefinedShelfName(chosenShelf);
        if (predefinedShelfName.isEmpty()) {
        	return new HashSet<>();
        }

    	Optional<PredefinedShelf> predefinedShelf =
    			findByPredefinedShelfNameAndLoggedInUser(predefinedShelfName.get());
    	if (predefinedShelf.isEmpty()) {
    		return new HashSet<>();
    	}

    	return predefinedShelf.get().getBooks();
    }

    public Set<Book> getBooksInAllPredefinedShelves() {
        return getBooksInPredefinedShelves(findAllForLoggedInUser());
    }
}
