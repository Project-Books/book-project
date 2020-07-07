package com.karankumar.bookproject.backend.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.validation.constraints.Min;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class ReadingGoal extends BaseEntity {

    @Min(value = 1)
    private int booksToRead;

    public ReadingGoal(int booksToRead) {
        this.booksToRead = booksToRead;
    }

    public int getBooksToRead() {
        return booksToRead;
    }
}
