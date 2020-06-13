/*
    The book project lets a user keep track of different books they've read, are currently reading or would like to read
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.karankumar.bookproject.backend.model;

/**
 * A rating scale for a {@code Book} from 0 to 10 (inclusive) that goes up in increments of 0.5.
 */
public enum RatingScale {
    NO_RATING("No rating"),
    ZERO("0/10"),
    ZERO_POINT_FIVE("0.5/10"),
    ONE("1/10"),
    ONE_POINT_FIVE("1.5/10"),
    TWO("2/10"),
    TWO_POINT_FIVE("2.5/10"),
    THREE("3/10"),
    THREE_POINT_FIVE("3.5/10"),
    FOUR("4/10"),
    FOUR_POINT_FIVE("4.5/10"),
    FIVE("5/10"),
    FIVE_POINT_FIVE("5.5/10"),
    SIX("6/10"),
    SIX_POINT_FIVE("6.5/10"),
    SEVEN("7/10"),
    SEVEN_POINT_FIVE("7.5/10"),
    EIGHT("8/10"),
    EIGHT_POINT_FIVE("8.5/10"),
    NINE("9/10"),
    NINE_POINT_FIVE("9.5/10"),
    TEN("10/10");

    private String rating;

    RatingScale(String rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return rating;
    }
}
