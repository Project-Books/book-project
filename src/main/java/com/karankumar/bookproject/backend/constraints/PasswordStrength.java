package com.karankumar.bookproject.backend.constraints;

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
}
