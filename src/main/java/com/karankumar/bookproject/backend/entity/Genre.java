/*
    The book project lets a user keep track of different books they've read, are currently reading or would like to read
    Copyright (C) 2020  Karan Kumar

    This program is free software: you can redistribute it and/or modify it under the terms of the
    GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
    warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along with this program.
    If not, see <https://www.gnu.org/licenses/>.
 */

package com.karankumar.bookproject.backend.entity;

public enum Genre {
    // This should be kept in alphabetical order
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
