package com.karankumar.bookproject.backend.entity;

import com.karankumar.bookproject.backend.entity.book.BookProgressId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BookProgressIdTest {

    @Test
    @DisplayName("BookProgressId Class should return true")
    void compareBySameBookAndUserId() {
        BookProgressId id1 = new BookProgressId(10L, 5L);
        BookProgressId id2 = new BookProgressId(10L, 5L);
        assertThat(id1).isEqualTo(id2);
    }

    @Test
    @DisplayName("BookProgressId Class should return true")
    void compareBySameObject() {
        BookProgressId id = new BookProgressId(10L, 5L);
        assertThat(id).isEqualTo(id);
    }

    @Test
    @DisplayName("BookProgressId Class should return false")
    void compareByNull() {
        BookProgressId id = new BookProgressId(10L, 5L);
        assertThat(id).isNotEqualTo(null);
    }

    @Test
    @DisplayName("BookProgressId Class should return false")
    void compareByAnotherObject() {
        BookProgressId id = new BookProgressId(10L, 5L);
        Integer x = 10;
        assertThat(x).isNotEqualTo(id);
    }

    @Test
    @DisplayName("BookProgressId Class should return false")
    void compareByHashCode() {
        BookProgressId id1 = new BookProgressId(10L, 5L);
        BookProgressId id2 = new BookProgressId(10L, 5L);
        assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
    }

}
