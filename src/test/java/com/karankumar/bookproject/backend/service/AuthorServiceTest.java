package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.Author;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Log
@IntegrationTest
public class AuthorServiceTest {

    private final AuthorService sut;

    public AuthorServiceTest(@Autowired AuthorService sut, @Autowired BookService bookService) {
        bookService.deleteAll();
        sut.deleteAll();
        this.sut = sut;
    }

    @Test
    public void testSaveAndConfirmDuplicateNameWithDifferentId() {

        //given
        sut.save(null); // then it will skip the saving
        Author author = new Author("Nyor", "Ja");
        sut.save(author);

        Author author1 = author;
        sut.save(author1);

        Author author2 = new Author("Papa", "Cologne");
        sut.save(author2);
        sut.save(author2);

        // when
        List<Author> savedAuthors = sut.findAll();


        // then
        LOGGER.log(Level.INFO, "savedAuthors" + savedAuthors.size());

        // it should save 2 authors
        assertEquals(4, savedAuthors.size());
    }
}
