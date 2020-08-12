package com.karankumar.bookproject.backend.repository;

import com.karankumar.bookproject.annotations.DataJpaIntegrationTest;
import com.karankumar.bookproject.backend.entity.CustomShelf;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DataJpaIntegrationTest
class CustomShelfRepositoryTest {
    @Autowired private CustomShelfRepository repository;
    private final String test1 = "Test1";

    @BeforeEach
    void setup() {
        repository.save(new CustomShelf(test1));
        repository.save(new CustomShelf("Test2"));
        repository.save(new CustomShelf("Test3"));
    }

    @Test
    void whenShelfExistsFindByShelfNameReturnsOneShelf() {
        List<CustomShelf> shelves = repository.findByShelfName(test1);
        Assertions.assertEquals(1, shelves.size());
    }
}

