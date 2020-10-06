package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.annotations.IntegrationTest;
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
    void notSaveNullPredefinedShelf() {
        // given
        Long initialCount = predefinedShelfService.count();

        // when
        predefinedShelfService.save(null);

        // then
        assertThat(predefinedShelfService.count()).isEqualTo(initialCount);
    }
}
