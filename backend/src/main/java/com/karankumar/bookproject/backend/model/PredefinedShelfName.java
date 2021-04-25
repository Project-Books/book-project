package com.karankumar.bookproject.backend.model;

public enum PredefinedShelfName {
    TO_READ("To read"),
    READING("Reading"),
    READ("Read"),
    DID_NOT_FINISH("Did not finish");

    private final String name;

    PredefinedShelfName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
