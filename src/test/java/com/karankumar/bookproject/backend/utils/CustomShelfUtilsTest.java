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
    private static final CustomShelf customShelf3 = new CustomShelf("CustomShelf3");

    private static BookService bookService;
    private static CustomShelfService customShelfService;

    private static Set<Book> booksInCustomShelf1;
    private static Set<Book> booksInCustomShelf2;
    private static Set<Book> booksInCustomShelf3;

    private static CustomShelfUtils customShelfUtils;

    @BeforeAll
    public static void setUp(@Autowired PredefinedShelfService predefinedShelfService,
                             @Autowired CustomShelfService customShelfService,
                             @Autowired BookService bookService) {
        PredefinedShelfUtils predefinedShelfUtils = new PredefinedShelfUtils(predefinedShelfService);
        PredefinedShelf toRead = predefinedShelfUtils.findToReadShelf();

        CustomShelfUtilsTest.bookService = bookService;
        CustomShelfUtilsTest.customShelfService = customShelfService;
        resetServices();
        customShelfUtils = new CustomShelfUtils(customShelfService);

        saveCustomShelves(customShelf1, customShelf2, customShelf3);

        booksInCustomShelf1 = createSetOfBooks("Title1", "Title2", toRead, customShelf1);
        booksInCustomShelf2 = createSetOfBooks("Title3", "Title4", toRead, customShelf2);
        booksInCustomShelf3 = createSetOfBooks("Title5", "Title5", toRead, customShelf3);

        addBooksToCustomShelves();
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

    private static void addBooksToCustomShelves() {
        customShelf1.setBooks(booksInCustomShelf1);
        customShelf2.setBooks(booksInCustomShelf2);
        customShelf3.setBooks(booksInCustomShelf3);
    }

    private static void saveCustomShelves(CustomShelf... customShelves) {
        for (CustomShelf c : customShelves) {
            customShelfService.save(c);
        }
    }

    @Test
    void testGetBooksInCustomShelfSuccessfullyReturnsBooks() {
        Set<Book> actual = customShelfUtils.getBooksInCustomShelf(customShelf1.getShelfName());
        booksInCustomShelf1.forEach(book -> assertThat(actual.contains(book)));
    }
}
