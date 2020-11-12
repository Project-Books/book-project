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

package com.karankumar.bookproject.backend.entity;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import com.karankumar.bookproject.backend.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@IntegrationTest
@Transactional
@DisplayName("Book should")
class BookTest {
    private final BookService bookService;
    private final TagService tagService;
    private final PredefinedShelfService predefinedShelfService;

    private Book testBook;
    private Tag testTag;
    
    private Validator validator;
    private Set<ConstraintViolation<Book>> violations;

    @Autowired
    BookTest(BookService bookService, TagService tagService,
             PredefinedShelfService predefinedShelfService) {
        this.bookService = bookService;
        this.tagService = tagService;
        this.predefinedShelfService = predefinedShelfService;
    }

    private Book createBook(String title, PredefinedShelf shelf) {
        Author author = new Author("Firstname", "Lastname");
        Book book = new Book(title, author, shelf);

        book.setTags(Collections.singleton(testTag));

        return book;
    }

    @BeforeEach
    void setUp() {
        testTag = new Tag("Test Tag");
        tagService.deleteAll();
        tagService.save(testTag);

        testBook = createBook("Test Title", predefinedShelfService.findToReadShelf());
        bookService.deleteAll();
        bookService.save(testBook);
        
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @Transactional
    void notRemoveOrphanTags() {
        // given
        assumeThat(tagService.findAll().size()).isOne();

        // when
        bookService.delete(testBook);

        // then
        assertThat(tagService.findAll().size()).isOne();
    }

    @Test
    @DisplayName("correctly convert into an edition with the 'st' suffix")
    @Transactional
    void correctlyConvertEditionEndingIn1() {
        // given
        int firstEdition = 1;
        int twentyFirstEdition = 21;

        // when
        String actualFirst = Book.convertToBookEdition(firstEdition);
        String actualTwentyFirst = Book.convertToBookEdition(twentyFirstEdition);

        // then
        assertSoftly(softly -> {
            assertThat(actualFirst).isEqualTo("1st edition");
            assertThat(actualTwentyFirst).isEqualTo("21st edition");
        });
    }

    @Test
    @DisplayName("correctly convert into an edition with the 'nd' suffix")
    void correctlyConvertEditionEndingIn2() {
        // given
        int secondEdition = 2;
        int twentySecondEdition = 22;

        // when
        String actualSecondEdition = Book.convertToBookEdition(secondEdition);
        String actualTwentySecondEdition = Book.convertToBookEdition(twentySecondEdition);

        // then
        assertSoftly(softly -> {
            softly.assertThat(actualSecondEdition).isEqualTo("2nd edition");
            softly.assertThat(actualTwentySecondEdition).isEqualTo("22nd edition");
        });
    }

    @Test
    @DisplayName("correctly convert into an edition wth the 'rd' suffix")
    void correctlyConvertEditionEndingIn3() {
        // given
        int thirdEdition = 3;
        int twentyThirdEdition = 23;

        // when
        String actualThirdEdition = Book.convertToBookEdition(thirdEdition);
        String actualTwentyThirdEdition = Book.convertToBookEdition(twentyThirdEdition);

        // then
        assertSoftly(softly -> {
            softly.assertThat(actualThirdEdition).isEqualTo("3rd edition");
            softly.assertThat(actualTwentyThirdEdition).isEqualTo("23rd edition");
        });
    }

    @Test
    @DisplayName("correctly convert into an edition with the 'th' suffix")
    void correctlyConvertNthEdition() {
        // given
        int fourthEdition = 4;
        int eleventhEdition = 11;

        // when
        String actualFourthEdition = Book.convertToBookEdition(fourthEdition);
        String actualEleventhEdition = Book.convertToBookEdition(eleventhEdition);

        // then
        assertSoftly(softly -> {
            softly.assertThat(actualFourthEdition).isEqualTo("4th edition");
            softly.assertThat(actualEleventhEdition).isEqualTo("11th edition");
        });
    }
    
    @Test
    void notAcceptNullTitle() {
    	// when
    	Book bookWithNullTitle = createBook(null, predefinedShelfService.findToReadShelf());
    	
    	violations = validator.validateProperty(bookWithNullTitle, "title");
    	
    	// then
    	assertThat(violations.size()).isEqualTo(2);
    }
    
    @Test
    void notAcceptBlankTitle() {
    	// when
    	Book bookWithBlankTitle = createBook(" ", predefinedShelfService.findToReadShelf());
    	
    	violations = validator.validateProperty(bookWithBlankTitle, "title");
 
    	// then
    	assertThat(violations.size()).isOne();
    }
}
