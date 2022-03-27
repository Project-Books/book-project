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

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.nulabinc.zxcvbn.Zxcvbn;

public class PasswordStrengthValidator
    implements ConstraintValidator<PasswordStrengthCheck, String> {

  private PasswordStrength passwordStrength;

  @Override
  public void initialize(PasswordStrengthCheck constraintAnnotation) {
    this.passwordStrength = constraintAnnotation.value();
  }

  @Override
  public boolean isValid(String password, ConstraintValidatorContext context) {
    return new Zxcvbn().measure(password).getScore() >= passwordStrength.getStrengthNum();
  }
}
