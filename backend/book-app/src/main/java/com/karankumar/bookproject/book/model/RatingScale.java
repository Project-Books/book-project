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

package com.karankumar.bookproject.book.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.karankumar.bookproject.ExcludeFromJacocoGeneratedReport;
import lombok.extern.java.Log;

import java.util.Arrays;
import java.util.Optional;

@Log
@ExcludeFromJacocoGeneratedReport
public enum RatingScale {
  NO_RATING("No rating", null),
  ZERO("0/10", 0.0),
  ZERO_POINT_FIVE("0.5/10", 0.5),
  ONE("1/10", 1.0),
  ONE_POINT_FIVE("1.5/10", 1.5),
  TWO("2/10", 2.0),
  TWO_POINT_FIVE("2.5/10", 2.5),
  THREE("3/10", 3.0),
  THREE_POINT_FIVE("3.5/10", 3.5),
  FOUR("4/10", 4.0),
  FOUR_POINT_FIVE("4.5/10", 4.5),
  FIVE("5/10", 5.0),
  FIVE_POINT_FIVE("5.5/10", 5.5),
  SIX("6/10", 6.0),
  SIX_POINT_FIVE("6.5/10", 6.5),
  SEVEN("7/10", 7.0),
  SEVEN_POINT_FIVE("7.5/10", 7.5),
  EIGHT("8/10", 8.0),
  EIGHT_POINT_FIVE("8.5/10", 8.5),
  NINE("9/10", 9.0),
  NINE_POINT_FIVE("9.5/10", 9.5),
  TEN("10/10", 10.0);

  @JsonValue private final String rating;
  private final Double value;

  RatingScale(String rating, Double value) {
    this.rating = rating;
    this.value = value;
  }

  @Override
  public String toString() {
    return rating;
  }

  public static Optional<Double> toDouble(RatingScale ratingScale) {
    return (ratingScale == null || ratingScale.value == null)
        ? Optional.empty()
        : Optional.of(ratingScale.value);
  }

  public static Optional<RatingScale> of(Double ratingValue) {
    if (ratingValue == null) {
      return Optional.of(RatingScale.NO_RATING);
    }

    return Arrays.stream(values())
        .filter(ratingScale -> ratingScale.value != null)
        .filter(ratingScale -> ratingScale.value.doubleValue() == ratingValue.doubleValue())
        .findFirst();
  }
}
