package com.karankumar.bookproject.backend.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.nulabinc.zxcvbn.Zxcvbn;

public class PasswordStrengthValidator implements ConstraintValidator<PasswordStrengthCheck, String>{
    
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
