package com.karankumar.bookproject.backend.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringUtilsTest {
    @Test
    public void pluralizeReturnsSingularString() {
        Assertions.assertEquals("book", StringUtils.pluralize("book", 1));
    }

    @Test
    public void pluralizeReturnsPluralString() {
        Assertions.assertEquals("books", StringUtils.pluralize("book", 2));
    }
}
