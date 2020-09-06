/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish. Copyright (C) 2020 Karan Kumar This program is free
 * software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version. This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received
 * a copy of the GNU General Public License along with this program. If not, see
 * <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.utils;

import java.text.MessageFormat;
import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

public class PredefinedShelfUtilsTest {

    private static final List<String> PREDEFINED_SHELVES = List.of("To read", "Reading", "Read", "Did not finish");
    private static final List<String> INVALID_SHELVES = List.of("Too read", "Readin", "Do not finish", "Shelf");
    private static final String ERROR_MSG = "Shelf with name ''{0}'' does not match any predefined shelf";

    @Test
    void testAllPredefinedShelves() {
        SoftAssertions softly = new SoftAssertions();

        PREDEFINED_SHELVES.stream().forEach(shelfName -> {
            String errorMsg = MessageFormat.format(ERROR_MSG, shelfName);
            softly.assertThat(PredefinedShelfUtils.isPredefinedShelf(shelfName)).as(errorMsg).isTrue();
        });

        softly.assertAll();
    }

    @Test
    void testAllPredefinedShelvesLowerCase() {
        SoftAssertions softly = new SoftAssertions();

        PREDEFINED_SHELVES.stream().map(String::toLowerCase).forEach(shelfName -> {
            String errorMsg = MessageFormat.format(ERROR_MSG, shelfName);
            softly.assertThat(PredefinedShelfUtils.isPredefinedShelf(shelfName)).as(errorMsg).isFalse();
        });

        softly.assertAll();
    }

    @Test
    void testAllPredefinedShelvesUpperCase() {
        SoftAssertions softly = new SoftAssertions();

        PREDEFINED_SHELVES.stream().map(String::toUpperCase).forEach(shelfName -> {
            String errorMsg = MessageFormat.format(ERROR_MSG, shelfName);
            softly.assertThat(PredefinedShelfUtils.isPredefinedShelf(shelfName)).as(errorMsg).isFalse();
        });

        softly.assertAll();
    }

    @Test
    void testInvalidShelves() {
        SoftAssertions softly = new SoftAssertions();

        INVALID_SHELVES.stream().forEach(shelfName -> {
            String errorMsg = MessageFormat.format(ERROR_MSG, shelfName);
            softly.assertThat(PredefinedShelfUtils.isPredefinedShelf(shelfName)).as(errorMsg).isFalse();
        });

        softly.assertAll();
    }
}

