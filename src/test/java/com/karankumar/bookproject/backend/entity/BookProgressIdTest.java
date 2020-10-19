package com.karankumar.bookproject.backend.entity;

import com.karankumar.bookproject.backend.entity.book.BookProgressId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BookProgressIdTest {

    @Test
    @DisplayName("BookProgressId Class should return true")
    void compareBySameBookAndUserId() {
        BookProgressId id1 = new BookProgressId(10L, 5L);
        BookProgressId id2 = new BookProgressId(10L, 5L);
        assertEquals(id2, id1);
    }

    @Test
    @DisplayName("BookProgressId Class should return true")
    void compareBySameObject() {
        BookProgressId id = new BookProgressId(10L, 5L);
        assertEquals(id, id);
    }

    @Test
    @DisplayName("BookProgressId Class should return false")
    void compareByNull() {
        BookProgressId id = new BookProgressId(10L, 5L);
        assertNotEquals(id, null);
    }

    @Test
    @DisplayName("BookProgressId Class should return false")
    void compareByAnotherObject() {
        BookProgressId id = new BookProgressId(10L, 5L);
        Integer x = 10;
        assertNotEquals(x, id);
    }

    @Test
    @DisplayName("BookProgressId Class should return false")
    void compareByHashCode() {
        BookProgressId id1 = new BookProgressId(10L, 5L);
        BookProgressId id2 = new BookProgressId(10L, 5L);
        assertEquals(id1.hashCode(), (id2.hashCode()));
    }

}
