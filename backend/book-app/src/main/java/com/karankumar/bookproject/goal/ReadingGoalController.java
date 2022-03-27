/*
 * The book project lets a user keep track of different books they would like to read, are currently
 * reading, have read or did not finish.
 * Copyright (C) 2021  Karan Kumar
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.  See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <https://www.gnu.org/licenses/>.
 *
 */
package com.karankumar.bookproject.goal;

import com.karankumar.bookproject.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.Mockito.mock;

@RestController
@RequestMapping(Mappings.GOAL)
public class ReadingGoalController {
  private final ReadingGoalService readingGoalService;

  public static final String READING_GOAL_NOT_FOUND = "Reading goal not found.";
  public static final String TARGET_BAD_REQUEST = "Minimum target value is 1.";

  public static class Endpoints {
    private Endpoints() {}

    public static final String ADD_BOOKS = "/add/books";
    public static final String ADD_PAGES = "/add/pages";
    public static final String PREVIOUS = "/previous";
    public static final String CURRENT = "/current";
  }

  @Autowired
  public ReadingGoalController(ReadingGoalService readingGoalService) {
    this.readingGoalService = readingGoalService;
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping()
  public void deleteReadingGoal() {
    readingGoalService.deleteAll();
  }

  // This endpoint will be used for both adding and updating, since at most one goal
  // (pages or books) is allowed at a time
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(Endpoints.ADD_BOOKS)
  public void addBookReadingGoal(@RequestParam(value = "target") int target) {
    if (target <= 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, TARGET_BAD_REQUEST);
    }

    ReadingGoal readingGoal = new ReadingGoal(target, ReadingGoal.GoalType.PAGES);
    readingGoalService.save(readingGoal);
  }

  // This endpoint will be used for both adding and updating, since at most one goal
  // (pages or books) is allowed at a time
  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping(Endpoints.ADD_PAGES)
  public void addPagesReadingGoal(@RequestParam(value = "target") int target) {
    if (target <= 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, TARGET_BAD_REQUEST);
    }

    ReadingGoal readingGoal = new ReadingGoal(target, ReadingGoal.GoalType.PAGES);
    readingGoalService.save(readingGoal);
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping(Endpoints.PREVIOUS)
  public List<ReadingGoal> getPreviousReadingGoals() {
    return readingGoalService.findAll();
  }

  @GetMapping(Endpoints.CURRENT)
  public ReadingGoal getExistingReadingGoal() {
    return readingGoalService.findAll().stream()
        .findFirst()
        .orElseThrow(
            () -> new ResponseStatusException(HttpStatus.NOT_FOUND, READING_GOAL_NOT_FOUND));
  }
}
