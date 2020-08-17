/*
    The book project lets a user keep track of different books they've read, are currently reading or would like to read
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
    warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with this program.
    If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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
