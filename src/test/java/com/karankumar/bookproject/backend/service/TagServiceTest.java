package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.annotations.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@DisplayName("TagService should")
class TagServiceTest {
    private final TagService tagService;

    TagServiceTest(@Autowired TagService tagService) {
        this.tagService = tagService;
    }

    @Test
    void notSaveANullTag() {
        // given
        int initialSize = tagService.findAll().size();

        // when
        tagService.save(null);

        // then
        assertThat(tagService.findAll()).hasSize(initialSize);
    }
}
