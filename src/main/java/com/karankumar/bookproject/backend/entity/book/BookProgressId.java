package com.karankumar.bookproject.backend.entity.book;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Embeddable
public class BookProgressId implements Serializable {

    @Column
    private Long bookId;

    @Column
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BookProgressId bookProgressId = (BookProgressId) o;
        return bookId.equals(bookProgressId.bookId) &&
                userId.equals(bookProgressId.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, userId);
    }
}
