package com.karankumar.bookproject.backend.repository;

import com.karankumar.bookproject.annotations.DataJpaIntegrationTest;
import com.karankumar.bookproject.backend.entity.*;
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
        readShelf = predefinedShelfRepository.save(new PredefinedShelf(PredefinedShelf.ShelfName.READ));
        PredefinedShelf toRead = predefinedShelfRepository.saveAndFlush(new PredefinedShelf(PredefinedShelf.ShelfName.TO_READ));

        bookRepository.deleteAll();
        book1 = bookRepository.save(new Book("someTitle", author, readShelf));
        bookRepository.save(new Book("title", author, readShelf));
        bookRepository.saveAndFlush(new Book("test", author2, toRead));

    }

    @Test
    void successfullyDeleteABook_whenAuthorHasOtherBooks() {
        // given
        Book book2 = bookRepository.save(new Book("someOtherTitle", author, readShelf));

        // when
        bookRepository.delete(book1);

        // then
        assertThat(bookRepository.findAll().size()).isEqualTo(3);

    }

    @Test
    @DisplayName("should successfully delete a book when the author has no other books")
    void successfullyDeleteSingleAuthoredBook() {
        // when
        bookRepository.delete(book1);

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
    void findBookByPredefinedShelfAndTitleOrAuthor(){
        PredefinedShelf readShelf = predefinedShelfRepository.findByPredefinedShelfName(PredefinedShelf.ShelfName.READ);
        PredefinedShelf toReadShelf = predefinedShelfRepository.findByPredefinedShelfName(PredefinedShelf.ShelfName.TO_READ);

        assertThat(bookRepository.findByShelfAndTitleOrAuthor(readShelf,"%", "%").size()).isEqualTo(2);
        assertThat(bookRepository.findByShelfAndTitleOrAuthor(toReadShelf, "%", "%").size()).isOne();
        assertThat(bookRepository.findByShelfAndTitleOrAuthor(readShelf, "some", "%").size()).isOne();
        assertThat(bookRepository.findByShelfAndTitleOrAuthor(readShelf, "title", "%").size()).isEqualTo(2);
        assertThat(bookRepository.findByShelfAndTitleOrAuthor(toReadShelf, "title", "%").size()).isZero();
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

