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
import com.karankumar.bookproject.backend.repository.AuthorRepository;
import com.karankumar.bookproject.backend.repository.BookRepository;
import com.karankumar.bookproject.backend.repository.PredefinedShelfRepository;
import com.karankumar.bookproject.backend.repository.TagRepository;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.logging.Level;

import static com.karankumar.bookproject.backend.utils.TestData.createPredefinedShelves;
import static com.karankumar.bookproject.backend.utils.TestData.generateAuthors;
import static com.karankumar.bookproject.backend.utils.TestData.generateBooks;
import static com.karankumar.bookproject.backend.utils.TestData.generateListOfTags;
import static com.karankumar.bookproject.backend.utils.TestData.setPredefinedShelfForBooks;

@Service
@Log
public class PredefinedShelfService extends BaseService<PredefinedShelf, Long> {

    private final BookRepository bookRepository;
    private final PredefinedShelfRepository predefinedShelfRepository;
    private final AuthorRepository authorRepository;
    private final TagRepository tagRepository;

    public PredefinedShelfService(BookRepository bookRepository, AuthorRepository authorRepository,
            PredefinedShelfRepository shelfRepository, TagRepository tagRepository) {
        this.bookRepository = bookRepository;
        this.predefinedShelfRepository = shelfRepository;

        this.authorRepository = authorRepository;
        this.tagRepository = tagRepository;
    }

    @Override
    public PredefinedShelf findById(Long id) {
        return predefinedShelfRepository.getOne(id);
    }

    @Override
    public void save(PredefinedShelf shelf) {
        if (shelf != null) {
            LOGGER.log(Level.INFO, "Saving shelf: " + shelf);
            predefinedShelfRepository.save(shelf);
        } else {
            LOGGER.log(Level.SEVERE, "Null Shelf");
        }
    }

    public List<PredefinedShelf> findAll() {
        return predefinedShelfRepository.findAll();
    }

    public List<PredefinedShelf> findAll(PredefinedShelf.ShelfName shelfName) {
        if (shelfName == null) {
            return predefinedShelfRepository.findAll();
        } else {
            return predefinedShelfRepository.findByPredefinedShelfName(shelfName);
        }
    }

    @Override
    public void delete(PredefinedShelf shelf) {
        // Don't want to delete a predefined shelf
        LOGGER.severe("delete() should not be called");
    }

    @Override
    public void deleteAll() {
        // Don't want to delete the predefined shelves
        LOGGER.severe("deleteAll() should not be called");
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

        if (predefinedShelfRepository.count() == 0) {
            populateShelfRepository();
        }

        if (bookRepository.count() == 0) {
            populateBookRepository();
        }

        setShelfForEveryBookInBookRepository();
    }

    private void populateAuthorRepository() {
        authorRepository.saveAll(generateAuthors());
    }

    private void populateTagRepository() {
        tagRepository.saveAll(generateListOfTags());
    }

    private void populateShelfRepository() {
        predefinedShelfRepository.saveAll(createPredefinedShelves(bookRepository.findAll()));
    }

    private void populateBookRepository() {
        bookRepository.saveAll(
                generateBooks(
                        authorRepository.findAll(), tagRepository.findAll(),
                        predefinedShelfRepository.findAll()
                )
        );
    }

    private void setShelfForEveryBookInBookRepository() {
        List<PredefinedShelf> shelves = predefinedShelfRepository.findAll();
        List<Book> books = setPredefinedShelfForBooks(bookRepository.findAll(), shelves);
        bookRepository.saveAll(books);
    }
}
