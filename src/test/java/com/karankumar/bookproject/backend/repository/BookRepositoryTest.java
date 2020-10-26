package com.karankumar.bookproject.backend.repository;

import com.karankumar.bookproject.annotations.DataJpaIntegrationTest;
import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.entity.account.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.karankumar.bookproject.util.SecurityTestUtils.getTestUser;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaIntegrationTest
@DisplayName("BookRepository should")
class BookRepositoryTest {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final UserRepository userRepository;
    private final PredefinedShelfRepository predefinedShelfRepository;
    private User user;

    private PredefinedShelf readShelf;
    private Author author;
    private Book book;

    @Autowired
    BookRepositoryTest(BookRepository bookRepository, AuthorRepository authorRepository,
                       UserRepository userRepository,
                       PredefinedShelfRepository predefinedShelfRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.userRepository = userRepository;
        this.predefinedShelfRepository = predefinedShelfRepository;
    }

    @BeforeEach
    void setup() {
        bookRepository.deleteAll();
        user = getTestUser(userRepository);
        author = authorRepository.save(new Author("firstName", "lastName"));
        Author author2 = authorRepository.saveAndFlush(new Author("author", "test"));
        readShelf = predefinedShelfRepository.save(new PredefinedShelf(PredefinedShelf.ShelfName.READ, user));
        book = bookRepository.save(new Book("someTitle", author, readShelf));
        PredefinedShelf toRead = predefinedShelfRepository.saveAndFlush(new PredefinedShelf(PredefinedShelf.ShelfName.TO_READ, user));
        bookRepository.save(new Book("title", author, readShelf));
        bookRepository.saveAndFlush(new Book("test", author2, toRead));
    }

    @Test
    void successfullyDeleteABook_whenAuthorHasOtherBooks() {
        // given
        Book book2 = bookRepository.save(new Book("someOtherTitle", author, readShelf));

        // when
        bookRepository.delete(book);

        // then
        assertThat(bookRepository.findAll().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("should successfully delete a book when the author has no other books")
    void successfullyDeleteSingleAuthoredBook() {
        // when
        bookRepository.delete(book);

        // then
        assertThat(bookRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("should successfully find list of books by their title")
    void findBookByTitle() {
        assertThat(bookRepository.findByTitleContainingIgnoreCase("someTitle").size()).isOne();
        assertThat(bookRepository.findByTitleContainingIgnoreCase("some").size()).isOne();
        assertThat(bookRepository.findByTitleContainingIgnoreCase("title").size()).isEqualTo(2);
    }

    @Test
    @DisplayName("should successfully find list of books by predefined shelf, title or author")
    void findBookByPredefinedShelfAndTitleOrAuthor() {
        PredefinedShelf readShelf = predefinedShelfRepository.findByPredefinedShelfNameAndUser(PredefinedShelf.ShelfName.READ, user);
        PredefinedShelf toReadShelf = predefinedShelfRepository.findByPredefinedShelfNameAndUser(PredefinedShelf.ShelfName.TO_READ, user);

        assertThat(bookRepository.findByShelfAndTitleOrAuthor(readShelf,"%", "%").size()).isEqualTo(2);
        assertThat(bookRepository.findByShelfAndTitleOrAuthor(toReadShelf, "%", "%").size()).isOne();
        assertThat(bookRepository.findByShelfAndTitleOrAuthor(readShelf, "some", "%").size()).isOne();
        assertThat(bookRepository.findByShelfAndTitleOrAuthor(readShelf, "title", "%").size()).isEqualTo(2);
        assertThat(bookRepository.findByShelfAndTitleOrAuthor(toReadShelf, "title", "%").size()).isZero();
    }

    @Test
    @DisplayName("should successfully find list of books by title or author")
    void findBookByTitleOrAuthor() {

        assertThat(bookRepository.findByTitleOrAuthor("%", "%").size()).isEqualTo(3);
        assertThat(bookRepository.findByTitleOrAuthor("title", "%").size()).isEqualTo(2);
        assertThat(bookRepository.findByTitleOrAuthor("%", "firstName").size()).isEqualTo(2);
        assertThat(bookRepository.findByTitleOrAuthor("%", "lastName").size()).isEqualTo(2);
        assertThat(bookRepository.findByTitleOrAuthor("%", "author").size()).isOne();
        assertThat(bookRepository.findByTitleOrAuthor("title", "firstName").size()).isEqualTo(2);
        assertThat(bookRepository.findByTitleOrAuthor("te", "au").size()).isOne();
    }

}

