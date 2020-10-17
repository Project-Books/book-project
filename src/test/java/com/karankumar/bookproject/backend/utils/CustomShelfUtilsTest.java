package com.karankumar.bookproject.backend.utils;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.CustomShelf;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.CustomShelfService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@IntegrationTest
@DisplayName("CustomShelfUtils should")
class CustomShelfUtilsTest {
    private final BookService bookService;
    private final CustomShelfService customShelfService;
    private final PredefinedShelfService predefinedShelfService;
    private final CustomShelfUtils customShelfUtils;

    private CustomShelf customShelf1;
    private CustomShelf customShelf2;
    private CustomShelf customShelfWithNoBooks;

    private Set<Book> booksInCustomShelf1;

    @Autowired
    CustomShelfUtilsTest(BookService bookService, CustomShelfService customShelfService, PredefinedShelfService predefinedShelfService) {
        this.bookService = bookService;
        this.customShelfService = customShelfService;
        this.predefinedShelfService = predefinedShelfService;
        this.customShelfUtils = new CustomShelfUtils(customShelfService);
    }

    @BeforeEach
    public void setUp() {
        resetServices();

        customShelf1 = customShelfService.createCustomShelf("CustomShelf1");
        customShelf2 = customShelfService.createCustomShelf("CustomShelf2");
        customShelfWithNoBooks = customShelfService.createCustomShelf("CustomShelf3");
        saveCustomShelves(customShelf1, customShelf2, customShelfWithNoBooks);

        addBooksToCustomShelves(predefinedShelfService.findToReadShelf());
    }

    private void resetServices() {
        bookService.deleteAll();
        customShelfService.deleteAll();
    }

    private HashSet<Book> createSetOfBooks(String title1, String title2,
                                                  PredefinedShelf predefinedShelf,
                                                  CustomShelf customShelf) {
        return new HashSet<>(List.of(
                createAndSaveBook(title1, predefinedShelf, customShelf),
                createAndSaveBook(title2, predefinedShelf, customShelf)
        ));
    }

    private Book createAndSaveBook(String title, PredefinedShelf predefinedShelf,
                                          CustomShelf customShelf) {
        Book book = new Book(title, new Author("John", "Doe"), predefinedShelf);
        book.setCustomShelf(customShelf);
        bookService.save(book);
        return book;
    }

    private void addBooksToCustomShelves(PredefinedShelf predefinedShelf) {
        booksInCustomShelf1
                = createSetOfBooks("Title1", "Title2", predefinedShelf, customShelf1);
        customShelf1.setBooks(booksInCustomShelf1);
        customShelf2.setBooks(
                createSetOfBooks("Title3", "Title4", predefinedShelf, customShelf2)
        );
    }

    private void saveCustomShelves(CustomShelf... customShelves) {
        for (CustomShelf c : customShelves) {
            customShelfService.save(c);
        }
    }

    @Test
    void returnBooksSuccessfully() {
        Set<Book> actual = customShelfUtils.getBooksInCustomShelf(customShelf1.getShelfName());
        booksInCustomShelf1.forEach(book -> assertThat(actual).contains(book));
    }

    @Test
    void returnNoBooksIfNoBooksInCustomShelf() {
        Set<Book> actual =
                customShelfUtils.getBooksInCustomShelf(customShelfWithNoBooks.getShelfName());
        assertThat(actual).isEmpty();
    }

    @Test
    void returnEmptySetOfBooksWhenCustomShelfIsInvalid() {
        assertThat(customShelfUtils.getBooksInCustomShelf("InvalidShelf")).isEmpty();
    }
}
