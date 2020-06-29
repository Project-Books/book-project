package com.karankumar.bookproject.backend.repository;


import com.karankumar.bookproject.backend.model.PredefinedShelf;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PredefinedShelfRepositoryTest {

    @Autowired
    private PredefinedShelfRepository repository;

    @BeforeEach
    void setUp() {
        repository.saveAll(
            Arrays.stream(PredefinedShelf.ShelfName.values())
                .map(shelfName -> {
                    return new PredefinedShelf(shelfName);
                }).collect(Collectors.toList()));
    }

    @Test
    void givenShelfExistsFindByShelfNameShouldReturnOneShelf() {
        List<PredefinedShelf> shelves =
            repository.findPredefinedShelfByShelfName(PredefinedShelf.ShelfName.TO_READ);

        Assertions.assertEquals(1, shelves.size());
        PredefinedShelf shelf = shelves.get(0);
        Assertions.assertNotNull(shelf);
        Assertions.assertAll(
            () -> Assertions.assertEquals(PredefinedShelf.ShelfName.TO_READ, shelf.getShelfName()),
            () -> Assertions.assertNull(shelf.getBooks())
        );


    }
}

