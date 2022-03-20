/*
   The book project lets a user keep track of different books they would like to read, are currently
   reading, have read or did not finish.
   Copyright (C) 2020  Karan Kumar

   This program is free software: you can redistribute it and/or modify it under the terms of the
   GNU General Public License as published by the Free Software Foundation, either version 3 of the
   License, or (at your option) any later version.

   This program is distributed in the hope that it will be useful, but WITHOUT ANY
   WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
   PURPOSE.  See the GNU General Public License for more details.

   You should have received a copy of the GNU General Public License along with this program.
   If not, see <https://www.gnu.org/licenses/>.
*/

package com.karankumar.bookproject.goal;

import com.karankumar.bookproject.ExcludeFromJacocoGeneratedReport;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ExcludeFromJacocoGeneratedReport
public class ReadingGoal {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  private Long id;
  /** Target represents either the number of pages or books the user wants to read */
  @Min(value = 1)
  private int target;

  @NotNull private GoalType goalType;

  public ReadingGoal(@Min(value = 1) int target, @NotNull GoalType goalType) {
    this.target = target;
    this.goalType = goalType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ReadingGoal that = (ReadingGoal) o;
    return target == that.target && goalType == that.goalType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(target, goalType);
  }

  @ExcludeFromJacocoGeneratedReport
  public enum GoalType {
    PAGES("Pages"),
    BOOKS("Books");

    private final String type;

    GoalType(String goalType) {
      this.type = goalType;
    }

    @Override
    public String toString() {
      return type;
    }
  }
}
