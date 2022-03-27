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

package com.karankumar.bookproject.goal;

import com.google.common.annotations.VisibleForTesting;
import com.karankumar.bookproject.book.model.Book;
import com.karankumar.bookproject.shelf.model.PredefinedShelf;
import com.karankumar.bookproject.util.DateUtils;

import javax.validation.constraints.NotNull;

import static com.karankumar.bookproject.goal.ReadingGoal.GoalType;
import static com.karankumar.bookproject.util.DateUtils.WEEKS_IN_YEAR;

public final class ReadingGoalCalculator {

  private static final String BEHIND = "behind";
  private static final String AHEAD_OF = "ahead of";

  private ReadingGoalCalculator() {}

  /**
   * Calculates the number of books that should have been read by this point in the year in order to
   * be on target to achieving the goal
   *
   * @param booksToReadThisYear the number of books to read by the end of the year (the goal)
   * @return the number of books that the user should have read by this point in the year
   */
  public static double shouldHaveRead(int booksToReadThisYear) {
    return booksToReadFromStartOfYear(booksToReadThisYear) * DateUtils.getCurrentWeekNumberOfYear();
  }

  /**
   * @param booksToReadThisYear the number of books to read by the end of the year (the goal)
   * @return the number of books that should have been read a week (on average) from the start of
   *     the year
   */
  public static double booksToReadFromStartOfYear(int booksToReadThisYear) {
    return ((double) booksToReadThisYear / WEEKS_IN_YEAR);
  }

  /**
   * Find how many books or pages have been read this year
   *
   * @param goalType either books or pages
   * @param readShelf the predefined read shelf
   * @return the number of books or pages read this year
   */
  public static int howManyReadThisYear(GoalType goalType, @NotNull PredefinedShelf readShelf) {
    int readThisYear = 0;
    boolean lookingForBooks = goalType == GoalType.BOOKS;
    for (Book book : readShelf.getBooks()) {
      // only books that have been given a finish date can count towards the reading goal
      if (bookHasFinishDateInThisYear(book)) {
        int pages = (book.getNumberOfPages() == null) ? 0 : book.getNumberOfPages();
        readThisYear += (lookingForBooks ? 1 : pages);
      }
    }

    return readThisYear;
  }

  @VisibleForTesting
  static boolean bookHasFinishDateInThisYear(Book book) {
    return book != null
        && book.getDateFinishedReading() != null
        && DateUtils.dateIsInCurrentYear(book.getDateFinishedReading());
  }

  public static int howFarAheadOrBehindSchedule(int booksToReadThisYear, int booksReadThisYear) {
    int shouldHaveRead =
        (int) (booksToReadFromStartOfYear(booksToReadThisYear))
            * DateUtils.getCurrentWeekNumberOfYear();
    return Math.abs(shouldHaveRead - booksReadThisYear);
  }

  /**
   * Calculates a user's progress towards their reading goal
   *
   * @param toRead the number of books to read by the end of the year (the goal)
   * @param read the number of books that the user has read so far
   * @return a fraction of the number of books to read over the books read. If greater than 1, 1.0
   *     is returned
   */
  public static double calculateProgressTowardsReadingGoal(int toRead, int read) {
    double progress = (toRead == 0) ? 0 : ((double) read / toRead);
    return Math.min(progress, 1.0);
  }

  /**
   * Note that this method assumes that the user is behind or ahead of schedule (and that they
   * haven't met their goal)
   *
   * @param booksReadThisYear the number of books read so far
   * @param shouldHaveRead the number of books that should have been ready by this point to be on
   *     schedule
   * @return a String denoting that the user is ahead or behind schedule
   */
  public static String behindOrAheadSchedule(int booksReadThisYear, double shouldHaveRead) {
    return (booksReadThisYear >= shouldHaveRead) ? AHEAD_OF : BEHIND;
  }
}
