package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.Book;
import com.karankumar.bookproject.backend.entity.CustomShelf;
import com.karankumar.bookproject.backend.entity.account.User;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
@DisplayName("JsonImportServiceTest should")
class JsonImportServiceTest {

    @Autowired
    private JsonImportService importService;

    @Autowired
    private BookService bookService;

    @Autowired
    private CustomShelfService customShelfService;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        bookService.deleteAll();
        customShelfService.deleteAll();
    }


    @Test
    void importBooksAndShelvesCorrectly() throws IOException {
        // given
        String jsonString = FileUtils.readFileToString(
                new File("src/test/resources/booksToImportSample.json"), "UTF8");
        // when
        importService.importJson(jsonString);

        // then
        assertThat(bookService.count()).isEqualTo(2);
        assertThat(customShelfService.count()).isEqualTo(1);
    }

}