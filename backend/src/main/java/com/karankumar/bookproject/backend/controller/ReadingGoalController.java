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
package com.karankumar.bookproject.backend.controller;

import com.karankumar.bookproject.backend.model.ReadingGoal;
import com.karankumar.bookproject.backend.service.ReadingGoalService;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static com.karankumar.bookproject.backend.model.ReadingGoal.*;

@RestController
@RequestMapping("/api/goal")
public class ReadingGoalController {

    private final ReadingGoalService readingGoalService;
    private final ModelMapper modelMapper;

    public static final String READING_GOAL_NOT_FOUND = "Reading goal not found.";
    public static final String GOAL_TYPE_NOT_FOUND = "Goal type not found.";
    public static final String TARGET_BAD_REQUEST = "Target minimal value is 1.";

    @Autowired
    ReadingGoalController(ReadingGoalService readingGoalService, ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        Converter<String, GoalType> readingGoalFormatter = new AbstractConverter<>() {
            public GoalType convert(String goalType) {
                return GoalType.valueOf(goalType.toUpperCase());
            }
        };
        this.modelMapper.addConverter(readingGoalFormatter);
        this.readingGoalService = readingGoalService;
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete")
    public void deleteReadingGoal() {
        readingGoalService.deleteAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/add")
    public void addReadingGoal(@RequestParam(value = "goalType") String goalType,
                               @RequestParam(value = "target") int target) {
        try {
            checkReadingGoalsAndSave(goalType, target);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, GOAL_TYPE_NOT_FOUND);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/update")
    public void updateReadingGoal(@RequestParam(value = "goalType") String goalType,
                                  @RequestParam(value = "target") int target) {
        try {
            checkReadingGoalsAndSave(goalType, target);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, GOAL_TYPE_NOT_FOUND);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/previous")
    public List<ReadingGoal> getPreviousReadingGoals() {
        return readingGoalService.findAll();
    }

    @GetMapping("/current")
    public ReadingGoal getExistingReadingGoal() {
        return readingGoalService.findAll().stream().findFirst().orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, READING_GOAL_NOT_FOUND)
        );
    }

    private void checkReadingGoalsAndSave(String goalType, int target) {

        if (target > 0) {
            try {
                GoalType newGoalType = convertToReadingGoalEnum(goalType);
                ReadingGoal newReadingGoal = new ReadingGoal(target, newGoalType);
                readingGoalService.save(newReadingGoal);
            }catch(Exception e){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, GOAL_TYPE_NOT_FOUND);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, TARGET_BAD_REQUEST);
        }
    }



    private GoalType convertToReadingGoalEnum(String goalType){
        return modelMapper.map(goalType.toUpperCase(), GoalType.class);
    }

}