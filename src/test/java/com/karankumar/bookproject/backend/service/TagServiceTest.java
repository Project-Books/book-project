package com.karankumar.bookproject.backend.service;

import com.karankumar.bookproject.annotations.IntegrationTest;
import com.karankumar.bookproject.backend.entity.Tag;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

import javax.transaction.Transactional;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
@DisplayName("TagService should")
class TagServiceTest {
    private final TagService tagService;

    @Autowired
    TagServiceTest(BookService bookService, TagService tagService) {
        // Tags can only be deleted if it does not belong to a book, hence why the bookService needs
        // to be reset
        bookService.deleteAll();
        tagService.deleteAll();
        this.tagService = tagService;
    }

    @Test
    void saveAValidTag() {
        // given
        Tag tag = new Tag("dystopian");

        // when
        tagService.save(tag);

        // then
        assertThat(tagService.findById(tag.getId())).isNotNull();
    }

    @Test
    @DisplayName("throw exception on an attempt to save a null tag")
    void throwExceptionWhenSavingANullTag() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> tagService.save(null));
    }

    @Test
    @DisplayName("throw exception on an attempt to delete a null tag")
    void throwExceptionWhenDeletingANullTag() {
        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> tagService.delete(null));
    }

    @Test
    @Transactional
    void deleteExistingTag() {
        // given
        Tag tag = new Tag("dystopian");
        tagService.save(tag);

        Long tagId = tag.getId();

        // when
        tagService.delete(tag);

        // then
        assertThatThrownBy(() -> tagService.findById(tagId)).isInstanceOf(
                JpaObjectRetrievalFailureException.class);
    }

    @Test
    void findAllSavedTags() {
        // given
        Tag tag1 = new Tag("favourites");
        Tag tag2 = new Tag("dystopian");
        tagService.save(tag1);
        tagService.save(tag2);

        // when
        List<Tag> allTags = tagService.findAll();

        // then
        assertThat(allTags).contains(tag1, tag2);
    }

    @AfterEach
    public void tearDown() {
        resetTagService();
    }

    private void resetTagService() {
        tagService.deleteAll();
    }
}
