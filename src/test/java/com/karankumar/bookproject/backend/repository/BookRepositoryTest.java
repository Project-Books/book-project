package com.karankumar.bookproject.backend.repository;

import com.karankumar.bookproject.annotations.DataJpaIntegrationTest;
import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.account.User;
import com.karankumar.bookproject.utils.SecurityTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaIntegrationTest
class BookRepositoryTest {
    @Autowired private BookRepository bookRepository;

    private final PredefinedShelf readShelf;
    private final Author author;
    private Book book1;

    BookRepositoryTest(@Autowired AuthorRepository authorRepository,
                       @Autowired UserRepository userRepository,
                       @Autowired PredefinedShelfRepository predefinedShelfRepository) {
        User user = SecurityTestUtils.getTestUser(userRepository);
        author = authorRepository.saveAndFlush(new Author("firstName", "lastName"));
        readShelf =
                predefinedShelfRepository.save(new PredefinedShelf(PredefinedShelf.ShelfName.READ, user));
    }

    @BeforeEach
    void setup() {
        resetBookService();
    }

    private void resetBookService() {
        bookRepository.deleteAll();
        book1 = bookRepository.save(new Book("someTitle", author, readShelf));
    }

    @Test
    void successfullyDeleteABook_whenAuthorHasOtherBooks() {
        // given
        Book book2 = bookRepository.save(new Book("someOtherTitle", author, readShelf));

        // when
        bookRepository.delete(book1);

        // then
        assertThat(bookRepository.findAll().size()).isOne();
        assertThat(bookRepository.findAll().get(0))
                .isEqualToComparingFieldByField(book2);
    }

    @Test
    @DisplayName("should successfully delete a book when the author has no other books")
    void successfullyDeleteSingleAuthoredBook() {
        // when
        bookRepository.delete(book1);

        // then
        assertThat(bookRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("should successfully find list of books by their title")
    void findBookByTitle() {
        assertThat(bookRepository.findByTitleContainingIgnoreCase("someTitle").size()).isOne();
        assertThat(bookRepository.findByTitleContainingIgnoreCase("some").size()).isOne();
        assertThat(bookRepository.findByTitleContainingIgnoreCase("title").size()).isOne();
    }
}
