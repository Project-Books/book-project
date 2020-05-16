package com.karankumar.bookproject.backend.model;

/**
 * @author karan on 08/05/2020
 */
// This should be kept in alphabetical order
public enum Genre {
    ADVENTURE("Adventure"),
    ANTHOLOGY("Anthology"),
    ART("Art"),
    AUTOBIOGRAPHY("Autobiography"),
    BIOGRAPHY("Biography"),
    BUSINESS("Business"),
    CHILDREN("Children"),
    CLASSIC("Classic"),
    COOKBOOK("Cookbook"),
    COMEDY("Comedy"),
    COMICS("Comics"),
    CRIME("Crime"),
    DRAMA("Drama"),
    ESSAY("Essay"),
    FANTASY("Fantasy"),
    FABLE("Fable"),
    FAIRY_TALE("Fairy tale"),
    FAN_FICTION("Fan fiction"),
    FICTION("Fiction"),
    HEALTH("Health"),
    HISTORY("History"),
    HISTORICAL_FICTION("Historical fiction"),
    HORROR("Horror"),
    MEMOIR("Memoir"),
    MYSTERY("Mystery"),
    NON_FICTION("Non-fiction"),
    PERIODICAL("Periodical"),
    PHILOSOPHY("Philosophy"),
    POETRY("Poetry"),
    PSYCHOLOGY("Psychology"),
    REFERENCE("Reference"),
    RELIGION("Religion"),
    ROMANCE("Romance"),
    SATIRE("Satire"),
    SCIENCE("Science"),
    SCIENCE_FICTION("Science fiction"),
    SELF_HELP("Self Help"),
    SHORT_STORY("Short story"),
    SPORTS("Sports"),
    THRILLER("Thriller"),
    TRAVEL("Travel"),
    YOUNG_ADULT("Young adult");

    private String genre;

    Genre(String genre) {
	this.genre = genre;
    }

    @Override
    public String toString() {
	return genre;
    }
}
