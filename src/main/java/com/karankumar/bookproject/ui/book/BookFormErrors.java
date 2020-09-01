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

package com.karankumar.bookproject.ui.book;

public class BookFormErrors {
    public static final String BOOK_TITLE_ERROR = "Please provide a book title";
    public static final String FIRST_NAME_ERROR = "Please enter the author's first name";
    public static final String LAST_NAME_ERROR = "Please enter the author's last name";
    public static final String SHELF_ERROR = "Please select a shelf";
    public static final String SERIES_POSITION_ERROR = "Series position must be at least 1";
    public static final String PAGE_NUMBER_ERROR = "There must be at least one page in the book";
    public static final String FINISH_DATE_ERROR = "The date you finished reading the book " +
            "cannot be earlier than the date you started reading the book";
    public static final String AFTER_TODAY_ERROR = "The date you %s reading the book cannot be " +
            "after today's date.";
}
