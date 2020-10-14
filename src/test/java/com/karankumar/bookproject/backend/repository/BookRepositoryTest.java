package com.karankumar.bookproject.backend.repository;

import com.karankumar.bookproject.annotations.DataJpaIntegrationTest;
import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.CustomShelf;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaIntegrationTest
class BookRepositoryTest {
    @Autowired private BookRepository bookRepository;
    @Autowired AuthorRepository authorRepository;
    @Autowired PredefinedShelfRepository predefinedShelfRepository;
    @Autowired CustomShelfRepository customShelfRepository;

    private PredefinedShelf readShelf;
    private CustomShelf customShelf;
    private Author author;
    private Book book1;
    private Book customBook;

    @BeforeEach
    void setup() {
        resetBookService();
    }

    private void resetBookService() {
        author = authorRepository.save(new Author("firstName", "lastName"));
        Author author2 = authorRepository.saveAndFlush(new Author("author", "test"));
        readShelf = predefinedShelfRepository.saveAndFlush(new PredefinedShelf(PredefinedShelf.ShelfName.READ));

        bookRepository.deleteAll();
        book1 = bookRepository.save(new Book("someTitle", author, readShelf));
        bookRepository.save(new Book("title", author, readShelf));
        bookRepository.saveAndFlush(new Book("test", author2, readShelf));

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

    @Test
    @DisplayName("should successfully find list of books by predefined shelf, title or author")
    void findBookByPredefinedShelfAndTitleOrAuthor(){
        PredefinedShelf shelf = predefinedShelfRepository.findByPredefinedShelfName(PredefinedShelf.ShelfName.READ).get(0);
        String title = "someTitle";
        String authorsName = "firstName lastName";
        String wildcard = "%"; //considering empty string from UI a wildcard for any result

        assertThat(bookRepository.findByShelfAndTitleOrAuthor(shelf,title, authorsName).size()).isOne();
        assertThat(bookRepository.findByShelfAndTitleOrAuthor(shelf, wildcard, authorsName).size()).isOne();
        assertThat(bookRepository.findByShelfAndTitleOrAuthor(shelf, wildcard, wildcard).size()).isOne();
        assertThat(bookRepository.findByShelfAndTitleOrAuthor(null, wildcard, wildcard).size()).isZero();
    }

    @Test
    @DisplayName("should successfully find list of books by title or author")
    void findBookByTitleOrAuthor(){

        assertThat(bookRepository.findByTitleOrAuthor("%", "%").size()).isEqualTo(3);
        assertThat(bookRepository.findByTitleOrAuthor("title", "%").size()).isEqualTo(2);
        assertThat(bookRepository.findByTitleOrAuthor("%", "firstName").size()).isEqualTo(2);
        assertThat(bookRepository.findByTitleOrAuthor("%", "lastName").size()).isEqualTo(2);
        assertThat(bookRepository.findByTitleOrAuthor("%", "author").size()).isOne();
        assertThat(bookRepository.findByTitleOrAuthor("title", "firstName").size()).isEqualTo(2);
        assertThat(bookRepository.findByTitleOrAuthor("te", "au").size()).isOne();
    }

}

