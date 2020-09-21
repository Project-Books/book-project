package com.karankumar.bookproject.backend.repository;

import com.karankumar.bookproject.annotations.DataJpaIntegrationTest;
import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaIntegrationTest
class BookRepositoryTest {

    @Autowired private PredefinedShelfRepository shelfRepository;
    @Autowired private AuthorRepository authorRepository;
    @Autowired private BookRepository bookRepository;

    PredefinedShelf predefinedShelf;

    @BeforeEach
    void setup() {
        predefinedShelf = shelfRepository.save(new PredefinedShelf(PredefinedShelf.ShelfName.READ));
        bookRepository.deleteAll();
    }

    @Test
    void shouldSuccessfullyDeleteABook_whenAuthorHasOtherBooks() {
        // given
        Author author = authorRepository.saveAndFlush(new Author("firstName", "lastName"));
        Book book1 = bookRepository.save(new Book("someTitle", author, predefinedShelf));
        Book book2 = bookRepository.save(new Book("someOtherTitle", author, predefinedShelf));

        // when
        bookRepository.delete(book1);

        // then
        assertThat(bookRepository.findAll().size()).isOne();
        assertThat(bookRepository.findAll().get(0))
                .isEqualToComparingFieldByField(book2);
    }

    @Test
    void shouldSuccessfullyDeleteABook_whenAuthorHasNoOtherBooks() {
        // given
        Author author = authorRepository.saveAndFlush(new Author("firstName", "lastName"));
        Book book = bookRepository.save(new Book("someTitle", author, predefinedShelf));

        // when
        bookRepository.delete(book);

        // then
        assertTrue(bookRepository.findAll().isEmpty());
    }
}
