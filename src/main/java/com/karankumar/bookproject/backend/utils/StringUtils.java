package com.karankumar.bookproject.backend.utils;

public class StringUtils {
    private StringUtils() {}

    /**
     * Determine if a String should be singular or plural
     * @param num the number of book or pages
     * @param itemStr the String that will be pluralized
     * @return either the original String or the original string with an "s" concatenated to it
     */
    public static String pluralize(String itemStr, int num) {
        return (num > 1) ? (itemStr + "s") : (itemStr);
    }
}
