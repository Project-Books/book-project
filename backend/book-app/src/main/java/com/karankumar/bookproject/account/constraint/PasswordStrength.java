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

package com.karankumar.bookproject.account.constraint;

import com.karankumar.bookproject.ExcludeFromJacocoGeneratedReport;

@ExcludeFromJacocoGeneratedReport
public enum PasswordStrength {
  WEAK("Weak", 0),
  FAIR("Fair", 1),
  GOOD("Good", 2),
  STRONG("Strong", 3),
  VERY_STRONG("Very strong", 4);

  private final String strength;
  private final int strengthNum;

  PasswordStrength(String strength, int strengthNum) {
    this.strength = strength;
    this.strengthNum = strengthNum;
  }

  @Override
  public String toString() {
    return strength;
  }

  public int getStrengthNum() {
    return strengthNum;
  }

  public static PasswordStrength fromValue(int strengthNum) {
    int length = PasswordStrength.values().length - 1;
    if (strengthNum >= 0 && strengthNum <= length) {
      return PasswordStrength.values()[strengthNum];
    }
    String message =
        String.format(
            "The password score has to lie between 0 and %d " + "(inclusive). You entered in %d",
            length, strengthNum);
    throw new IllegalArgumentException(message);
  }
}
