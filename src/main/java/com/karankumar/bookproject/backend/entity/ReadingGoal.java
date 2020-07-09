package com.karankumar.bookproject.backend.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.validation.constraints.Min;

/**
 * Represents a reading goal: the number of books or pages a user wants to have read by the end of the year
 */
@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class ReadingGoal extends BaseEntity {

    @Min(value = 1)
    private int booksToRead;

    @Min(value = 1)
    private Integer pagesToRead; // an Integer instead of an int so that the constructor overloading works

    public ReadingGoal(int booksToRead) {
        this.booksToRead = booksToRead;
    }

    public ReadingGoal(@Min(value = 1) Integer pagesToRead) {
        this.pagesToRead = pagesToRead;
    }
}
