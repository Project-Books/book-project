package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.PredefinedShelf;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@DisplayName("PredefinedShelfService should")
class PredefinedShelfServiceTest {
    private static PredefinedShelfService predefinedShelfService;

    @BeforeAll
    public static void setup(@Autowired PredefinedShelfService predefinedShelfService) {
        PredefinedShelfServiceTest.predefinedShelfService = predefinedShelfService;
    }

    @Test
    void notDeleteAPredefinedShelf() {
        // given
        PredefinedShelf read = predefinedShelfService.findReadShelf();

        // when
        predefinedShelfService.delete(read);

        // then
        assertThat(predefinedShelfService.findAll()).contains(read);
    }

    @Test
    void notDeleteAllPredefinedShelves() {
        // given
        int expected = predefinedShelfService.findAll().size();

        // when
        predefinedShelfService.deleteAll();

        // then
        assertThat(predefinedShelfService.findAll()).hasSize(expected);
    }
}
