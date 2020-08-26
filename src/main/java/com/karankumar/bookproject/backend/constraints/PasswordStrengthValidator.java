package com.karankumar.bookproject.backend.constraints;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.nulabinc.zxcvbn.Zxcvbn;

public class PasswordStrengthValidator implements ConstraintValidator<PasswordStrengthCheck, String>{
    
    private PasswordDescriptor passwordDescriptor;
    
    @Override
    public void initialize(PasswordStrengthCheck constraintAnnotation) {
	this.passwordDescriptor = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
	return new Zxcvbn().measure(password).getScore() >= passwordDescriptor.getStrengthNum();
    }
}
