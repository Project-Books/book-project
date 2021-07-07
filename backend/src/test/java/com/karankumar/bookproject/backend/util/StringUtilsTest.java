/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2020  Karan Kumar

 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("StringUtils should")
class StringUtilsTest {
    @Test
    void returnSingularStringOnPluralize() {
        assertThat(StringUtils.pluralize("book", 1))
                .isEqualTo("book");
    }

    @Test
    void returnPluralStringOnPluralize() {
        assertThat(StringUtils.pluralize("book", 2))
                .isEqualTo("books");
    }

    @Test
    void pluralize() {

        assertEquals("dogs", StringUtils.pluralize("dog", 2));
        assertEquals("cats", StringUtils.pluralize("cat", 2));
        assertEquals("buses", StringUtils.pluralize("bus", 2));
        assertEquals("marshes", StringUtils.pluralize("marsh", 2));
        assertEquals("lunches", StringUtils.pluralize("lunch", 2));
        assertEquals("taxes", StringUtils.pluralize("tax", 2));
        assertEquals("blitzes", StringUtils.pluralize("blitz", 2));
        assertEquals("fezzes", StringUtils.pluralize("fez", 2));
        assertEquals("gasses", StringUtils.pluralize("gas", 2));
        assertEquals("wives", StringUtils.pluralize("wife", 2));
        assertEquals("wolves", StringUtils.pluralize("wolf", 2));
        assertEquals("roofs", StringUtils.pluralize("roof", 2));
        assertEquals("beliefs", StringUtils.pluralize("belief", 2));
        assertEquals("chefs", StringUtils.pluralize("chef", 2));
        assertEquals("chiefs", StringUtils.pluralize("chief", 2));
        assertEquals("cities", StringUtils.pluralize("city", 2));
        assertEquals("puppies", StringUtils.pluralize("puppy", 2));
        assertEquals("rays", StringUtils.pluralize("ray", 2));
        assertEquals("boys", StringUtils.pluralize("boy", 2));
        assertEquals("potatoes", StringUtils.pluralize("potato", 2));
        assertEquals("tomatoes", StringUtils.pluralize("tomato", 2));
        assertEquals("photos", StringUtils.pluralize("photo", 2));
        assertEquals("pianos", StringUtils.pluralize("piano", 2));
        assertEquals("halos", StringUtils.pluralize("halo", 2));
        assertEquals("rays", StringUtils.pluralize("ray", 2));
        assertEquals("cacti", StringUtils.pluralize("cactus", 2));
        assertEquals("foci", StringUtils.pluralize("focus", 2));
        assertEquals("analyses", StringUtils.pluralize("analysis", 2));
        assertEquals("ellipses", StringUtils.pluralize("ellipsis", 2));
        assertEquals("phenomena", StringUtils.pluralize("phenomenon", 2));
        assertEquals("criteria", StringUtils.pluralize("criterion", 2));
        assertEquals("sheep", StringUtils.pluralize("sheep", 2));
        assertEquals("series", StringUtils.pluralize("series", 2));
        assertEquals("species", StringUtils.pluralize("species", 2));
        assertEquals("deer", StringUtils.pluralize("deer", 2));
        assertEquals("children", StringUtils.pluralize("child", 2));
        assertEquals("geese", StringUtils.pluralize("goose", 2));
        assertEquals("men", StringUtils.pluralize("man", 2));
        assertEquals("women", StringUtils.pluralize("woman", 2));
        assertEquals("teeth", StringUtils.pluralize("tooth", 2));
        assertEquals("feet", StringUtils.pluralize("foot", 2));
        assertEquals("mice", StringUtils.pluralize("mouse", 2));
        assertEquals("people", StringUtils.pluralize("person", 2));
    }

    @Test
    void returnPluralFromCapitalLetters() {
        assertEquals("dogs", StringUtils.pluralize("DOG", 2));
    }
}
