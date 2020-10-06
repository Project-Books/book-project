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
    private final PredefinedShelfService predefinedShelfService;

    @Autowired
    PredefinedShelfServiceTest(PredefinedShelfService predefinedShelfService) {
        this.predefinedShelfService = predefinedShelfService;
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
        Long expected = predefinedShelfService.count();

        // when
        predefinedShelfService.deleteAll();

        // then
        assertThat(predefinedShelfService.count()).isEqualTo(expected);
    }
}
