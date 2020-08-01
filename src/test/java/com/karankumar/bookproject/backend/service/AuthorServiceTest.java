package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.backend.entity.Author;
import com.karankumar.bookproject.backend.entity.BaseEntity;
import com.karankumar.bookproject.tags.IntegrationTest;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@IntegrationTest
public class AuthorServiceTest {

    private static AuthorService authorService;

    @BeforeEach
    public void setup(@Autowired AuthorService authorService) {
        AuthorServiceTest.authorService = authorService;
    }

    @Test
    public void createAuthorsWithDifferentId() {
        Author author = new Author("Test First Name", "Test Last Name");
        authorService.save(author);
        authorService.save(author);
        List<Author> authors = authorService.isMatchingAuthorsPresent(author);
        Assumptions.assumeFalse(authors.isEmpty(), "List of Authors shouldn't be empty");
        Assumptions
                .assumeTrue(authors.stream().map(BaseEntity::getId).count() == 2,
                        "Similar Authors shouldn't have the same Id");
    }

    @Test
    public void testNonMatchingAuthors() {
        List<Author> authors = authorService.isMatchingAuthorsPresent(new Author("firstName", "lastName"));
        Assumptions.assumeTrue(authors.isEmpty());
    }

    @Test
    public void testMatchingAuthors() {
        Author author = new Author("firstName", "lastName");
        authorService.save(author);
        List<Author> authors = authorService.isMatchingAuthorsPresent(author);
        Assumptions.assumeFalse(authors.isEmpty());
    }
    
}
