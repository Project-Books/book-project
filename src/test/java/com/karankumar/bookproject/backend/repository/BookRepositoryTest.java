package com.karankumar.bookproject.backend.repository;

import com.karankumar.bookproject.annotations.DataJpaIntegrationTest;
import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
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

    private PredefinedShelf readShelf;
    private Author author;
    private Book book1;

    BookRepositoryTest() {
//        author = authorRepository.saveAndFlush(new Author("firstName", "lastName"));
//        readShelf =
//                predefinedShelfRepository.save(new PredefinedShelf(PredefinedShelf.ShelfName.READ));
    }

    @BeforeEach
    void setup() {
        resetBookService();
    }

    private void resetBookService() {
        author = authorRepository.saveAndFlush(new Author("firstName", "lastName"));
        readShelf = predefinedShelfRepository.save(new PredefinedShelf(PredefinedShelf.ShelfName.READ));

        bookRepository.deleteAll();
        book1 = bookRepository.saveAndFlush(new Book("someTitle", author, readShelf));
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
    @DisplayName("should successfully find list of books by title or author filter")
    void findBookByAuthor(){
        assertThat(bookRepository.findByTitleOrAuthor("someTitle", author).size()).isOne();
        assertThat(bookRepository.findByTitleOrAuthor("", author).size()).isOne();
        assertThat(bookRepository.findByTitleOrAuthor("someTitle", null).size()).isOne();
        assertThat(bookRepository.findByTitleOrAuthor("", null).size()).isZero();
        assertThat(bookRepository.findByTitleOrAuthor(null, null).size()).isZero();
    }

}

