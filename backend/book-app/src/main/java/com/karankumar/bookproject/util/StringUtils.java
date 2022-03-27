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

package com.karankumar.bookproject.util;

public final class StringUtils {
  private StringUtils() {}

  /**
   * Determine if a String should be singular or plural
   *
   * @param num the number of book or pages
   * @param itemStr the String that will be pluralized
   * @return either the original String or the original string with an "s" concatenated to it
   */
  public static String pluralize(String itemStr, int num) {
    return (num > 1) ? (itemStr + "s") : (itemStr);
  }
}
