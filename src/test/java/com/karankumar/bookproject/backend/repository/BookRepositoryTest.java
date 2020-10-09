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
        author = authorRepository.saveAndFlush(new Author("firstName", "lastName"));
        readShelf = predefinedShelfRepository.saveAndFlush(new PredefinedShelf(PredefinedShelf.ShelfName.READ));
        customBook = new Book("someTitle", author, null);
        customShelf = new CustomShelf("MyShelf");
        customShelfRepository.saveAndFlush(customShelf);

        bookRepository.deleteAll();
        book1 = bookRepository.saveAndFlush(new Book("someTitle", author, readShelf));

        customBook = bookRepository.saveAndFlush(customBook);
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

}

