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

package com.karankumar.bookproject.backend.util;

import org.atteo.evo.inflector.English;

import java.util.HashMap;

public final class StringUtils {
    private StringUtils() {
    }

    private static HashMap<String, String> pluralsMap = new HashMap<>();

    /**
     * Determine if a String should be singular or plural
     *
     * @param num     the number of book or pages
     * @param itemStr the String that will be pluralized
     * @return either the original String or the original string with an "s" concatenated to it
     */
    public static String pluralize(String itemStr, int num) {
        itemStr = itemStr.toLowerCase();
        initializeMap();
        if (pluralsMap.containsKey(itemStr) && num > 1)
            return pluralsMap.get(itemStr);

        return English.plural(itemStr, num);
    }

    private static void initializeMap() {
        if (!pluralsMap.isEmpty())
            return;

        pluralsMap.put("fez", "fezzes");
        pluralsMap.put("gas", "gasses");
        pluralsMap.put("halo", "halos");
        pluralsMap.put("cactus", "cacti");
        pluralsMap.put("focus", "foci");
        pluralsMap.put("person", "people");

    }
}
