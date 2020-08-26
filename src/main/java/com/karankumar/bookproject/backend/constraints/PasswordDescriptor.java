package com.karankumar.bookproject.backend.constraints;

public enum PasswordDescriptor {
    WEAK("Weak", 0),
    FAIR("Fair", 1),
    GOOD("Good", 2),
    STRONG("Strong", 3),
    VERY_STRONG("Very strong", 4);

    private final String descriptor;
    private final int strengthNum;

    private PasswordDescriptor(String descriptor, int strengthNum) {
	this.descriptor = descriptor;
	this.strengthNum = strengthNum;
    }

    @Override
    public String toString() {
	return descriptor;
    }

    public int getStrengthNum() {
	return strengthNum;
    }
}
