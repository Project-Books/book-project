package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.CustomShelf;
import com.karankumar.bookproject.backend.entity.account.User;
import com.karankumar.bookproject.backend.repository.CustomShelfRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.karankumar.bookproject.utils.SecurityTestUtils.TEST_USER_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@IntegrationTest
@Transactional
@DisplayName("CustomShelfService should")
class CustomShelfServiceTest {
    @Autowired private CustomShelfService customShelfService;
    @Autowired private CustomShelfRepository customShelfRepository;

    private static final List<String> SHELF_NAMES = List.of("CustomShelf1", "CustomShelf2", "CustomShelf3");

    @BeforeEach
    void setup() {
        customShelfRepository.deleteAll();
        customShelfRepository.saveAll(
                SHELF_NAMES.stream()
                        .map(customShelfService::createCustomShelf)
                        .collect(Collectors.toList())
        );

    }

    @Test
    void findAllCustomShelvesForLoggedInUser() {
        List<CustomShelf> shelves = customShelfService.findAllForLoggedInUser();
        assertThat(shelves).isNotNull().hasSameSizeAs(SHELF_NAMES);

        assertSoftly(softly -> {
            softly.assertThat(shelves)
                    .extracting(CustomShelf::getShelfName)
                    .containsExactlyInAnyOrderElementsOf(SHELF_NAMES);
            softly.assertThat(shelves)
                    .extracting(CustomShelf::getUser)
                    .extracting(User::getUsername)
                    .allMatch(username -> username.equals(TEST_USER_NAME));
        });
    }

    @Test
    void findAllPredefinedShelvesForPredefinedShelfNameAndLoggedInUser() {
        String name = SHELF_NAMES.get(0);
        CustomShelf shelf = customShelfService.findByShelfNameAndLoggedInUser(name);
        assertThat(shelf).isNotNull();

        assertSoftly(softly -> {
            softly.assertThat(shelf.getShelfName()).isEqualTo(name);
            softly.assertThat(shelf.getUser().getUsername()).isEqualTo(TEST_USER_NAME);
        });
    }
}
