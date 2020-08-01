package com.karankumar.bookproject.backend.repository;


import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import com.karankumar.bookproject.tags.DataJpaIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@DataJpaIntegrationTest
class PredefinedShelfRepositoryTest {

    @Autowired
    private PredefinedShelfRepository repository;

    @BeforeEach
    void setUp() {
        repository.saveAll(
            Arrays.stream(PredefinedShelf.ShelfName.values())
                  .map(PredefinedShelf::new)
                  .collect(Collectors.toList())
        );
    }

    @Test
    void givenShelfExistsFindByShelfNameShouldReturnOneShelf() {
        List<PredefinedShelf> shelves =
//                repository.findPredefinedShelfByShelfName(PredefinedShelf.ShelfName.TO_READ);
            repository.findByPredefinedShelfName(PredefinedShelf.ShelfName.TO_READ);

        Assertions.assertEquals(1, shelves.size());
        PredefinedShelf shelf = shelves.get(0);
        Assertions.assertNotNull(shelf);
        Assertions.assertAll(
            () -> Assertions.assertEquals(PredefinedShelf.ShelfName.TO_READ, shelf.getPredefinedShelfName()),
            () -> Assertions.assertNull(shelf.getBooks())
        );
    }
}

