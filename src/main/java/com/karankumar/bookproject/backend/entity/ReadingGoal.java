package com.karankumar.bookproject.backend.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Represents a reading goal: the number of books or pages a user wants to have read by the end of the year
 */
@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(callSuper = true)
public class ReadingGoal extends BaseEntity {
    public enum GoalType {
        PAGES("Pages"),
        BOOKS("Books");

        private String goalType;

        GoalType(String goalType) {
            this.goalType = goalType;
        }

        @Override
        public String toString() {
            return goalType;
        }
    }

    /**
     * Target represents either the number of pages or books the user wants to read
     */
    @Min(value = 1)
    private int target;

    @NotNull
    private GoalType goalType;

    public ReadingGoal(@Min(value = 1) int target, @NotNull GoalType goalType) {
        this.target = target;
        this.goalType = goalType;
    }
}
