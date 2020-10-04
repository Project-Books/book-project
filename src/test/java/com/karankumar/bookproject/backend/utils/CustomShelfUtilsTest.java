package com.karankumar.bookproject.backend.utils;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.CustomShelf;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.backend.service.BookService;
import com.karankumar.bookproject.backend.service.CustomShelfService;
import com.karankumar.bookproject.backend.service.PredefinedShelfService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class CustomShelfUtilsTest {
    private static final CustomShelf customShelf1 = new CustomShelf("CustomShelf1");
    private static final CustomShelf customShelf2 = new CustomShelf("CustomShelf2");
    private static final CustomShelf customShelfWithNoBooks = new CustomShelf("CustomShelf3");

    private static BookService bookService;
    private static CustomShelfService customShelfService;
    private static CustomShelfUtils customShelfUtils;

    private static Set<Book> booksInCustomShelf1;

    @BeforeAll
    public static void setUp(@Autowired PredefinedShelfService predefinedShelfService,
                             @Autowired CustomShelfService customShelfService,
                             @Autowired BookService bookService) {
        PredefinedShelf toRead = predefinedShelfService.findByPredefinedShelfName(PredefinedShelf.ShelfName.TO_READ);

        CustomShelfUtilsTest.bookService = bookService;
        CustomShelfUtilsTest.customShelfService = customShelfService;
        resetServices();
        customShelfUtils = new CustomShelfUtils(customShelfService);

        saveCustomShelves(customShelf1, customShelf2, customShelfWithNoBooks);
        addBooksToCustomShelves(toRead);
    }

    private static void resetServices() {
        bookService.deleteAll();
        customShelfService.deleteAll();
    }

    private static HashSet<Book> createSetOfBooks(String title1, String title2,
                                                  PredefinedShelf predefinedShelf,
                                                  CustomShelf customShelf) {
        return new HashSet<>(List.of(
                createAndSaveBook(title1, predefinedShelf, customShelf),
                createAndSaveBook(title2, predefinedShelf, customShelf)
        ));
    }

    private static Book createAndSaveBook(String title, PredefinedShelf predefinedShelf,
                                          CustomShelf customShelf) {
        Book book = new Book(title, new Author("John", "Doe"), predefinedShelf);
        book.setCustomShelf(customShelf);
        bookService.save(book);
        return book;
    }

    private static void addBooksToCustomShelves(PredefinedShelf predefinedShelf) {
        booksInCustomShelf1
                = createSetOfBooks("Title1", "Title2", predefinedShelf, customShelf1);
        customShelf1.setBooks(booksInCustomShelf1);
        customShelf2.setBooks(
                createSetOfBooks("Title3", "Title4", predefinedShelf, customShelf2)
        );
    }

    private static void saveCustomShelves(CustomShelf... customShelves) {
        for (CustomShelf c : customShelves) {
            customShelfService.save(c);
        }
    }

    @Test
    void getBooksInCustomShelfSuccessfullyReturnsBooks() {
        Set<Book> actual = customShelfUtils.getBooksInCustomShelf(customShelf1.getShelfName());
        booksInCustomShelf1.forEach(book -> assertThat(actual.contains(book)));
    }

    @Test
    void givenNoBooksInCustomShelf_getBooksInCustomShelfReturnsNoBooks() {
        Set<Book> actual =
                customShelfUtils.getBooksInCustomShelf(customShelfWithNoBooks.getShelfName());
        assertThat(actual).isEmpty();
    }

    @Test
    void givenInvalidCustomShelf_anEmptySetOfBooksAreReturned() {
        assertThat(customShelfUtils.getBooksInCustomShelf("InvalidShelf")).isEmpty();
    }
}
